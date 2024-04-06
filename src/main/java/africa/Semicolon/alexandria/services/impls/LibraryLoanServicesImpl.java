package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.models.Borrower;
import africa.Semicolon.alexandria.data.models.LibraryLoan;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.Borrowers;
import africa.Semicolon.alexandria.data.repositories.LibraryLoans;
import africa.Semicolon.alexandria.dto.requests.BorrowBookRequest;
import africa.Semicolon.alexandria.dto.requests.ReturnBookRequest;
import africa.Semicolon.alexandria.dto.responses.BorrowBookResponse;
import africa.Semicolon.alexandria.dto.responses.ReturnBookResponse;
import africa.Semicolon.alexandria.exceptions.BadRequestException;
import africa.Semicolon.alexandria.exceptions.IllegalBookStateException;
import africa.Semicolon.alexandria.exceptions.IllegalUserStateException;
import africa.Semicolon.alexandria.services.BookServices;
import africa.Semicolon.alexandria.services.LibraryLoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static africa.Semicolon.alexandria.utils.Mapper.*;

@Service
public class LibraryLoanServicesImpl implements LibraryLoanServices {
    @Autowired
    private LibraryLoans libraryLoans;
    @Autowired
    private BookServices bookServices;
    @Autowired
    private Borrowers borrowers;

    @Override
    public BorrowBookResponse loanBookWith(BorrowBookRequest borrowBookRequest, User member) {
        Book book = bookServices.findBookBy(borrowBookRequest.getBookId());
        validate(book);
        if (isNew(member)) createNewBorrowerWith(member);
        Borrower borrower = borrowers.findByMember(member);
        validate(borrower.getBooks(), borrowBookRequest.getBookId());
        LibraryLoan newLibraryLoan = map(borrower, book);
        updateModels(book, borrower, newLibraryLoan);
        return mapBorrowBookResponse(newLibraryLoan);
    }

    @Override
    public ReturnBookResponse returnBookWith(ReturnBookRequest returnBookRequest, User member) {
        Borrower borrower = getBorrower(member);
        LibraryLoan libraryLoan = findLibraryLoanBy(returnBookRequest.getLibraryLoanId(), borrower.getLibraryLoans());
        Book book = libraryLoan.getBook();
        updateModels(borrower, book, libraryLoan);
        return mapReturnBookResponse(libraryLoan);
    }

    private void updateModels(Borrower borrower, Book book, LibraryLoan libraryLoan) {
        bookServices.updateQuantityOf(book, 1);
        borrower.getBooks().removeIf(b -> b.getId().equals(book.getId()));
        borrower.getLibraryLoans().removeIf(l -> l.getId().equals(libraryLoan.getId()));
        libraryLoan.setReturnedAt(LocalDateTime.now());
        libraryLoans.save(libraryLoan);
        borrowers.save(borrower);
    }

    private LibraryLoan findLibraryLoanBy(String id, List<LibraryLoan> libraryLoans) {
        return libraryLoans.stream()
                .filter(libraryLoan -> libraryLoan.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(String.format("Library loan with id '%s' not found", id)));
    }

    private Borrower getBorrower(User member) {
        if (isNew(member)) throw new IllegalUserStateException("You need to borrow a book before returning the book");
        return borrowers.findByMember(member);
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
            throw new BadRequestException(String.format("Book with id '%s' already borrowed", bookId));
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
