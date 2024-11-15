package com.example.SpringAppGB.Authorization.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;

import com.example.SpringAppGB.Authorization.services.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Фильтр для аутентификации пользователей с использованием JWT токенов.
 * Этот фильтр извлекает JWT токен из заголовков запроса или из cookies, проверяет его валидность,
 * и если токен действителен, устанавливает аутентификацию в контексте безопасности.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Метод, выполняющий фильтрацию запросов, проверяет JWT токен в запросе и устанавливает аутентификацию.
     *
     * @param request HTTP запрос
     * @param response HTTP ответ
     * @param filterChain цепочка фильтров для дальнейшей обработки запроса
     * @throws ServletException если возникает ошибка в процессе обработки запроса
     * @throws IOException если ошибка ввода/вывода при обработке запроса
     * @throws JWTVerificationException если токен недействителен
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, JWTVerificationException {
        // Игнорируем проверку для маршрута логина
        String path = request.getServletPath();
        if ("/login".equals(path) || "/auth/login".equals(path) || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String token = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            token = getJwtFromCookies(request);
        } else {
            if (authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
                token = authHeader.substring(7);
            } else {
                throw new BadCredentialsException("");
            }
        }

        if (token == null) {
            throw new BadCredentialsException("Неверный токен");
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new JWTVerificationException("Invalid or expired token");
        }

        username = jwtTokenProvider.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenProvider
                            .getRolesFromToken(token)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            // Устанавливаем аутентификацию в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            // Если username == null или уже есть аутентификация, можно залогировать ошибку или вернуть ошибку 401
            if (username == null) {
                log.warn("Username is null, invalid or expired token.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return; // Прерываем выполнение фильтров
            }
            // Если аутентификация уже установлена
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("Authentication already set, skipping token processing.");
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Извлекает JWT токен из cookies запроса.
     *
     * @param request HTTP запрос
     * @return строка, содержащая JWT токен, или null, если токен не найден
     */
    private String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // Получаем все cookies из запроса
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) { // Ищем cookie с именем "jwt"
                    return cookie.getValue(); // Возвращаем значение токена
                }
            }
        }
        return null; // Возвращаем null, если токен не найден
    }


}


