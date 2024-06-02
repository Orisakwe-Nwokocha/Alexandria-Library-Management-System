package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.data.models.Otp;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.OtpRepository;
import africa.Semicolon.alexandria.dto.responses.RegisterResponse;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.EmailService;
import africa.Semicolon.alexandria.services.OtpService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Override
    public africa.Semicolon.alexandria.dto.responses.RegisterResponse generateAndSendOtp(String email, User user) {
        otpRepository.deleteByUsername(user.getUsername());

        String otp = generateOtp();
        Otp newOtp = new Otp();
        newOtp.setOtp(otp);
        newOtp.setUsername(user.getUsername());
        newOtp.setOtpGeneratedAt(LocalDateTime.now());

        emailService.sendEmail(email, "One-Time Password", "otp-template", otp);
        otpRepository.save(newOtp);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setMessage("Otp Generated");
        return registerResponse;
    }

    @Override
    public void validate(String otp, User user) {
        Otp foundOtp = otpRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AlexandriaAppException("Otp not found"));

        validate(otp, foundOtp);
        otpRepository.delete(foundOtp);
    }

    private static void validate(String otp, Otp foundOtp) {
        LocalDateTime submissionTime = LocalDateTime.now();
        LocalDateTime validityTime = foundOtp.getOtpGeneratedAt().plusMinutes(5);

        boolean isExpired = submissionTime.isAfter(validityTime);
        boolean isEquals = foundOtp.getOtp().equals(otp);
        if (isExpired || !isEquals) throw new AlexandriaAppException("OTP is invalid or expired");
    }

    private static String generateOtp() {
        return RandomStringUtils.randomNumeric(6);
    }

}
