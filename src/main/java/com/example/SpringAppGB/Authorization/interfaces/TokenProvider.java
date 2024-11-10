package com.example.SpringAppGB.Authorization.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


/**
 * Интерфейс для работы с токенами, который включает методы для их валидации, извлечения информации
 * о пользователе и ролях, а также для генерации JWT токенов.
 */
public interface TokenProvider {

    /**
     * Проверяет валидность токена.
     *
     * @param token JWT токен для проверки
     * @return {@code true}, если токен валиден, иначе {@code false}
     */
    boolean validateToken(String token);

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT токен, из которого нужно извлечь имя пользователя
     * @return Имя пользователя, содержащееся в токене
     */
    String getUsernameFromToken(String token);

    /**
     * Извлекает список ролей из токена.
     *
     * @param token JWT токен, из которого нужно извлечь роли
     * @return Список ролей пользователя, содержащихся в токене
     */
    List<String> getRolesFromToken(String token);

    /**
     * Генерирует токен для указанного пользователя.
     *
     * @param userDetails Детали пользователя, для которого генерируется токен
     * @return Сгенерированный JWT токен
     */
    String generateJwtToken(UserDetails userDetails);
}

