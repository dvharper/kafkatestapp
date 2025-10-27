
package org.test.mapper;

import org.test.dto.UserDto;
import org.test.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static User fromDto(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setCreatedAt(dto.getCreatedAt() == null ? java.time.LocalDateTime.now() : dto.getCreatedAt());
        return user;
    }
}
