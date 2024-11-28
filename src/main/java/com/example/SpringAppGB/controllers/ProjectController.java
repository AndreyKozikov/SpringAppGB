package com.example.SpringAppGB.controllers;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

/**
 * Контроллер для управления проектами.
 * Обеспечивает функциональность для создания новых проектов,
 * отображения формы добавления проекта и просмотра списка всех проектов.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Обрабатывает GET-запрос на отображение операций над проектами".
     *
     * @return имя представления для страницы управления проектами
     */
    @GetMapping("/managment")
    public String userManagment() {
        return "/projects/project_managment";
    }

    /**
     * Метод обрабатывает GET-запрос и возвращает страницу для добавления нового проекта
     * @param project объект проекта
     * @return имя шаблона для отображения
     */
    @GetMapping("/add")
    public String showProjectForm(@ModelAttribute("project") Project project) {
        return "/projects/add_project";
    }

    /**
     * Метод обрабатывает POST-запрос на добавление нового проекта в базу и
     * осуществляет перенаправление на HTML страницу.
     *
     * @param project объект проекта, содержащий данные для добавления
     * @param bindingResult результат валидации входных данных
     * @return имя шаблона для отображения или перенаправление на страницу управления проектами
     */
    @PostMapping("/add")
    public String addProject(@Valid @ModelAttribute("project") Project project,
                             BindingResult bindingResult) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            return "/projects/add_project";
        }
        projectService.addProject(project);

        return "/projects/project_managment";
    }

    /**
     * Обрабатывает GET-запрос и отображает форму редактирования проекта.
     *
     * @param id идентификатор проекта для редактирования.
     * @param model модель для передачи данных на страницу.
     * @return имя представления для редактирования проекта.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Project projectToEdit = projectService.findProjectById(id);
        System.out.println(projectToEdit.getCreatedDate());
        // Установим отформатированное значение даты, если необходимо
        // Форматируем дату в строку для отображения
        String formattedDate = projectToEdit.getCreatedDate() != null
                ? projectToEdit.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "";
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("project", projectToEdit);
        return "/projects/edit_project";
    }

}
