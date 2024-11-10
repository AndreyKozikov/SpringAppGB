package com.example.SpringAppGB.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для передачи данных аутентификации пользователя.
 * Используется для передачи имени пользователя и пароля при создании JWT токена.
 */
@Data
@AllArgsConstructor
public class JwtRequest {
    private String userName;
    private String password;
}
