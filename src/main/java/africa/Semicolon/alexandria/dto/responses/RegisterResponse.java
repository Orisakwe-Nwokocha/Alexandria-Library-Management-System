package africa.Semicolon.alexandria.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RegisterResponse {
    private String id;
    private String username;
    private String message;
}