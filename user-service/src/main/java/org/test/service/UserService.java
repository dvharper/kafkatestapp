package org.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.kafka.KafkaProducerService;
import org.test.kafka.UserEvent;
import org.test.model.User;
import org.test.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducer;

    public UserService(UserRepository userRepository, KafkaProducerService kafkaProducer) {
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public User createUser(String name, String email, int age) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be empty");
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }

        User user = new User(name, email, age);
        user.setCreatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);

        kafkaProducer.sendUserEvent(new UserEvent(saved.getEmail(), UserEvent.Operation.CREATE));
        return saved;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User updateUser(User user) {
        if (user == null || user.getId() == null) throw new IllegalArgumentException("User or id null");
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        userRepository.deleteById(id);
        kafkaProducer.sendUserEvent(new UserEvent(user.getEmail(), UserEvent.Operation.DELETE));
    }
}