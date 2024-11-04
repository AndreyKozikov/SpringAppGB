package com.example.SpringAppGB.repository.interfaces;

import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import com.example.SpringAppGB.model.UsersProject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для управления связями между пользователями и проектами.
 * Предоставляет методы для выполнения операций с сущностью UsersProject,
 * а также специфические запросы для работы со связями пользователей и проектов.
 */
public interface UsersProjectRepository extends JpaRepository<UsersProject, Long> {

    /**
     * Находит всех пользователей, связанных с конкретным проектом.
     *
     * @param projectId ID проекта
     * @return список пользователей, связанных с данным проектом
     */
    @Query("SELECT up.user FROM UsersProject up WHERE up.project.id = :projectId")
    List<User> findUsersByProjectId(@Param("projectId") Long projectId);

    /**
     * Находит все проекты, связанные с конкретным пользователем.
     *
     * @param userId ID пользователя
     * @return список проектов, связанных с данным пользователем
     */
    @Query("SELECT up.project FROM UsersProject up WHERE up.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    /**
     * Находит всех пользователей в таблице Users, которые отсутствуют в списке ID пользователей.
     *
     * @param userIds список ID пользователей для исключения
     * @return список пользователей, не связанных с указанными ID
     */
    @Query("SELECT u FROM User u WHERE u.id NOT IN :userIds")
    List<User> findUsersNotInProject(@Param("userIds") List<Long> userIds);

    /**
     * Добавляет пользователя в проект.
     *
     * @param relatedEntityId ID связанной сущности
     * @param projectId       ID проекта
     * @param userId          ID пользователя
     * @return количество затронутых строк (должно быть 1 при успешном выполнении)
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_project (related_entity_id, user_id, project_id) " +
            "VALUES (:relatedEntityId, :userId, :projectId)", nativeQuery = true)
    int addUsersToProject(@Param("relatedEntityId") Long relatedEntityId,
                          @Param("projectId") Long projectId,
                          @Param("userId") Long userId);

    /**
     * Удаляет пользователя из проекта.
     *
     * @param userId    ID пользователя для удаления
     * @param projectId ID проекта
     * @return количество затронутых строк (должно быть 1 при успешном выполнении)
     */
    @Modifying
    @Query("DELETE FROM UsersProject up WHERE (:userId IS NULL OR  up.user.id = :userId)" +
            " AND (:projectId IS NULL OR up.project.id = :projectId)")
    int removeUserAndProject(@Param("userId") Long userId,
                               @Param("projectId") Long projectId);

    /**
     * Проверяет существование записей в таблице по указанным пользователю и проекту.
     *
     * @param userId    ID пользователя
     * @param projectId ID проекта
     * @return true, если связь существует; иначе false
     */
    boolean existsByUserIdAndProjectId(Long userId, Long projectId);
}
