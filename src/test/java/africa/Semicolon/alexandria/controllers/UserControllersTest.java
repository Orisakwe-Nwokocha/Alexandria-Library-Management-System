package africa.Semicolon.alexandria.controllers;

import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Books;
import africa.Semicolon.alexandria.data.repositories.Borrowers;
import africa.Semicolon.alexandria.data.repositories.Users;
import africa.Semicolon.alexandria.dto.requests.*;
import africa.Semicolon.alexandria.dto.responses.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
public class UserControllersTest {
    @Autowired
    private UserControllers userControllers;
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

    private static void assertIsSuccessful(ResponseEntity<?> response, boolean expected) {
        if (response.hasBody() && response.getBody() instanceof ApiResponse apiResponse) {
            boolean success = apiResponse.isSuccessful();
            assertThat(success, is(expected));
        }
    }

    private User getUser() {
        return users.findByUsername(lowerCaseValueOf("username")).orElseThrow(()-> new IllegalArgumentException("error"));
    }
    private String getLibraryLoanId() {
        Borrower borrower = borrowers.findByMember(getUser());
        return borrower.getLibraryLoans().getFirst().getId();
    }

    private String getBookId() {
        return books.findAll().getFirst().getId();
    }


    @Test
    public void testRegister_isSuccessful_isTrue() {
        var response = userControllers.register(registerRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(CREATED));
    }

    @Test
    public void testRegister_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        var response = userControllers.register(registerRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void testLogin_isSuccessful_isTrue() {
        userControllers.register(registerRequest);
        var response = userControllers.login(loginRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void testLogin_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        loginRequest.setPassword("wrongPassword");
        var response = userControllers.login(loginRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void testLogout_isSuccessful_isTrue() {
        userControllers.register(registerRequest);
        var response = userControllers.logout(logoutRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void testLogout_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        logoutRequest.setUsername("nonExistingUsername");
        var response = userControllers.logout(logoutRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void testAddBook_isSuccessful_isTrue() {
        userControllers.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userControllers.register(registerRequest);

        var response = userControllers.addBook(addBookRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(CREATED));
    }

    @Test
    public void testAddBook_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        var response = userControllers.addBook(addBookRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void testBorrowBook_isSuccessful_isTrue() {
        userControllers.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userControllers.register(registerRequest);
        userControllers.addBook(addBookRequest);

        borrowBookRequest.setBookId(getBookId());
        var response = userControllers.borrowBook(borrowBookRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void testBorrowBook_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userControllers.register(registerRequest);
        userControllers.addBook(addBookRequest);

        borrowBookRequest.setBookId(getBookId());
        userControllers.borrowBook(borrowBookRequest);
        var response = userControllers.borrowBook(borrowBookRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }

    @Test
    public void testReturnBook_isSuccessful_isTrue() {
        userControllers.register(registerRequest);
        registerRequest.setUsername("username2");
        registerRequest.setRole("librarian");
        userControllers.register(registerRequest);
        userControllers.addBook(addBookRequest);

        borrowBookRequest.setBookId(getBookId());
        userControllers.borrowBook(borrowBookRequest);
        returnBookRequest.setLibraryLoanId(getLibraryLoanId());
        var response = userControllers.returnBook(returnBookRequest);
        assertIsSuccessful(response, true);
        assertThat(response.getStatusCode(), is(OK));
    }

    @Test
    public void testReturnBook_isSuccessful_isFalse() {
        userControllers.register(registerRequest);
        var response = userControllers.returnBook(returnBookRequest);
        assertIsSuccessful(response, false);
        assertThat(response.getStatusCode(), is(BAD_REQUEST));
    }
}