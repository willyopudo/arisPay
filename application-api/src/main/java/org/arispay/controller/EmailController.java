package org.arispay.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.arispay.data.EmailRequest;
import org.arispay.data.dtoauth.EmailDetails;
import org.arispay.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/api/v1/email")
@SecurityRequirement(name = "Bearer Authentication")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @PostMapping("/test-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {

        EmailDetails emailDetails = new EmailDetails(emailRequest.getTo(), "", emailRequest.getSubject(), "");

        Context context = new Context();
        // Set variables for the template from the POST request data
        context.setVariable("message", emailRequest.getMessage());
        context.setVariable("subject", emailRequest.getSubject());
        try {
            emailService.sendHTMLMail(emailDetails, "companyUserEmailTemplate", context);
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }
}
