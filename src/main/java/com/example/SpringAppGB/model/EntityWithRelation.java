package com.example.SpringAppGB.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Абстрактный базовый класс для сущностей, имеющих связь с другими сущностями.
 * Предоставляет общие поля и функциональность для классов-наследников.
 */
@MappedSuperclass
@Data
public abstract class EntityWithRelation {

    /**
     * Уникальный идентификатор сущности.
     * Генерируется автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоинкремент
    private Long id;

    /**
     * Идентификатор связанной сущности.
     * Используется для установления связи с другой сущностью в системе.
     * Не может быть null.
     */
    @Column(name = "related_entity_id", nullable = false)
    private Long relatedEntityId;
}
