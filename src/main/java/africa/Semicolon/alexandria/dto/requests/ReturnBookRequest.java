package africa.Semicolon.alexandria.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnBookRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Library Loan ID cannot be null")
    private String libraryLoanId;
}
