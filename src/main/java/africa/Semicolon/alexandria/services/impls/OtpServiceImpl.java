package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.data.models.Otp;
import africa.Semicolon.alexandria.data.models.User;
import africa.Semicolon.alexandria.data.repositories.OtpRepository;
import africa.Semicolon.alexandria.dto.responses.RegisterResponse;
import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.EmailService;
import africa.Semicolon.alexandria.services.OtpService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public africa.Semicolon.alexandria.dto.responses.RegisterResponse generateAndSendOtp(String email, User user) {
        otpRepository.deleteByUsername(user.getUsername());

        String otp = generateOtp();
        Otp newOtp = new Otp();
        newOtp.setOtp(otp);
        newOtp.setUsername(user.getUsername());
        newOtp.setOtpGeneratedAt(LocalDateTime.now());

        emailService.sendEmail("orisakwenwokocha1@gmail.com", "One-Time Password", otp);
        otpRepository.save(newOtp);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setMessage("Otp Generated");
        return registerResponse;
    }

    @Override
    public void validate(String otp, User user) {
        Otp foundOtp = otpRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AlexandriaAppException("Otp not found"));

        var submissionTime = LocalDateTime.now().minusMinutes(5);
        LocalDateTime generatedTime = foundOtp.getOtpGeneratedAt();

        boolean isAfter = submissionTime.isAfter(generatedTime);
        if (isAfter) {
            otpRepository.delete(foundOtp);
            throw new AlexandriaAppException("OTP is invalid or expired");
        }
        if (!foundOtp.getOtp().equals(otp)) throw new AlexandriaAppException("OTP is invalid or expired");

        otpRepository.delete(foundOtp);
    }

    private static String generateOtp() {
        return RandomStringUtils.randomNumeric(6);
    }

}
