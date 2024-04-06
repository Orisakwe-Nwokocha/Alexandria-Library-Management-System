package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.LibraryLoan;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Borrowers;
import africa.Semicolon.alexandria.data.repositories.LibraryLoans;
import africa.Semicolon.alexandria.dtos.requests.BorrowBookRequest;
import africa.Semicolon.alexandria.dtos.responses.BorrowBookResponse;
import africa.Semicolon.alexandria.exceptions.IllegalBookStateException;
import africa.Semicolon.alexandria.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.Semicolon.alexandria.utils.Mapper.map;

@Service
public class BorrowServicesImpl implements BorrowServices {
    @Autowired
    private LibraryLoans libraryLoans;
    @Autowired
    private BookServices bookServices;
    @Autowired
    private Borrowers borrowers;

    @Override
    public BorrowBookResponse borrowBookWith(BorrowBookRequest borrowBookRequest, User member) {
        Book book = bookServices.findBookBy(borrowBookRequest.getBookId());
        validate(book);
        if (isNew(member)) createNewBorrowerWith(member);
        Borrower borrower = borrowers.findByMember(member);
        validate(borrower.getBooks(), borrowBookRequest.getBookId());
        LibraryLoan newLibraryLoan = map(borrower, book);
        updateModels(book, borrower, newLibraryLoan);
        return Mapper.map(newLibraryLoan);
    }

    private void updateModels(Book book, Borrower borrower, LibraryLoan newLibraryLoan) {
        bookServices.updateQuantityOf(book, -1);
        borrower.getBooks().add(book);
        borrower.getLibraryLoans().add(newLibraryLoan);
        libraryLoans.save(newLibraryLoan);
        borrowers.save(borrower);
    }

    private void validate(List<Book> books, String bookId) {
        if (books.stream().anyMatch(book -> book.getId().equals(bookId)))
            throw new IllegalBookStateException(String.format("Book with id %s already borrowed", bookId));
    }

    private void createNewBorrowerWith(User member) {
        Borrower newBorrower = new Borrower();
        newBorrower.setMember(member);
        borrowers.save(newBorrower);
    }

    private boolean isNew(User member) {
        return !borrowers.existsByMember(member);
    }

    private static void validate(Book book) {
        if (book.isUnavailable()) throw new IllegalBookStateException("Book is not available");
    }
}
