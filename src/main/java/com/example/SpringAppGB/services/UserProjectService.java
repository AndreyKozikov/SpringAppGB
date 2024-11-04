package com.example.SpringAppGB.services;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.repository.interfaces.UsersProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления связи между пользователями и проектами.
 * Предоставляет методы для работы с пользователями в системе.
 */
@Service
@AllArgsConstructor
public class UserProjectService {

    private final UsersProjectRepository usersProjectRepository;
    private final UserService userService;
    private final ProjectService projectService;

    /**
     * Метод, возвращающий список пользователей, связанных с определенным проектом
     *
     * @param projectId идентификатор проекта
     * @return список пользователей
     */
    public List<User> getUsersByProjectId(Long projectId) {
        return projectValidation(projectId) ? usersProjectRepository.findUsersByProjectId(projectId) :
                null;
    }

    /**
     * Метод, возвращающий список проектов, связанных с определенным пользователем
     *
     * @param userId идентификатор пользователя
     * @return список проектов
     */
    public List<Project> getProjectsByUserId(Long userId) {
        return userValidation(userId) ? usersProjectRepository.findProjectsByUserId(userId) : null;
    }

    /**
     * Метод, возвращающий список пользователей, не входящих в проект
     *
     * @param projectId идентификатор проекта
     * @return список пользователей, не входящих в проект
     */
    public List<User> getUsersNotInProject(Long projectId) {
        if (!projectValidation(projectId)){
            return null;
        }
        List<Long> userIds = this.getUsersByProjectId(projectId)
                .stream().map(User::getId)
                .collect(Collectors.toList());
        return usersProjectRepository.findUsersNotInProject(userIds);
    }

    /**
     * Метод, добавляющий пользователя к проекту
     *
     * @param projectId идентификатор проекта
     * @param userIds   список идентификаторов пользователей
     * @return true, если хотя бы один пользователь был добавлен, иначе false
     */
    @Transactional
    public boolean addUserToProject(Long projectId, List<Long> userIds) {
        if (!projectValidation(projectId)) {
            return false;
        }
        Long relatedEntityId = projectId;
        int a = 0;
        for (Long userId : userIds) {
            if ((userService.getUserById(userId) != null) &&
                    (!usersProjectRepository.existsByUserIdAndProjectId(userId, projectId))) {
                a += usersProjectRepository.addUsersToProject(relatedEntityId, projectId, userId);
            }
        }
        return a > 0;
    }

    /**
     * Метод, удаляющий пользователя из проекта
     *
     * @param projectId идентификатор проекта
     * @param userIds   список идентификаторов пользователей
     * @return true, если хотя бы один пользователь был удален, иначе false
     */
    @Transactional
    public boolean removeUserFromProject(Long projectId, List<Long> userIds) {
        if (!projectValidation(projectId)) {
            return false;
        }
        int a = 0;
        for (Long userId : userIds) {
            if (userService.getUserById(userId) != null) {
                a += usersProjectRepository.removeUserAndProject(userId, projectId);
            }
        }
        return a>0;
    }

    /**
     * Удаляет связь между пользователем и проектом по их идентификаторам.
     *
     * Этот метод выполняет валидацию проекта и, если проект валиден,
     * удаляет запись о связи между пользователем и проектом.
     *
     * @param projectId идентификатор проекта, который необходимо удалить.
     * @param userId идентификатор пользователя, для которого необходимо удалить связь с проектом.
     * @return {@code true}, если связь была успешно удалена, {@code false} в противном случае.
     */
    @Transactional
    public boolean removeProject(Long projectId, Long userId) {
        if (!projectValidation(projectId)) {
            return false;
        }
        int a = 0;
        a += usersProjectRepository.removeUserAndProject(userId, projectId);
        return a>0;
    }

    /**
     * Метод проверяет, существует ли проект с заданным идентификатором.
     *
     * @param projectId идентификатор проекта
     * @return true, если проект существует; false в противном случае
     */
    private boolean projectValidation(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        return project != null;
    }

    /**
     * Метод проверяет, существует ли пользователь с заданным идентификатором.
     *
     * @param userId идентификатор пользователя
     * @return true, если пользователь существует; false в противном случае
     */
    private boolean userValidation(Long userId) {
        User user = userService.getUserById(userId);
        return user != null;
    }
}
