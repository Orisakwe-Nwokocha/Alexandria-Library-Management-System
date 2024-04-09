package africa.Semicolon.alexandria.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveBookRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Book ID cannot be null")
    private String bookId;
}
