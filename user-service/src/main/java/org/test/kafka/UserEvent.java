package org.test.kafka;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserEvent implements Serializable {
    private String email;
    private Operation operation;
    private LocalDateTime timestamp;

    public UserEvent() {}

    public UserEvent(String email, Operation operation) {
        this.email = email;
        this.operation = operation;
        this.timestamp = LocalDateTime.now();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public enum Operation { CREATE, DELETE }
}