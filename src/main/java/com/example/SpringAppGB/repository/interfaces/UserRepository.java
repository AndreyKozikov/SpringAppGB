package com.example.SpringAppGB.repository.interfaces;

import com.example.SpringAppGB.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для выполнения CRUD операций с пользователями,
 * а также дополнительные методы запросов, унаследованные от JpaRepository.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<User> findUserByUserNameOrEmail(@Param("username") String userName, @Param("email") String email);

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * Данный метод также удаляет кэшированные данные пользователей,
     * чтобы обеспечить актуальность информации.
     *
     * @param id идентификатор пользователя, которого необходимо обновить.
     * @param user объект пользователя, содержащий обновленные данные.
     */
    @Modifying
    @CacheEvict(value = "users", key = "#id")
    @Query("UPDATE User u SET u.userName = :#{#user.userName}, " +
            "u.email = :#{#user.email}, " +
            "u.role = :#{#user.role} " +
            "WHERE u.id = :id")
    void updateUserById(@Param("id") Long id, User user);

}
