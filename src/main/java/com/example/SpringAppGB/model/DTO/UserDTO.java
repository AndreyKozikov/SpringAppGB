package com.example.SpringAppGB.model.DTO;

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
//    @NotBlank(message = "Имя пользователя не должно быть пустым")
//    @Size(max = 100, message = "Имя пользователя не должно превышать 100 символов")
    private String userName;

    /**
     * Пароль пользователя.
     * Не может быть пустым.
     */
//    @NotBlank(message = "Пароль не должен быть пустым")
//    @Size(max = 255, message = "Пароль не должен превышать 255 символов")
    private String password;

    /**
     * Электронная почта пользователя.
     * Должен быть корректный формат email.
     */
//    @Email(message = "Некорректный формат электронной почты")
    private String email;

    /**
     * Роль пользователя в системе.
     * Не может быть пустым.
     */
//    @NotBlank(message = "Роль не должна быть пустой")
//    @Size(max = 50, message = "Роль не должна превышать 50 символов")
    private String role;
}
