package com.example.SpringAppGB.controllers;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Контроллер для управления проектами.
 * Обеспечивает функциональность для создания новых проектов,
 * отображения формы добавления проекта и просмотра списка всех проектов.
 */
@Controller
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Метод возвращает страницу для добавления нового проекта
     * @param project объект проекта
     * @return имя шаблона для отображения
     */
    @GetMapping("/add_project")
    public String showProjectForm(@ModelAttribute("project") Project project) {
        return "addproject";
    }

    /**
     * Метод обрабатывает POST запрос на добавление нового проекта в базу
     * @param project объект проекта
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения
     */
    @PostMapping("/add_project")
    public String addProject(@ModelAttribute("project") Project project, Model model){
        if (isProjectInvalid(project)){
            return "addproject";
        }
        projectService.addProject(project);
        model.addAttribute("projects", projectService.getAllProjects());
        return "showProjects";
    }

    /**
     * Метод возвращает страницу со всеми проектами из базы
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения
     */
    @GetMapping("/list_projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "showprojects";
    }

    private boolean isProjectInvalid(Project project) {
        return project == null ||
                project.getName() == null || project.getName().isEmpty() ||
                project.getDescription() == null || project.getDescription().isEmpty() ||
                project.getCreatedDate() == null;
    }
}
