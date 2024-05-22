package africa.Semicolon.alexandria.services.impls;

import africa.Semicolon.alexandria.exceptions.AlexandriaAppException;
import africa.Semicolon.alexandria.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;


    @Override
    public void sendEmail(String to, String subject, String name)  {
        validate(to);
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom("orisha.spring.maail@gmail.com");
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(name);
//            mailSender.send(message);
//        } catch (Exception e) {
//            System.out.println("Error: " +e.getMessage());
//            throw new AlexandriaAppException("Error sending email");
//        }


        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", name);
            String html = templateEngine.process("email-template", context);

            helper.setTo(to);
            helper.setFrom("orisha.spring.maail@gmail.com");
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Error: " +e.getMessage());
            throw new AlexandriaAppException("Error sending email");
        }
    }

    public static void validate(String email) {
        email = email.toLowerCase();
        String regex = "([a-z0-9]+\\.)?[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}";
        if (!email.matches(regex)) throw new AlexandriaAppException("Email is invalid");
    }



}
