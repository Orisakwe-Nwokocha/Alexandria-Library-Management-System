package africa.Semicolon.alexandria.data.models;

import africa.Semicolon.alexandria.data.constants.Genre;
import jakarta.validation.constraints.Min;
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
    private Genre genre;
    @NotNull (message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be less than 0")
    private Integer quantity;
    private boolean isUnavailable;

    @Override
    public String toString() {
        String format = "Book{\nid ='%s'\ntitle = '%s'\nauthor = '%s'\ngenre = '%s'\n}";
        return String.format(format, id, title, author, genre);
    }
}
