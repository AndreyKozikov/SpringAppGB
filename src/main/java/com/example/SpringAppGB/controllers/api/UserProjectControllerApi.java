package com.example.SpringAppGB.controllers.api;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.model.UserProjectRequest;
import com.example.SpringAppGB.services.ProjectService;
import com.example.SpringAppGB.services.UserProjectService;
import com.example.SpringAppGB.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления связями между пользователями и проектами.
 * Предоставляет API-эндпоинты для добавления и удаления пользователей из проектов,
 * а также для получения информации о пользователях, связанных или не связанных с проектами.
 */
@RestController
@RequestMapping("/api")
public class UserProjectControllerApi {

    private final UserProjectService userProjectService;
    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public UserProjectControllerApi(UserProjectService userProjectService, UserService userService, ProjectService projectService) {
        this.userProjectService = userProjectService;
        this.userService = userService;
        this.projectService = projectService;
    }


    /**
     * Метод, обрабатывающий GET-запрос для получения списка пользователей, связанных с определенным проектом
     * @param projectId идентификатор проекта
     * @return ResponseEntity со списком пользователей или статус 404, если проект не найден
     */
    @GetMapping("/users_in_project")
    public ResponseEntity<List<User>> getUsersByProjectId(@RequestParam(value="projectId",
            defaultValue = "-1") Long projectId) {
        if (projectId == -1){
            return ResponseEntity.notFound().build();
        }
        List<User> users = userProjectService.getUsersByProjectId(projectId);
        if (users != null) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Метод, обрабатывающий GET-запрос для получения списка проектов, связанных с определенным пользователем
     *
     * @param userId идентификатор пользователя
     * @return ResponseEntity со списком проектов или статус 404, если пользователь не найден
     */
    @GetMapping("/user_projects")
    public ResponseEntity<List<Project>> getProjectsByUserId(@RequestParam(value = "userId",
            defaultValue = "-1") Long userId) {
        if (userId == -1){
            return ResponseEntity.notFound().build();
        }
        List<Project> projects = userProjectService.getProjectsByUserId(userId);
        if (projects != null) {
            return ResponseEntity.ok(projects);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Метод, обрабатывающий POST-запрос для добавления пользователя к проекту
     *
     * @param request объект запроса с идентификатором проекта и списком пользователей
     * @return ResponseEntity с результатом операции
     */
    @PostMapping("/add_users_to_project")
    public ResponseEntity<String> addUserToProject(@RequestBody(required = false) UserProjectRequest request) {
        // Проверка на корректность входных данных
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid input data: request not be null or empty.");
        }
        if (request.getProjectId() == null || request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid input data: projectId and userIds must not be null or empty.");
        }
        boolean success = userProjectService.addUserToProject(request.getProjectId(), request.getUserIds());
        return success
                ? ResponseEntity.ok("Users added successfully.")
                : ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Failed to add users: Duplicate entry or other conflict.");
    }

    /**
     * Метод, обрабатывающий POST-запрос для удаления пользователя из проекта
     *
     * @param request объект запроса с идентификатором проекта и списком пользователей
     * @return ResponseEntity с результатом операции
     */

    @PostMapping("/remove_users_from_project")
    public ResponseEntity<String> removeUserFromProject(@RequestBody(required = false) UserProjectRequest request) {
        // Проверка на корректность входных данных
        if (request == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid input data: request not be null or empty.");
        }
        if (request.getProjectId() == null || request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Invalid input data: projectId and userIds must not be null or empty.");
        }
        boolean success = userProjectService.removeUserFromProject(request.getProjectId(), request.getUserIds());
        return success
                ? ResponseEntity.ok("Users removed successfully.")
                : ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Failed to remove users: No such user or project or other conflict.");
    }

}
