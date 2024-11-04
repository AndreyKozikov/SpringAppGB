package com.example.SpringAppGB.repository.interfaces;

import com.example.SpringAppGB.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Project.
 * Предоставляет методы для выполнения CRUD операций с проектами,
 * а также дополнительные методы запросов, унаследованные от JpaRepository.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p " +
            "WHERE LOWER (p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Project> searchProjectByNameOrDescription(@Param("name") String name , @Param("description") String description);

    /**
     * Обновляет данные проекта по его идентификатору.
     *
     * @param id идентификатор проекта, который необходимо обновить.
     * @param project объект проекта, содержащий обновленные данные.
     */
    @Modifying
    @Query("UPDATE Project p SET p.name = :#{#project.name}, " +
            "p.description = :#{#project.description}, " +
            "p.createdDate = :#{#project.createdDate} " +
            "WHERE p.id = :id")
    void updateProjectById(@Param("id") Long id, Project project);
}
