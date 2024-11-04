package com.example.SpringAppGB.services;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.repository.interfaces.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для управления проектами.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Метод для добавления нового проекта
     * @param project проект для добавления
     */
    public void addProject(Project project){
        projectRepository.save(project);
    }

    /**
     * Метод для получения всех проектов
     * @return список всех проектов
     */
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    /**
     * Метод для поиска проекта по идентификатору
     * @param projectId идентификатор проекта
     * @return проект или выбрасывает исключение, если не найден
     */
    public Project findProjectById(Long projectId){
        return projectRepository.findById(projectId).orElse(null);
    }

    public List<Project> findProjectByNameOrDescription(String searchString){
        return projectRepository.searchProjectByNameOrDescription(searchString, searchString);
    }

    /**
     * Обновляет данные проекта по его идентификатору.
     *
     * Этот метод извлекает проект по идентификатору,
     * обновляет его свойства и сохраняет изменения в репозитории.
     *
     * @param projectId идентификатор проекта, который необходимо обновить.
     * @param project объект проекта, содержащий обновленные данные.
     */
    @Transactional
    public void updateProjectById(Long projectId, Project project){
        Project projectUpdate = findProjectById(projectId);
        projectUpdate.setName(project.getName());
        projectUpdate.setDescription(project.getDescription());
        projectUpdate.setCreatedDate(project.getCreatedDate());
        projectRepository.updateProjectById(projectId, projectUpdate);
        projectRepository.flush();
    }

    @Transactional
    public void deleteProjectById(Long projectId){
        projectRepository.deleteById(projectId);
    }
}
