package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.dto.requests.AddBookRequest;
import africa.Semicolon.alexandria.dto.requests.GetBookRequest;
import africa.Semicolon.alexandria.dto.responses.AddBookResponse;
import africa.Semicolon.alexandria.dto.responses.GetAllBooksResponse;
import africa.Semicolon.alexandria.dto.responses.GetBookResponse;

public interface BookServices {

    AddBookResponse addBookWith(AddBookRequest addBookRequest);
    GetBookResponse getBookWith(GetBookRequest getBookRequest);
    Book findBookBy(String id);
    GetAllBooksResponse getAllBooks();
    void updateQuantityOf(Book book, int amount);
}
