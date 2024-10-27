INSERT INTO users (user_name, password, email, role)
VALUES
('admin', 'password123', 'admin@example.com', 'ADMIN'),
('user1', 'password456', 'user1@example.com', 'USER'),
('user2', 'password789', 'user2@example.com', 'USER');


INSERT INTO projects (name, description, created_date)
VALUES
('Проект А', 'Описание проекта А', '2024-01-01'),
('Проект B', 'Описание проекта B', '2024-02-15'),
('Проект C', 'Описание проекта C', '2024-03-10');

INSERT INTO users_project (related_entity_id, project_id, user_id)
VALUES
(1, 1, 2),
(2, 1, 3),
(3, 2, 2);
