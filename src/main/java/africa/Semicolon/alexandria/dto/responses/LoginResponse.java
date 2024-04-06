package africa.Semicolon.alexandria.dto.responses;

import lombok.Data;

@Data
public final class LoginResponse {
    private String id;
    private String username;
    private boolean isLoggedIn;
}