package com.example.SpringAppGB.Authorization.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.SpringAppGB.Authorization.interfaces.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWTVerifier;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс JwtTokenProvider предоставляет функциональность для работы с JWT (JSON Web Tokens) в контексте авторизации.
 * Он генерирует, валидирует и извлекает информацию из токенов, используя RSA ключи для подписания и верификации.
 */
@Component
public class JwtTokenProvider implements TokenProvider {

    private final RSAPrivateKey privateKey; // Секретный ключ для подписи
    private final RSAPublicKey publicKey;

    @Value("${jwt.lifetime}")
    private Duration JWT_EXPIRATION_MS; // Время жизни токена

    /**
     * Конструктор для инициализации JwtTokenProvider с использованием ключей, предоставляемых JwtKeyProvider.
     *
     * @param jwtKeyProvider Поставщик RSA ключей.
     * @throws NoSuchAlgorithmException Если возникает ошибка при создании алгоритма RSA.
     * @throws InvalidKeySpecException Если возникает ошибка при создании ключа.
     * @throws IOException Если возникает ошибка при чтении ключей.
     */
    @Autowired
    public JwtTokenProvider(JwtKeyProvider jwtKeyProvider) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        try {
            this.privateKey = jwtKeyProvider.getPrivateKey();
            this.publicKey = jwtKeyProvider.getPublicKey();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException("Failed to initialize JwtTokenProvider: " + e.getMessage(), e);
        }
    }

    /**
     * Генерирует JWT токен для указанного пользователя.
     * Токен включает информацию о ролях пользователя и сроке действия.
     *
     * @param userDetails Детали пользователя, для которого генерируется токен.
     * @return Строка, представляющая собой сгенерированный JWT токен.
     */
    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities()
                .stream().
                map(GrantedAuthority::getAuthority).
                toList();
        claims.put("roles", rolesList);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + JWT_EXPIRATION_MS.toMillis());
        return JWT.create()
                .withPayload(claims)
                .withIssuer(userDetails.getUsername())
                .withIssuedAt(issuedDate)
                .withExpiresAt(expiredDate)
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    /**
     * Валидирует JWT токен, проверяя его подпись.
     *
     * @param token JWT токен для валидации.
     * @return true, если токен валиден.
     * @throws JWTVerificationException Если токен не прошел верификацию.
     */
    @Override
    public boolean validateToken(String token) throws JWTVerificationException {
        // Создаем алгоритм RSA256 с публичным ключом
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey); // Второй параметр (privateKey) оставляем null, так как для верификации нужен только публичный ключ
        // Создаем JWTVerifier с использованием алгоритма
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }

    /**
     * Извлекает имя пользователя из токена.
     * В данном случае имя пользователя хранится в поле "iss" токена.
     *
     * @param token JWT токен.
     * @return Имя пользователя.
     * @throws TokenExpiredException Если токен истек.
     * @throws SignatureVerificationException Если подпись токена недействительна.
     * @throws JWTVerificationException Если токен не прошел верификацию.
     */
    @Override
    public String getUsernameFromToken(String token) throws TokenExpiredException, SignatureVerificationException, JWTVerificationException {
        String username = getAllClaimsFromToken(token).get("iss").toString();
        System.out.println(username);
        return username;
    }

    /**
     * Извлекает список ролей пользователя из токена.
     *
     * @param token JWT токен.
     * @return Список ролей.
     */
    @Override
    public List<String> getRolesFromToken(String token) {
        return (List<String>) getAllClaimsFromToken(token).get("roles");
    }

    /**
     * Извлекает все утверждения (claims) из токена.
     *
     * @param token JWT токен.
     * @return Карта утверждений из токена.
     * @throws TokenExpiredException Если токен истек.
     * @throws SignatureVerificationException Если подпись токена недействительна.
     * @throws JWTVerificationException Если токен не прошел верификацию.
     */
    private Map<String, Object> getAllClaimsFromToken(String token) throws TokenExpiredException, SignatureVerificationException, JWTVerificationException {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Object> claims = new HashMap<>();
        return jwt
                .getClaims()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().as(Object.class)));
    }

    /**
     * Извлекает JWT токен из cookies запроса.
     *
     * @param request HTTP запрос, содержащий cookies.
     * @return Строка с JWT токеном или null, если токен не найден.
     */
    public String getJwtFromCookies(HttpServletRequest request) {
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