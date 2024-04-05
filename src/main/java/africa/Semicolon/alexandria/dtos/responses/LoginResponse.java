package africa.Semicolon.alexandria.dtos.responses;

import lombok.Data;

@Data
public final class LoginResponse {
    private String id;
    private String username;
    private boolean isLoggedIn;
}