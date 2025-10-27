package org.test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndGetUser() {
        UserDto dto = new UserDto();
        dto.setName("Charlie");
        dto.setEmail("charlie@test.com");
        dto.setAge(28);

        ResponseEntity<UserDto> postResponse = restTemplate.postForEntity("/users", dto, UserDto.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postResponse.getBody()).isNotNull();
        Long id = postResponse.getBody().getId();

        ResponseEntity<UserDto> getResponse = restTemplate.getForEntity("/users/" + id, UserDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo("Charlie");
    }
}
