package com.example.SpringAppGB.controllers.api;

import com.example.SpringAppGB.model.Roles;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.model.DTO.UserDTO;
import com.example.SpringAppGB.repository.interfaces.UsersProjectRepository;
import com.example.SpringAppGB.services.UserProjectService;
import com.example.SpringAppGB.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param id     идентификатор пользователя для обновления
     * @param user   объект пользователя с новыми данными
     * @param bindingResult результат валидации входных данных
     * @return ResponseEntity с информацией о местоположении после успешного обновления
     *         или список ошибок валидации в случае некорректных данных
     */
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable("id") long id,
                                      @Valid @RequestBody User user,
                                      BindingResult bindingResult) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
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
     * Метод обрабатывает POST-запрос на добавление нового пользователя в базу.
     *
     * @param userDTO объект пользователя, содержащий данные для добавления
     * @param bindingResult результат валидации входных данных
     * @return ResponseEntity с сообщением об ошибках валидации, если они есть,
     *         или статусом обработки запроса и добавленным пользователем
     */
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
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

