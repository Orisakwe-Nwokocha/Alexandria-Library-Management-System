package africa.Semicolon.alexandria.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class LoginRequest {
    @NotNull(message = "Please enter your username")
    private String username;
    @NotNull(message = "Please enter your password")
    private String password;
}