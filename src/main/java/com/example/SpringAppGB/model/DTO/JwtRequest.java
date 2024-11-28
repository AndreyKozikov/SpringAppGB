package com.example.SpringAppGB.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для передачи данных аутентификации пользователя.
 * Используется для передачи имени пользователя и пароля при создании JWT токена.
 */
@Data
@AllArgsConstructor
public class JwtRequest {

    @NotBlank(message = "The username cannot be empty")
    @Size(min = 3, max = 100, message = "The username must be between 3 and 100 characters long")
    private String userName;

    @NotBlank(message = "The password cannot be empty")
    @Size(min = 6, max = 255, message = "The password must contain from 6 to 255 characters")
    private String password;
}
