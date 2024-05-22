package africa.Semicolon.alexandria.services;

import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.dto.responses.RegisterResponse;

public interface OtpService {
    RegisterResponse generateAndSendOtp(String email, User user);
    void validate(String otp, User user);
}
