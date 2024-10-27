package com.example.SpringAppGB.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Сущность, представляющая связь между пользователями и проектами.
 * Реализует отношение многие-ко-многим между пользователями и проектами.
 * Наследуется от EntityWithRelation, добавляя специфические поля для связи.
 */
@Entity
@Data
@Table(name = "users_project")
public class UsersProject extends EntityWithRelation{
    /**
     * Связь с проектом.
     * Представляет проект, к которому привязан пользователь.
     * Реализует отношение многие-к-одному с сущностью Project.
     */
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    /**
     * Связь с пользователем.
     * Представляет пользователя, который привязан к проекту.
     * Реализует отношение многие-к-одному с сущностью User.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
