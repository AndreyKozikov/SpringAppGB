package com.example.SpringAppGB.services;

import com.example.SpringAppGB.Authorization.services.JwtTokenProvider;
import com.example.SpringAppGB.Exceptions.AppErrors;
import com.example.SpringAppGB.model.DTO.JwtRequest;
import com.example.SpringAppGB.services.Interfaces.AuthServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Сервис для аутентификации пользователей и генерации JWT токена.
 * Выполняет проверку учетных данных, генерацию токена и настройку cookie с токеном.
 */
@Service
@AllArgsConstructor
public class AuthService implements AuthServiceInterface {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Создает JWT токен для пользователя после успешной аутентификации.
     *
     * @param user     Объект {@link JwtRequest}, содержащий данные пользователя (имя и пароль).
     * @param response {@link HttpServletResponse} для добавления cookie с JWT токеном.
     * @return {@link ResponseEntity} с ответом, содержащим HTTP статус и перенаправлением на страницу в зависимости от роли пользователя.
     */

    @Override
    public ResponseEntity<?> createAuthToken(JwtRequest user, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),
                    user.getPassword()));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppErrors(HttpStatus.UNAUTHORIZED.value(),
                    "Некорректные данные пользователя"),
                    HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(user.getUserName());
        String jwtToken = jwtTokenProvider.generateJwtToken(userDetails);

        // Установка токена в HttpOnly cookie
        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true); // Запрещаем доступ к cookie через JavaScript
        cookie.setSecure(false); // Убедитесь, что cookie передается только через HTTPS при параметре True
        cookie.setPath("/"); // Устанавливаем путь доступности cookie
        cookie.setMaxAge(60 * 60 * 24); // Устанавливаем время жизни cookie (1 день)

        response.addCookie(cookie);

        // Проверка роли пользователя
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            // Если роль "ADMIN", перенаправляем на страницу для админов
            return ResponseEntity.ok().header("Location", "/main").build();
        } else {
            // Если роль обычного пользователя, перенаправляем на страницу для пользователей
            return ResponseEntity.ok().header("Location", "/user/profile").build();
        }

    }


}
