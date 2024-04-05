package africa.Semicolon.alexandria.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class RegisterRequest {
    private String name;
    private String emailAddress;
    private String phoneNumber;
    @NotNull(message = "Please enter a username")
    private String username;
    @NotNull(message = "Please enter a password")
    private String password;
    @NotNull(message = "Please select a role")
    private String role;
}
