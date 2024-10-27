package com.example.SpringAppGB.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс, представляющий запрос на связывание пользователей с проектом.
 * Используется для передачи данных между клиентом и сервером при операциях
 * добавления пользователей в проект или удаления пользователей из проекта.
 */
@Component
@Data
public class UserProjectRequest {

    /**
     * Идентификатор проекта, с которым производится операция.
     */
    private Long projectId;

    /**
     * Список идентификаторов пользователей, участвующих в операции.
     * Может содержать ID пользователей для добавления в проект или удаления из проекта.
     */
    private List<Long> userIds;

}
