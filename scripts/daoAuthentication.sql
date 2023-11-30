USE `springsecurity`;

CREATE TABLE `users`
(
    `id`       int auto_increment,
    `username` varchar(30) NOT NULL,
    `password` varchar(80),
    `email`    varchar(50) unique,
    PRIMARY KEY (`id`)
);

CREATE TABLE `roles`
(
    `id`   int auto_increment,
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
);

create table users_roles
(
    `user_id` int,
    `role_id`   int,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `users` foreign key (`user_id`) references users (`id`),
    CONSTRAINT `roles` foreign key (`role_id`) references roles (`id`)
);

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_ADMIN');

insert into users(username, password, email)
VALUES ('user', '$2a$12$TP60axqj3ipV4Uo1hz9Nz.D7aAbMBrusiUusitqZD8FLyhKRe8zHe', 'user@mail.com');

insert into user_roles(user_id, role_id) VALUES (1, 2);