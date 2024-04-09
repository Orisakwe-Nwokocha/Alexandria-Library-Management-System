package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.repositories.Books;
import africa.Semicolon.alexandria.dto.requests.AddBookRequest;
import africa.Semicolon.alexandria.dto.requests.RemoveBookRequest;
import africa.Semicolon.alexandria.dto.responses.AddBookResponse;
import africa.Semicolon.alexandria.dto.responses.GetAllBooksResponse;
import africa.Semicolon.alexandria.dto.responses.GetBookResponse;
import africa.Semicolon.alexandria.dto.responses.RemoveBookResponse;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.exceptions.BadRequestException;
import africa.Semicolon.alexandria.exceptions.BookNotFoundException;
import africa.Semicolon.alexandria.services.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static africa.Semicolon.alexandria.utils.Mapper.*;

@Service
public class BookServicesImpl implements BookServices {
    @Autowired
    private Books books;

    @Override
    public AddBookResponse addBookWith(AddBookRequest addBookRequest) {
        Book newBook = map(addBookRequest);
        Book savedBook = books.save(newBook);
        return mapAddBookResponse(savedBook);
    }

    @Override
    public Book findBookBy(String id) {
        return books.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Override
    public void updateQuantityOf(Book book, int amount) {
        int newQuantity = book.getQuantity() + amount;
        if (newQuantity < 0) throw new BadRequestException("The book quantity cannot be less than 0");
        book.setQuantity(newQuantity);
        book.setUnavailable(book.getQuantity() == 0);
        books.save(book);
    }

    @Override
    public GetBookResponse getBookBy(String id) {
        return mapGetBookResponse(findBookBy(id));
    }

    @Override
    public GetAllBooksResponse getAllBooks() {
        if (books.findAll().isEmpty()) throw new AlexandriaAppException("No available books found");
        return mapGetAllBooksResponse(books.findAll());
    }

    @Override
    public RemoveBookResponse removeBookWith(RemoveBookRequest removeBookRequest) {
        Book foundBook = findBookBy(removeBookRequest.getBookId());
        books.delete(foundBook);
        return mapRemoveBookResponse(removeBookRequest.getBookId());
    }
}
