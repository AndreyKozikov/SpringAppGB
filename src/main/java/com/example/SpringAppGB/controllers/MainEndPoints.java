package com.example.SpringAppGB.controllers;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.services.UserProjectService;
import com.example.SpringAppGB.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainEndPoints {

    private final UserService userService;
    private final UserProjectService userProjectService;


    /**
     * Эндпоинт для отображения страницы входа.
     *
     * @return Название представления страницы входа.
     */
    @GetMapping("/auth/login")
    public String login() {
        return "/auth/login";
    }

    /**
     * Эндпоинт для отображения профиля пользователя с проектами.
     *
     * @param model Модель для передачи данных в представление.
     * @param request HTTP запрос для извлечения данных пользователя из токена.
     * @return Название представления страницы профиля пользователя.
     */
    @GetMapping("/user/profile")
    public String userProfile(Model model, HttpServletRequest request) {
        User user = userService.getUserFromToken(request);
        model.addAttribute("user", user);
        List<Project> projects = userProjectService.getProjectsByUserId(user.getId());
        model.addAttribute("projects", projects);
        return "/user/profile";
    }
}
