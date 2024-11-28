package com.example.SpringAppGB.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Сущность, представляющая пользователя в системе.
 * Содержит основную информацию о пользователе, включая имя пользователя, пароль, email и роль.
 */
@Entity
@Data
@Table(name="users")
public class User{

    /**
     * Сущность, представляющая пользователя в системе.
     * Содержит основную информацию о пользователе, включая имя пользователя, пароль, email и роль.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоинкремент
    private Long id;

    /**
     * Имя пользователя.
     * Должно быть уникальным и не может быть null.
     * Максимальная длина - 100 символов.
     */
    @NotBlank(message = "The username cannot be empty")
    @Size(min = 3, max = 100, message = "The username must be between 3 and 100 characters long")
    @Column(name="user_name", nullable = false, length = 100)
    private String userName;

    /**
     * Пароль пользователя.
     * Не может быть null.
     * Максимальная длина - 255 символов.
     * Игнорируется при сериализации в JSON.
     */
    @NotBlank(message = "The password cannot be empty")
    @Size(min = 6, max = 255, message = "The password must contain from 6 to 255 characters")
    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String password;

    /**
     * Электронная почта пользователя.
     * Может быть null.
     */
    @Email(message = "Incorrect email format")
    @Column
    private String email;

    /**
     * Роль пользователя в системе.
     * Не может быть null.
     * Максимальная длина - 50 символов.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Roles role;

//    cascade = CascadeType.ALL: указывает Hibernate автоматически применять все каскадные операции
//    к зависимым записям UsersProject, включая удаление.
//    orphanRemoval = true: придает дополнительный эффект, удаляя "осиротевшие" записи,
//    т.е. записи UsersProject, которые больше не связаны с User.

    /**
     * Список проектов, связанных с данным пользователем.
     *
     * Это поле представляет собой связь "один ко многим",
     * где один пользователь может быть связан с несколькими проектами через
     * сущность {@link UsersProject}.
     *
     * @see UsersProject
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UsersProject> usersProjects = new ArrayList<>();

}
