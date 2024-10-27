package com.example.SpringAppGB.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Сущность, представляющая проект в системе.
 * Содержит основную информацию о проекте, включая его название, описание и дату создания.
 */
@Entity
@Data
@Table(name="projects")
public class Project {

    /**
     * Уникальный идентификатор проекта.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоинкремент
    private Long id;

    /**
     * Название проекта.
     * Не может быть null.
     */
    @Column
    private String name;

    /**
     * Описание проекта.
     * Может содержать подробную информацию о целях и задачах проекта.
     */
    @Column
    private String description;

    /**
     * Дата создания проекта.
     * Автоматически устанавливается при создании нового проекта.
     */
    @Column(name="created_date")
    private LocalDate createdDate;
}
