package africa.Semicolon.alexandria.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBookRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull (message = "Book title cannot be null")
    @NotBlank(message = "Book title cannot be blank")
    private String title;
    @NotNull (message = "Book author cannot be null")
    @NotBlank(message = "Book author cannot be blank")
    private String author;
    @NotNull (message = "Book genre cannot be null")
    @NotBlank(message = "Book genre cannot be blank")
    private String genre;
    @NotNull (message = "Number of pages cannot be null")
    @NotBlank(message = "Number of pages cannot be blank")
    private int numberOfPages;
}
