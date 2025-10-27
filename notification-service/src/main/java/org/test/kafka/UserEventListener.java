package org.test.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.test.service.EmailService;


@Component
public class UserEventListener {

    private final EmailService emailService;


    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        System.out.println("📩 Получено сообщение из Kafka: " + event);

        String subject;
        String text;

        if ("CREATE".equalsIgnoreCase(event.getOperation())) {
            subject = "Ваш аккаунт создан";
            text = String.format("Здравствуйте, %s!\nВаш аккаунт был успешно создан.", event.getName());
        } else if ("DELETE".equalsIgnoreCase(event.getOperation())) {
            subject = "Ваш аккаунт удалён";
            text = String.format("Здравствуйте, %s!\nВаш аккаунт был удалён.", event.getName());
        } else {
            subject = "Событие пользователя";
            text = event.toString();
        }

        emailService.sendEmail(event.getEmail(), subject, text);
    }
}
