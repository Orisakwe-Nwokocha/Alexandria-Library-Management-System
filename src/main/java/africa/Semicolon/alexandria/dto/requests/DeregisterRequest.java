package africa.Semicolon.alexandria.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeregisterRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password cannot be null")
    private String password;
}