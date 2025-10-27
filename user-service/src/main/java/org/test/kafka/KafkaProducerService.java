package org.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate,
                                org.springframework.core.env.Environment env) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = env.getProperty("app.kafka.topic", "user-events");
    }

    public void sendUserEvent(UserEvent event) {
        log.info("Publishing user event to topic {}: {}", topic, event);
        kafkaTemplate.send(topic, event.getEmail(), event);
    }
}
