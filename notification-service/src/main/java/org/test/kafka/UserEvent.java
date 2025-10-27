package org.test.kafka;

public class UserEvent {
    private String operation; // CREATE or DELETE
    private String email;
    private String name;

    public UserEvent() {}

    public UserEvent(String operation, String email, String name) {
        this.operation = operation;
        this.email = email;
        this.name = name;
    }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "UserEvent{" +
                "operation='" + operation + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
