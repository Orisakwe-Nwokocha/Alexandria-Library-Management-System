package africa.Semicolon.alexandria.services;

public interface EmailService {
    void sendEmail(String to, String subject, String template, String text);
}
