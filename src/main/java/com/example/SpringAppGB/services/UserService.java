package com.example.SpringAppGB.services;

import com.example.SpringAppGB.Authorization.services.JwtTokenProvider;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.repository.interfaces.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Добавлен PasswordEncoder
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Получает пользователя по его username
     *
     * @param username
     * @return бъект пользователя или null, если пользователь не найден
     */
    Optional<User> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    /**
     * Добавляет нового пользователя в систему.
     *
     * @param userAdd объект пользователя для добавления
     */
    @Transactional
    public void addUser(User userAdd) {
        User user = new User();
        user.setUserName(userAdd.getUserName());
        user.setEmail(userAdd.getEmail());
        user.setPassword(passwordEncoder.encode(userAdd.getPassword()));
        user.setRole(userAdd.getRole());
        userRepository.save(user);
    }

    /**
     * Получает список всех пользователей в системе.
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект пользователя или null, если пользователь не найден
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> findUserByUserNameOrByEmail(String findString){
        List<User> users = userRepository.findUserByUserNameOrEmail(findString, findString);
        return users != null ? users : Collections.emptyList();
    }
    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * Этот метод извлекает пользователя по идентификатору,
     * обновляет его свойства и сохраняет изменения в репозитории.
     *
     * @param userId идентификатор пользователя, который необходимо обновить.
     * @param user объект пользователя, содержащий обновленные данные.
     */
    @Transactional
    public void updateUser(Long userId, User user) {
        User userToBeUpdated = getUserById(userId);
        userToBeUpdated.setUserName(user.getUserName());
        userToBeUpdated.setEmail(user.getEmail());
        userRepository.updateUserById(userId, user);
        userRepository.flush();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * Этот метод удаляет пользователя из репозитория по указанному идентификатору.
     *
     * @param userId идентификатор пользователя, которого необходимо удалить.
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Метод загружает пользователя по имени пользователя для аутентификации в Spring Security.
     *
     * @param username имя пользователя, которое нужно найти
     * @return UserDetails объект с данными пользователя для аутентификации
     * @throws UsernameNotFoundException если пользователь с таким именем не найден
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException(""));
        // Преобразование единственной роли в список SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities);
    }

    /**
     * Извлекает пользователя из токена JWT, находящегося в cookies запроса.
     *
     * @param request объект HttpServletRequest, содержащий cookies с JWT токеном.
     * @return объект User, соответствующий имени пользователя, извлечённому из токена,
     *         или null, если пользователь не найден.
     */
    public User getUserFromToken(HttpServletRequest request) {
        String token = jwtTokenProvider.getJwtFromCookies(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return userRepository.findByUserName(username).orElse(null);
    }
}
