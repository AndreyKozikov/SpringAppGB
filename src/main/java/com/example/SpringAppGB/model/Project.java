package com.example.SpringAppGB.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="created_date")
    private LocalDate createdDate;

//    cascade = CascadeType.ALL: указывает Hibernate автоматически применять все каскадные операции
//    к зависимым записям UsersProject, включая удаление.
//    orphanRemoval = true: придает дополнительный эффект, удаляя "осиротевшие" записи,
//    т.е. записи UsersProject, которые больше не связаны с Project.

    /**
     * Список проектов пользователей, связанных с данным проектом.
     *
     * @see UsersProject
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UsersProject> usersProjects = new ArrayList<>();
}
