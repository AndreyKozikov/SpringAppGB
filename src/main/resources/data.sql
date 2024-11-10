INSERT INTO users (user_name, password, email, role)
VALUES
('admin', '$2a$10$FiSf0ZihK5nqAz92szedCOJN9qL0IQkd5rbFoL.VtNDVH4L1LPlCm', 'admin@example.com', 'ROLE_ADMIN'),
('user', '$2a$10$FiSf0ZihK5nqAz92szedCOJN9qL0IQkd5rbFoL.VtNDVH4L1LPlCm', 'user@example.com', 'ROLE_USER');


INSERT INTO projects (name, description, created_date)
VALUES
('Проект А', 'Описание проекта А', '2024-01-01'),
('Проект Б', 'Описание проекта Б', '2023-02-15'),
('Проект B', 'Описание проекта B', '2022-02-15'),
('Проект Г', 'Описание проекта Г', '2023-02-15'),
('Проект Д', 'Описание проекта Д', '2024-06-15'),
('Проект Е', 'Описание проекта Е', '2024-03-10');

INSERT INTO users_project (related_entity_id, project_id, user_id)
VALUES
('1', '1', '2'),
('1', '3', '2'),
('1', '5', '2');