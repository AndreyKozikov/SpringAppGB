package com.example.SpringAppGB.controllers.api;


import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.services.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для управления проектами через API.
 * Предоставляет RESTful интерфейс для операций над проектами.
 */
@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectControllerApi {

    private final ProjectService projectService;


    /**
     * Метод обрабатывает GET-запрос на получение всех проектов.
     *
     * @return список всех проектов в виде ответа HTTP.
     */
    @GetMapping("/get_all")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Обрабатывает GET-запрос на поиск проектов по имени или описанию.
     *
     * @param searchText текст для поиска в именах или описаниях проектов.
     * @return список проектов, соответствующих запросу.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProjects(@RequestParam("query") String searchText) {
        List<Project> projects = projectService.findProjectByNameOrDescription(searchText);
        return ResponseEntity.ok(projects);
    }

    /**
     * Обрабатывает PATCH-запрос на обновление проекта по идентификатору.
     *
     * @param id идентификатор проекта для обновления
     * @param project объект проекта с новыми данными
     * @param bindingResult результат валидации входных данных
     * @return ResponseEntity с информацией о местоположении после успешного обновления
     *         или список ошибок валидации в случае некорректных данных
     */
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editProjectById(@PathVariable("id") Long id,
                                             @Valid @RequestBody Project project,
                                             BindingResult bindingResult) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("location", "/projects/managment");
        return ResponseEntity.ok(response);
    }

    /**
     * Метод обрабатывает DELETE-запрос на удаление проекта по идентификатору.
     *
     * @param id идентификатор проекта для удаления.
     * @return пустой ответ HTTP 200 (OK) после успешного удаления.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable("id") Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод обрабатывает POST-запрос на добавление нового проекта в базу
     * @param project объект проекта для добавления
     * @param bindingResult результат валидации
     * @return ResponseEntity с добавленным проектом или списком ошибок валидации
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProject(@Valid @RequestBody Project project, BindingResult bindingResult) {
        // Проверка ошибок валидации
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        projectService.addProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }
}
