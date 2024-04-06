package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.data.repositories.Books;
import africa.Semicolon.alexandria.dtos.requests.AddBookRequest;
import africa.Semicolon.alexandria.dtos.requests.GetBookRequest;
import africa.Semicolon.alexandria.dtos.responses.AddBookResponse;
import africa.Semicolon.alexandria.dtos.responses.GetAllBooksResponse;
import africa.Semicolon.alexandria.dtos.responses.GetBookResponse;
import africa.Semicolon.alexandria.exceptions.BookNotFoundException;
import africa.Semicolon.alexandria.utils.Mapper;
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
    public GetBookResponse getBookBy(GetBookRequest getBookRequest) {
        return mapGetBookResponse(findBookBy(getBookRequest.getBookId()));
    }

    @Override
    public GetAllBooksResponse getAllBooks() {
        return mapGetAllBooksResponse(books.findAll());
    }

    private Book findBookBy(String id) {
        return books.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }
}
