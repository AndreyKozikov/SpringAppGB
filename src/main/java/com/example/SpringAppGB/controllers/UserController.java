package com.example.SpringAppGB.controllers;

import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * Контроллер для управления пользователями.
 * Обеспечивает функциональность для создания новых пользователей,
 * отображения формы добавления пользователя и просмотра списка всех пользователей.
 */
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод возвращает форму добавления пользователя
     *
     * @param user объект пользователя
     * @return имя шаблона для отображения
     */
    @GetMapping("/add_user")
    public String showUserForm(@ModelAttribute("user") User user) {
        //@ModelAttribute создает пустого пользователя и передает в шаблон для связывания параметров шаблона и класса
        return "adduser";
    }

    /**
     * Метод обрабатывает POST запрос на добавление нового пользователя в базу
     *
     * @param user  объект пользователя
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения
     */
    @PostMapping("/add_user")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        if (isUserInvalid(user)) {
           return "adduser";
        }
        userService.addUser(user);
        model.addAttribute("users", userService.getAllUsers());
        return "showusers";
    }

    /**
     * Метод возвращает страницу со всеми пользователями из базы
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения
     */
    @GetMapping("/list_users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "showusers";
    }

    // Метод для проверки валидности пользователя
    private boolean isUserInvalid(User user) {
        return user == null ||
                user.getUserName() == null || user.getUserName().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getRole() == null || user.getRole().isEmpty();
    }
}
