package com.example.SpringAppGB.services;

import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
}
