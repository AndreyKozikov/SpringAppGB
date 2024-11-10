package com.example.SpringAppGB.controllers;

import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для управления пользователями.
 * Обеспечивает функциональность для создания новых пользователей,
 * отображения формы добавления пользователя и просмотра списка всех пользователей.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Метод обрабатывает GET-запрос на отображение страницы управления пользователями
     *
     * @return возвращает ссылку на HTML страницу
     */
    @GetMapping("/managment")
    public String userManagment() {
        return "/users/users_managment";
    }

    /**
     * Метод обрабатывает GET-запрос и возвращает форму добавления пользователя
     *
     * @param user объект пользователя
     * @return имя шаблона для отображения
     */
    @GetMapping("/add")
    public String showUserForm(@ModelAttribute("user") User user) {
        //@ModelAttribute создает пустого пользователя и передает в шаблон для связывания параметров шаблона и класса
        return "/users/add_user";
    }

    /**
     * Метод обрабатывает POST-запрос на добавление нового пользователя в базу
     *
     * @param user  объект пользователя
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения
     */
    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        if (isUserInvalid(user)) {
            return "/users/add_user";
        }
        userService.addUser(user);
        //model.addAttribute("users", userService.getAllUsers());
        return "/users/users_managment";
    }

    /**
     * Метод обрабатывает GET-запрос на отображение формы редактирования пользователя.
     *
     * @param id    идентификатор пользователя для редактирования.
     * @param model модель для передачи данных на страницу.
     * @return имя представления для редактирования пользователя.
     */
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/users/user_edit";
    }

    // Метод для проверки валидности пользователя
    private boolean isUserInvalid(User user) {
        return user == null ||
                user.getUserName() == null || user.getUserName().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getRole() == null;
    }
}
