package africa.Semicolon.alexandria.dto.requests;

import jakarta.validation.constraints.Min;
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
    @NotNull (message = "Quantity cannot be null")
    @NotBlank(message = "Quantity cannot be blank")
    @Min(value = 1, message = "Quantity cannot be less than 1")
    private int quantity;
}
