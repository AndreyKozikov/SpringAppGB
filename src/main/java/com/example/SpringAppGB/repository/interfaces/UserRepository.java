package com.example.SpringAppGB.repository.interfaces;

import com.example.SpringAppGB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для выполнения CRUD операций с пользователями,
 * а также дополнительные методы запросов, унаследованные от JpaRepository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
