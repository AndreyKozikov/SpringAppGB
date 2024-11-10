package com.example.SpringAppGB.Exceptions;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Обработчик глобальных исключений для обработки различных ошибок в приложении.
 * Включает обработку ошибок, связанных с токенами, а также ошибок аутентификации.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обработка ошибки истекшего токена. Возвращает ответ с редиректом на страницу авторизации.
     *
     * @param ex Исключение, возникающее при истечении срока действия токена.
     * @return Ответ с редиректом на страницу /auth/login.
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
        log.debug("Время жизни токена истекло");
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/auth/login")
                .build();
    }

    /**
     * Обработка ошибки некорректной подписи токена. Возвращает ответ с редиректом на страницу авторизации.
     *
     * @param ex Исключение, возникающее при ошибке подписи токена.
     * @return Ответ с редиректом на страницу /auth/login.
     */
    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<String> handleSignatureVerificationException(SignatureVerificationException ex) {
        log.debug("Подпись некорректна");
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/auth/login")
                .build();
    }

    /**
     * Обработка всех ошибок валидации JWT токена. Возвращает ответ с редиректом на страницу авторизации.
     *
     * @param ex Исключение, возникающее при валидации JWT токена.
     * @return Ответ с редиректом на страницу /auth/login.
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<String> handleJWTVerificationException(JWTVerificationException ex) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/auth/login")
                .build();
    }

    /**
     * Обработка ошибки, когда пользователь не найден. Возвращает ответ с редиректом на страницу авторизации.
     *
     * @param ex Исключение, возникающее при попытке найти пользователя по имени.
     * @return Ответ с редиректом на страницу /auth/login.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/auth/login")
                .build();
    }


    /**
     * Обработка ошибки неверных учетных данных. Возвращает ответ с ошибкой в теле.
     *
     * @param ex Исключение, возникающее при неверных учетных данных.
     * @return Ответ с ошибкой и кодом состояния 431 (Request Header Fields Too Large).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
    }


//    /**
//     * Обработка других исключений. Возвращает ответ с сообщением об ошибке.
//     *
//     * @param ex Общее исключение.
//     * @return Ответ с ошибкой и кодом состояния 500 (Internal Server Error).
//     */
//    // Обработка других исключений
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralException(Exception ex) {
//        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }


}
