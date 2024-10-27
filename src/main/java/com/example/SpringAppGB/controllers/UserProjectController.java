package com.example.SpringAppGB.controllers;


import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.services.ProjectService;
import com.example.SpringAppGB.services.UserProjectService;
import com.example.SpringAppGB.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * Контроллер для управления связями между пользователями и проектами.
 * Обеспечивает функциональность для просмотра пользователей проекта и проектов пользователя,
 * добавления пользователей в проект и удаления пользователей из проекта.
 */
@Controller
public class UserProjectController {

    private final UserProjectService userProjectService;
    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public UserProjectController(UserProjectService userProjectService, UserService userService, ProjectService projectService) {
        this.userProjectService = userProjectService;
        this.userService = userService;
        this.projectService = projectService;
    }


    /**
     * Метод возвращает главную страницу
     *
     * @param model модель для передачи данных в представление
     * @return имя шаблона главной страницы
     */
    @GetMapping("/user")
    public String showMainPage(Model model) {
        return "mainpage";
    }

    /**
     * Метод возвращает пользователей по идентификатору проекта
     *
     * @param projectId идентификатор проекта
     * @param model     модель для передачи данных в представление
     * @return имя шаблона с пользователями проекта
     */
    @GetMapping("/project/{id}/users")
    public String getUsersByProjectId(@PathVariable("id") Long projectId, Model model) {
        List<User> users = userProjectService.getUsersByProjectId(projectId);
        if (users != null) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("users", users);
            return "project_users";
        }
        return "redirect:/user";
    }

    /**
     * Метод возвращает проекты по идентификатору пользователя
     *
     * @param userId идентификатор пользователя
     * @param model  модель для передачи данных в представление
     * @return имя шаблона с проектами пользователя
     */
    @GetMapping("/user/{id}/projects")
    public String getProjectsByUserId(@PathVariable("id") Long userId, Model model) {
        List<Project> projects = userProjectService.getProjectsByUserId(userId);
        if (projects != null) {
            model.addAttribute("userName", userService.getUserById(userId).getUserName());
            model.addAttribute("projects", projects);
            return "user_projects";
        }
        return "redirect:/user";
    }

    /**
     * Метод возвращает форму для добавления пользователей к проекту
     *
     * @param projectId идентификатор проекта
     * @param model     модель для передачи данных в представление
     * @return имя шаблона для действий с проектом
     */
    @GetMapping("/add_user_to_project/{id}")
    public String addUserToProject(@PathVariable("id") Long projectId, Model model) {
        List<User> users = userProjectService.getUsersNotInProject(projectId);
        if (users != null) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("projectName", projectService.findProjectById(projectId).getName());
            model.addAttribute("users", users);
            model.addAttribute("message", "Добавить выбранных пользователей");
            model.addAttribute("action", "add");
            return "project_actions";
        }
        return "redirect:/user";
    }

    /**
     * Метод обрабатывает добавление выбранных пользователей к проекту
     *
     * @param projectId идентификатор проекта
     * @param userIds   список идентификаторов пользователей
     * @param model     модель для передачи данных в представление
     * @return имя шаблона с пользователями проекта после добавления
     */
    @PostMapping("/add_selected_users")
    public String addUserToProject(@RequestParam(value = "projectId", defaultValue = "-1") Long projectId,
                                   @RequestParam(value = "selectedUsers", defaultValue = "")
                                   List<Long> userIds, Model model) {

        if (projectId == -1 || userIds == null || userIds.isEmpty()) {
            return "redirect:/user"; // Перенаправление на главную страницу, если проект не найден
        }
        if (!userProjectService.addUserToProject(projectId, userIds)) {
            return "redirect:/user";
        }
        List<User> users = userProjectService.getUsersByProjectId(projectId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("users", users);
        return "project_users";
    }

    /**
     * Метод возвращает форму для удаления пользователей из проекта
     *
     * @param projectId идентификатор проекта
     * @param model     модель для передачи данных в представление
     * @return имя шаблона для действий с проектом
     */
    @GetMapping("/remove_users_from_project/{id}")
    public String removeUserFromProject(@PathVariable("id") Long projectId, Model model) {
        List<User> users = userProjectService.getUsersByProjectId(projectId);
        if (users != null) {
            model.addAttribute("projectId", projectId);
            model.addAttribute("projectName", projectService.findProjectById(projectId).getName());
            model.addAttribute("users", users);
            model.addAttribute("message", "Удалить выбранных пользователей");
            model.addAttribute("action", "remove");
            return "project_actions";
        }
        return "redirect:/user";
    }

    /**
     * Метод обрабатывает удаление выбранных пользователей из проекта
     *
     * @param projectId идентификатор проекта
     * @param userIds   список идентификаторов пользователей
     * @param model     модель для передачи данных в представление
     * @return имя шаблона с пользователями проекта после удаления
     */
    @PostMapping("/remove_selected_users")
    public String removeUserFromProject(@RequestParam(value = "projectId", defaultValue = "-1") Long projectId,
                                        @RequestParam(value = "selectedUsers", defaultValue = "")
                                        List<Long> userIds, Model model) {

        if (projectId == -1 || userIds == null || userIds.isEmpty()) {
            return "redirect:/user"; // Перенаправление на главную страницу, если проект не найден
        }
        if (userProjectService.removeUserFromProject(projectId, userIds)) {
            List<User> users = userProjectService.getUsersByProjectId(projectId);
            model.addAttribute("projectId", projectId);
            model.addAttribute("users", users);
            return "project_users";
        }
        return "redirect:/user";
    }
}
