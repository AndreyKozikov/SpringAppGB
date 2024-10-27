package com.example.SpringAppGB.services;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.repository.interfaces.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления проектами.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

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
}
