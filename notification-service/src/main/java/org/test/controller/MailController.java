package org.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.service.EmailService;


@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestParam String to,
                                           @RequestParam String subject,
                                           @RequestParam String text) {
        emailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Email sent to " + to);
    }
}
