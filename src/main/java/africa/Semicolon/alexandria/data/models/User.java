package africa.Semicolon.alexandria.data.models;

import africa.Semicolon.alexandria.data.constants.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Users")
public class User {
    @Id
    private String id;
    private String name;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "Role cannot be null")
    private Role role;
    private boolean isLoggedIn = true;
    private boolean withBorrowedBook;
}
