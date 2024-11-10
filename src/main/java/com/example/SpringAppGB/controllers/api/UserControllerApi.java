package com.example.SpringAppGB.controllers.api;

import com.example.SpringAppGB.model.Roles;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.model.DTO.UserDTO;
import com.example.SpringAppGB.repository.interfaces.UsersProjectRepository;
import com.example.SpringAppGB.services.UserProjectService;
import com.example.SpringAppGB.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Контроллер для управления пользователями через API.
 * Предоставляет RESTful интерфейс для операций над пользователями.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor

public class UserControllerApi {
    private final UserService userService;
    private final UserProjectService userProjectService;
    private final UsersProjectRepository usersProjectRepository;

    /**
     * Метод обрабатывает GET-запрос на получение списока всех пользователей.
     *
     * @return список всех пользователей в виде ответа HTTP.
     */
    @GetMapping("/get_all")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Метод обрабатывает GET-запрос на поиск пользователей по имени или адресу электронной почты.
     *
     * @param query текст для поиска в именах пользователей или адресах электронной почты.
     * @return список пользователей, соответствующих запросу.
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("query") String query) {
        List<User> users = userService.findUserByUserNameOrByEmail(query);
        return ResponseEntity.ok(users);
    }

    /**
     * Метод обрабатывает PATCH-запрос на обновление информации о пользователе по идентификатору.
     *
     * @param user объект пользователя с новыми данными.
     * @param id   идентификатор пользователя для обновления.
     * @return ответ с местоположением после успешного обновления.
     */
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@RequestBody User user, @PathVariable("id") long id) {
        userService.updateUser(id, user);
        Map<String, Object> response = new HashMap<>();
        response.put("location", "/users/managment");
        return ResponseEntity.ok(response);
    }

    /**
     * Метод обрабатывает DELETE-запрос на удаление пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя для удаления.
     * @return ответ с местоположением после успешного удаления.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("location", "/users/managment");
        return ResponseEntity.ok(response);
    }

    /**
     * Метод обрабатывает POST запрос на добавление нового пользователя в базу
     *
     * @param userDTO  объект пользователя
     * @return статус обработки запроса сервером
     */
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        // Преобразуем строковое значение роли в значение enum Roles
        try {
            Roles role = Roles.valueOf(userDTO.getRole()); // Преобразуем строку в enum
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            // Обработка некорректного значения роли
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

