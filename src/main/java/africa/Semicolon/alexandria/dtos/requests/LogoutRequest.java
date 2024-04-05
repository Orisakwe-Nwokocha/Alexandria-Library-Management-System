package africa.Semicolon.alexandria.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class LogoutRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
}