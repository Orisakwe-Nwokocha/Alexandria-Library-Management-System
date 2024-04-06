package africa.Semicolon.alexandria.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetBookRequest {
    @NotNull(message = "Book ID cannot be null")
    private String bookId;
}
