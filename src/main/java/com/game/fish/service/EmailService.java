package com.game.fish.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendPurchaseConfirmationEmail(String userEmail, String username, String itemName, String category) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("franksun913@gmail.com");
        helper.setTo(userEmail);
        helper.setSubject("Purchase Confirmation");
        helper.setText(
                String.format(
                        "Dear %s,\n\nYou have successfully purchased %s in the category %s.\n\nThank you for your purchase!",
                        username, itemName, category
                ),
                false
        );

        mailSender.send(message);
    }

}
