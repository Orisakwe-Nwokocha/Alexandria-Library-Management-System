package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.Book;
import africa.Semicolon.alexandria.dto.requests.AddBookRequest;
import africa.Semicolon.alexandria.dto.responses.AddBookResponse;

public interface BookServices {

    AddBookResponse addBookWith(AddBookRequest addBookRequest);
    Book findBookBy(String id);
    void updateQuantityOf(Book book, int amount);
//        GetBookResponse getBookWith(GetBookRequest getBookRequest);
//    GetAllBooksResponse getAllBooks();
}
