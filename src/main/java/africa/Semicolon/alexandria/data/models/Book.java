package africa.Semicolon.alexandria.data.models;

import africa.Semicolon.alexandria.data.constants.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Books")
public class Book {
    @Id
    private String id;
    @NotNull(message = "Book title cannot be null")
    @NotBlank(message = "Book title cannot be blank")
    private String title;
    @NotNull (message = "Book author cannot be null")
    @NotBlank(message = "Book author cannot be blank")
    private String author;
    @NotNull (message = "Book genre cannot be null")
    @NotBlank(message = "Book genre cannot be blank")
    private Genre genre;
    @NotNull (message = "Number of pages cannot be null")
    @NotBlank(message = "Number of pages cannot be blank")
    private Integer quantity;
    private boolean isUnavailable = quantity == 0;

    @Override
    public String toString() {
        String format = "Book{id='%s'\ntitle='%s'\nauthor='%s'\ngenre='%s'}";
        return String.format(format, id, title, author, genre);
    }
}
