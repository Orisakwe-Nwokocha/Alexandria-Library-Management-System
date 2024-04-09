package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.dto.requests.AddBookRequest;
import africa.Semicolon.alexandria.dto.requests.RemoveBookRequest;
import africa.Semicolon.alexandria.dto.responses.AddBookResponse;
import africa.Semicolon.alexandria.dto.responses.GetAllBooksResponse;
import africa.Semicolon.alexandria.dto.responses.GetBookResponse;
import africa.Semicolon.alexandria.dto.responses.RemoveBookResponse;

public interface BookServices {

    AddBookResponse addBookWith(AddBookRequest addBookRequest);
    Book findBookBy(String id);
    void updateQuantityOf(Book book, int amount);
    GetBookResponse getBookBy(String id);
    GetAllBooksResponse getAllBooks();
    RemoveBookResponse removeBookWith(RemoveBookRequest removeBookRequest);
}
