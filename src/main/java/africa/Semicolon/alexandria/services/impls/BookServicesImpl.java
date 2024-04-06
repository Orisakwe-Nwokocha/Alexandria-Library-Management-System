package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.repositories.Books;
import africa.Semicolon.alexandria.dto.requests.AddBookRequest;
import africa.Semicolon.alexandria.dto.responses.AddBookResponse;
import africa.Semicolon.alexandria.exceptions.BadRequestException;
import africa.Semicolon.alexandria.exceptions.BookNotFoundException;
import africa.Semicolon.alexandria.services.BookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static africa.Semicolon.alexandria.utils.Mapper.map;
import static africa.Semicolon.alexandria.utils.Mapper.mapAddBookResponse;

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

//    @Override
//    public GetBookResponse getBookWith(GetBookRequest getBookRequest) {
//        return mapGetBookResponse(findBookBy(getBookRequest.getBookId()));
//    }
//
//    @Override
//    public GetAllBooksResponse getAllBooks() {
//        return mapGetAllBooksResponse(books.findAll());

//    }
}
