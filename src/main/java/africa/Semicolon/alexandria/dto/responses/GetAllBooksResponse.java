package africa.Semicolon.alexandria.dto.responses;

import africa.Semicolon.alexandria.data.models.Book;
import lombok.Data;

import java.util.List;

@Data
public class GetAllBooksResponse {
    private List<Book> books;
}
