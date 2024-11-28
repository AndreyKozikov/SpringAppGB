package com.example.SpringAppGB.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * DTO-класс для передачи данных при добавлении нового пользователя.
 */
@Data
@AllArgsConstructor
public class UserDTO {

    /**
     * Имя пользователя.
     * Должно быть уникальным и не может быть пустым.
     */
    @NotBlank(message = "The username cannot be empty")
    @Size(min = 3, max = 100, message = "The username must be between 3 and 100 characters long")

    private String userName;

    /**
     * Пароль пользователя.
     * Не может быть пустым.
     */
    @NotBlank(message = "The password cannot be empty")
    @Size(min = 6, max = 255, message = "The password must contain from 6 to 255 characters")
    private String password;

    /**
     * Электронная почта пользователя.
     * Должен быть корректный формат email.
     */
    @Email(message = "Incorrect email format")
    private String email;

    /**
     * Роль пользователя в системе.
     * Не может быть пустым.
     */

    private String role;
}
