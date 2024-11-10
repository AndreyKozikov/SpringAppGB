package com.example.SpringAppGB.controllers.api;


import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.services.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Обрабатывает Patch-запрос на обновление проекта по идентификатору.
     *
     * @param id идентификатор проекта для обновления.
     * @param project объект проекта с новыми данными.
     * @return ответ с местоположением после успешного обновления.
     */
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> editProjectById(@PathVariable("id") Long id, @RequestBody Project project) {
        System.out.println(project);
       // projectService.updateProjectById(id, project);
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
     * @param project объект проекта
     * @return статус выполнения операции
     */
    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project){
        projectService.addProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }
}
