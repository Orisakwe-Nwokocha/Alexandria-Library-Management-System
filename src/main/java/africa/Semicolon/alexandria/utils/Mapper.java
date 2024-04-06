package africa.Semicolon.alexandria.utils;

import africa.Semicolon.alexandria.data.constants.Genre;
import africa.Semicolon.alexandria.data.constants.Role;
import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.LibraryLoan;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.dtos.requests.AddBookRequest;
import africa.Semicolon.alexandria.dtos.requests.RegisterRequest;
import africa.Semicolon.alexandria.dtos.responses.*;
import africa.Semicolon.alexandria.exceptions.BadRequestException;
import africa.Semicolon.alexandria.exceptions.InvalidBookGenreException;
import africa.Semicolon.alexandria.exceptions.InvalidUserRoleException;

import java.util.List;

import static africa.Semicolon.alexandria.utils.Cleaner.lowerCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cleaner.upperCaseValueOf;
import static africa.Semicolon.alexandria.utils.Cryptography.encode;

public final class Mapper {
    public static User map(RegisterRequest registerRequest) {
        String username = lowerCaseValueOf(registerRequest.getUsername());
        String password = encode(registerRequest.getPassword());
        String role = registerRequest.getRole();
        User user = new User();
        try {
            user.setRole(Role.valueOf(upperCaseValueOf(role)));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidUserRoleException(String.format("Invalid role: %s", role));
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setName(registerRequest.getName());
        return user;
    }
    public static RegisterResponse mapRegisterResponseWith(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        return registerResponse;
    }

    public static LoginResponse mapLoginResponseWith(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setLoggedIn(true);
        return loginResponse;
    }

    public static LogoutResponse mapLogoutResponseWith(User user) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setId(user.getId());
        logoutResponse.setUsername(user.getUsername());
        logoutResponse.setLoggedIn(false);
        return logoutResponse;
    }

    public static Book map(AddBookRequest addBookRequest) {
        String genre = addBookRequest.getGenre();
        Book book = new Book();
        try {
            book.setGenre(Genre.valueOf(upperCaseValueOf(genre)));
        }
        catch (IllegalArgumentException e) {
            throw new InvalidBookGenreException(String.format("Invalid book genre: %s", genre));
        }
        if (addBookRequest.getQuantity() < 0) throw new BadRequestException("The book quantity cannot be less than 1");
        book.setQuantity(addBookRequest.getQuantity());
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        return book;
    }

    public static AddBookResponse mapAddBookResponse(Book savedBook) {
        AddBookResponse addBookResponse = new AddBookResponse();
        addBookResponse.setBookId(savedBook.getId());
        return addBookResponse;
    }

    public static GetBookResponse mapGetBookResponse(Book book) {
        GetBookResponse getBookResponse = new GetBookResponse();
        getBookResponse.setBookId(book.getId());
        getBookResponse.setBook(book.toString());
        return getBookResponse;
    }

    public static GetAllBooksResponse mapGetAllBooksResponse(List<Book> books) {
        GetAllBooksResponse getAllBooksResponse = new GetAllBooksResponse();
        getAllBooksResponse.setBooks(books.toString());
        return getAllBooksResponse;
    }

    public static LibraryLoan map(Borrower borrower, Book book) {
        LibraryLoan libraryLoan = new LibraryLoan();
        libraryLoan.setBorrower(borrower);
        libraryLoan.setBook(book);
        return libraryLoan;
    }

    public static BorrowBookResponse map(LibraryLoan newLibraryLoan) {
        BorrowBookResponse borrowBookResponse = new BorrowBookResponse();
        String username = newLibraryLoan.getBorrower().getMember().getUsername();
        borrowBookResponse.setUsername(username);
        borrowBookResponse.setLibraryLoan(newLibraryLoan.toString());
        return borrowBookResponse;
    }
}
