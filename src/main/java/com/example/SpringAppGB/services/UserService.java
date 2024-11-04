package com.example.SpringAppGB.services;

import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.repository.interfaces.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Добавляет нового пользователя в систему.
     *
     * @param user объект пользователя для добавления
     */
    public void addUser(User user) {
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
}
