package org.arispay.service;

import jakarta.mail.MessagingException;
import org.arispay.data.dtoauth.EmailDetails;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public interface EmailService {
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);

    String sendHTMLMail(EmailDetails details, String template, Context context)  throws MessagingException;
}
