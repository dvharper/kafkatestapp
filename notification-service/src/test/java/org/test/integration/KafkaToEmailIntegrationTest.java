package org.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.test.kafka.UserEvent;
import org.test.service.EmailService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaToEmailIntegrationTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private EmailService emailService;

    // ✅ Мокаем EmailService
    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public EmailService emailService() {
            return org.mockito.Mockito.mock(EmailService.class);
        }
    }

    // ✅ Добавляем KafkaTemplate (продюсер) только для теста
    @Configuration
    static class TestKafkaProducerConfig {

        @Bean
        public ProducerFactory<String, UserEvent> producerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    org.apache.kafka.common.serialization.StringSerializer.class);
            props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    org.springframework.kafka.support.serializer.JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, UserEvent> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }

    @Test
    void whenUserCreatedEventPublished_thenWelcomeEmailSent() {
        UserEvent event = new UserEvent("CREATE", "alice@test.com", "Alice");
        kafkaTemplate.send("user-events", event);
        kafkaTemplate.flush();

        verify(emailService, timeout(7000))
                .sendEmail(
                        eq("alice@test.com"),
                        contains("Аккаунт создан"),
                        contains("Ваш аккаунт был успешно создан")
                );
    }

    @Test
    void whenUserDeletedEventPublished_thenGoodbyeEmailSent() {
        UserEvent event = new UserEvent("DELETE", "bob@test.com", "Bob");
        kafkaTemplate.send("user-events", event);
        kafkaTemplate.flush();

        verify(emailService, timeout(7000))
                .sendEmail(
                        eq("bob@test.com"),
                        contains("Аккаунт удалён"),
                        contains("Ваш аккаунт был удалён")
                );
    }
}
