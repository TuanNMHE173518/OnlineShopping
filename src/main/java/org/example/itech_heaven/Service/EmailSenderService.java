package org.example.itech_heaven.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
        helper.setFrom("TuanNMHE173518@fpt.edu.vn");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);
    }

}
