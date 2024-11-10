package com.example.SpringAppGB.controllers.api;

import com.example.SpringAppGB.model.DTO.JwtRequest;
import com.example.SpringAppGB.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для обработки запросов, связанных с авторизацией.
 * Предоставляет API для получения токена и входа в систему.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerApi {
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Эндпоинт для получения JWT токена на основе данных пользователя.
     *
     * @param userDTO DTO с данными пользователя, необходимыми для получения токена.
     * @param response Ответ для добавления токена в cookie.
     * @return Ответ с JWT токеном в случае успешной авторизации.
     */
    @PostMapping("/get_token")
    public ResponseEntity<?> createAuthToken (@RequestBody JwtRequest userDTO, HttpServletResponse response) {
        return authService.createAuthToken(userDTO, response);
    }

    /**
     * Эндпоинт для входа пользователя в систему с последующей генерацией токена.
     *
     * @param userDTO DTO с данными пользователя, необходимыми для входа.
     * @param response Ответ для добавления токена в cookie.
     * @return Ответ с JWT токеном в случае успешной авторизации.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody JwtRequest userDTO, HttpServletResponse response) {
        return authService.createAuthToken(userDTO, response);
    }
}


