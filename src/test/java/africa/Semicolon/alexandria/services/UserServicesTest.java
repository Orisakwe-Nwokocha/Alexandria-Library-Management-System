package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Books;
import africa.Semicolon.alexandria.data.repositories.Borrowers;
import africa.Semicolon.alexandria.data.repositories.Users;
import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UserServicesTest {
    @Autowired
    private UserServices userServices;
    @Autowired
    private Users users;
    @Autowired
    private Books books;
    @Autowired
    private Borrowers borrowers;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private LogoutRequest logoutRequest;
    private AddBookRequest addBookRequest;
    private BorrowBookRequest borrowBookRequest;
    private ReturnBookRequest returnBookRequest;


    @BeforeEach
    public void setUp() {
        users.deleteAll();
        books.deleteAll();

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        registerRequest.setRole("member");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("username");

        addBookRequest = new AddBookRequest();
        addBookRequest.setUsername("username2");
        addBookRequest.setGenre("action");
        addBookRequest.setQuantity(1);

        borrowBookRequest = new BorrowBookRequest();
        borrowBookRequest.setUsername("username");

        returnBookRequest = new ReturnBookRequest();
        returnBookRequest.setUsername("username");
    }

    private User getUser() {
        return users.findByUsername(lowerCaseValueOf("username")).orElseThrow(()-> new IllegalArgumentException("error"));
    }
    private Borrower getBorrower() {
        return borrowers.findByMember(getUser());
    }

    private Book getBook() {
        return books.findAll().getFirst();
    }


    @Test
    public void registerUser_numberOfUsersIsOneTest() {
        assertThat(users.count(), is(0L));
        var registerResponse = userServices.register(registerRequest);
        assertThat(users.count(), is(1L));
        assertThat(registerResponse.getId(), notNullValue());
    }

    @Test
    public void registerSameUser_throwsUserExistsExceptionTest() {
        userServices.register(registerRequest);
        try {
            userServices.register(registerRequest);
        }
        catch (UserExistsException e) {
            assertThat(e.getMessage(), containsString("username already exists"));
        }
        assertThat(users.count(), is(1L));
    }

    @Test
    public void loginUserWithCorrectPassword_loginUserResponseIsNotNull() {
        userServices.register(registerRequest);
        assertThat(users.count(), is(1L));
        var loginResponse = userServices.login(loginRequest);
        assertThat(loginResponse.getId(), notNullValue());
        assertThat(loginResponse.isLoggedIn(), is(true));
    }

    @Test
    public void loginNonExistentUser_throwsUserNotFoundExceptionTest() {
        userServices.register(registerRequest);
        loginRequest.setUsername("Non existent username");
        try {
            userServices.login(loginRequest);
        }
        catch (UserNotFoundException e) {
            assertThat(e.getMessage(), containsString("User with 'Non existent username' not found"));
        }
    }

    @Test
    public void loginWithIncorrectPassword_throwsIncorrectPasswordExceptionTest() {
        userServices.register(registerRequest);
        loginRequest.setPassword("incorrectPassword");
        try {
            userServices.login(loginRequest);
        }
        catch (IncorrectPasswordException e) {
            assertThat(e.getMessage(), containsString("Password is not correct"));
        }
    }
    @Test
    public void logoutTest() {
        userServices.register(registerRequest);
        assertThat(users.count(), is(1L));
        var logoutResponse = userServices.logout(logoutRequest);
        assertThat(logoutResponse.getId(), notNullValue());
        assertThat(logoutResponse.isLoggedIn(), is(false));
    }

    @Test
    public void addBook_numberOfBooksIs1Test() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        assertThat(books.count(), is(0L));

        var addBookResponse = userServices.addBook(addBookRequest);
        assertThat(books.count(), is(1L));
        assertThat(addBookResponse.getBookId(), notNullValue());
        System.out.println(getBook());
    }

    @Test
    public void givenExistingBook_borrowBook_bookQuantityIs0Test() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        userServices.addBook(addBookRequest);
        assertThat(getBook().getQuantity(), is(1));

        borrowBookRequest.setBookId(getBook().getId());
        var response = userServices.borrowBook(borrowBookRequest);
        assertThat(getBook().getQuantity(), is(0));
        assertThat(response.getLibraryLoan(), notNullValue());
        System.out.println(response.getLibraryLoan());
        System.out.println(getBook().isUnavailable());
    }

    @Test
    public void givenUnavailableBook_borrowBook_IllegalBookStateExceptionIsThrownTest() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        userServices.addBook(addBookRequest);
        assertThat(getBook().getQuantity(), is(1));
        borrowBookRequest.setBookId(getBook().getId());
        userServices.borrowBook(borrowBookRequest);
        assertThat(getBook().getQuantity(), is(0));

        try {
            userServices.borrowBook(borrowBookRequest);
        }
        catch (IllegalBookStateException e) {
            assertThat(e.getMessage(), is("Book is not available"));
        }
        assertThat(getBook().getQuantity(), is(0));
    }

    @Test
    public void borrowSameBook_BadRequestExceptionIsThrownTest() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        addBookRequest.setQuantity(2);
        userServices.addBook(addBookRequest);
        assertThat(getBook().getQuantity(), is(2));
        borrowBookRequest.setBookId(getBook().getId());
        userServices.borrowBook(borrowBookRequest);
        assertThat(getBook().getQuantity(), is(1));

        try {
            userServices.borrowBook(borrowBookRequest);
        }
        catch (BadRequestException e) {
            String expectedMessage =
                    String.format("Book with id '%s' already borrowed", borrowBookRequest.getBookId());
            assertThat(e.getMessage(), is(expectedMessage));
        }
        assertThat(getBook().getQuantity(), is(1));
    }

    @Test
    public void givenBorrowedBook_returnBook_borrowerLibraryLoanIs0Test() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        userServices.addBook(addBookRequest);
        borrowBookRequest.setBookId(getBook().getId());
        var borrowBookResponse = userServices.borrowBook(borrowBookRequest);
        var borrower = getBorrower();
        assertThat(borrower.getBooks(), hasSize(1));
        assertThat(borrower.getLibraryLoans(), hasSize(1));
        assertThat(getBook().getQuantity(), is(0));
        System.out.println(borrowBookResponse.getLibraryLoan());

        for (long i = 0L; i < 15_000_000_001L; i++);

        returnBookRequest.setLibraryLoanId(borrower.getLibraryLoans().getFirst().getId());
        var returnBookResponse = userServices.returnBook(returnBookRequest);
        borrower = getBorrower();
        assertThat(returnBookResponse.getLibraryLoan(), notNullValue());
        assertThat(borrower.getBooks(), hasSize(0));
        assertThat(borrower.getLibraryLoans(), hasSize(0));
        assertThat(getBook().getQuantity(), is(1));
        System.out.println(returnBookResponse.getLibraryLoan());
    }

    @Test
    public void returnBookWithoutBeingAValidBorrower_IllegalUserStateExceptionIsThrownTaest() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        userServices.addBook(addBookRequest);
        assertThat(getBook().getQuantity(), is(1));

        try {
            userServices.returnBook(returnBookRequest);
        }
        catch (IllegalUserStateException e) {
            String expectedMessage = "You need to borrow a book before returning the book";
            assertThat(e.getMessage(), is(expectedMessage));
        }
        assertThat(getBook().getQuantity(), is(1));
    }

    @Test
    public void returnBookWithInvalidLibraryLoanId_BadRequestExceptionIsThrownTaest() {
        userServices.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userServices.register(registerRequest);
        userServices.addBook(addBookRequest);
        borrowBookRequest.setBookId(getBook().getId());
        userServices.borrowBook(borrowBookRequest);
        assertThat(getBook().getQuantity(), is(0));

        returnBookRequest.setLibraryLoanId("bunchOfStrings");
        try {
            userServices.returnBook(returnBookRequest);
        }
        catch (BadRequestException e) {
            String expectedMessage = "Library loan with id 'bunchOfStrings' not found";
            assertThat(e.getMessage(), is(expectedMessage));
        }
        assertThat(getBook().getQuantity(), is(0));
    }

}