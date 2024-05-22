package africa.Semicolon.alexandria.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Otp {
    @Id
    private String id;
    private String otp;
    private String username;
    private LocalDateTime otpGeneratedAt;
}
