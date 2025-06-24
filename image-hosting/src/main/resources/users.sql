create table users
(
    id       bigint default nextval('users_id_seq'::regclass) not null
        primary key,
    login    varchar(255)                                     not null
        unique,
    password varchar(255)                                     not null
);

alter table users
    owner to postgres;

INSERT INTO public.users (id, login, password) VALUES (1, 'admin', '$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG');
INSERT INTO public.users (id, login, password) VALUES (2, 'user', '$2a$12$.dlnBAYq6sOUumn3jtG.AepxdSwGxJ8xA2iAPoCHSH61Vjl.JbIfq');
INSERT INTO public.users (id, login, password) VALUES (3, 'anon', '$2a$12$tPkyYzWCYUEePUFXUh3scesGuPum1fvFYwm/9UpmWNa52xEeUToRu');
