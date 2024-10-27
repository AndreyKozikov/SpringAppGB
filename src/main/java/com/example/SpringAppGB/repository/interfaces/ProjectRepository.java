package com.example.SpringAppGB.repository.interfaces;

import com.example.SpringAppGB.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью Project.
 * Предоставляет методы для выполнения CRUD операций с проектами,
 * а также дополнительные методы запросов, унаследованные от JpaRepository.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
