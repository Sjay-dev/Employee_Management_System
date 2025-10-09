package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Email;
import com.Darum.Employee.Management.System.Repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private EmailRepository emailRepository;

    public void sendEmail(String recipient, String subject, String body) {
        Email email = new Email();
        email.setRecipient(recipient);
        email.setSubject(subject);
        email.setBody(body);
        email.setSentAt(LocalDateTime.now());

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            email.setStatus("sent");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            email.setStatus("failed");
        }
        emailRepository.save(email);
    }

    public void sendPasswordResetLink(String recipient, String token) {
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String body = "You requested a password reset.\n\n" +
                "Click the link below recipient reset your password:\n" +
                resetUrl + "\n\n" +
                "If you did not request this, please ignore this email.";

        sendEmail(recipient, subject, body);
    }
}
