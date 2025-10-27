package org.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.dto.UserDto;
import org.test.mapper.UserMapper;
import org.test.model.User;
import org.test.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        User created = userService.createUser(dto.getName(), dto.getEmail(), dto.getAge());
        return ResponseEntity.ok(UserMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto dto) {
        User existing = userService.findUserById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (dto.getName() != null && !dto.getName().isBlank()) existing.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) existing.setEmail(dto.getEmail());
        if (dto.getAge() != null) existing.setAge(dto.getAge());
        existing.setCreatedAt(existing.getCreatedAt() == null ? LocalDateTime.now() : existing.getCreatedAt());
        User updated = userService.updateUser(existing);
        return ResponseEntity.ok(UserMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}