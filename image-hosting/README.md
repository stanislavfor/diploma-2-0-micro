## Запуск модуля image-hosting

1. Выполнить в корне модуля image-hosting:

    ```bash
    mvn clean install
    ```
2. Запустить ImageHostingApplication.

3. Перейти в браузере по адресу: http://localhost:8084

##
#### Страница авторизации (login.html):

Это страница, на которой пользователи могут ввести свои логин и пароль для входа в систему.

## Главная страница:
### URL: http://localhost:8084/

##
#### Настройки для файла application.yml

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/images_db
    username: postgres # указать имя User из базы данных
    password: password # указать пароль, записанный для данной базы данных
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

```

#### SQL-запросы для создания базы данных PostgreSQL

Создание базы данных и таблиц в PostgreSQL:

```
-- Создать базу данных
CREATE DATABASE images_db;

-- Переключиться на созданную базу данных
\c images_db;

-- Создать таблицу users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL    
);

-- Создать таблицу item
CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    link VARCHAR(200)
);

```

Добавление значений в Таблицу 'users'
```
INSERT INTO users (login, password) VALUES
('admin', '$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG'),  -- пароль зашифрован
('user', '$2a$12$.dlnBAYq6sOUumn3jtG.AepxdSwGxJ8xA2iAPoCHSH61Vjl.JbIfq')   -- пароль зашифрован


```

#### Thymeleaf шаблон страницы my-app.html

```
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
    <form th:action="@{/login}" method="post">
        <div>
            <label for="login">Login:</label>
            <input type="text" id="login" name="username"/>
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password"/>
        </div>
        <div>
            <button type="submit">Login</button>
        </div>
    </form>
</body>
</html>

```

#### Хеширование пароля

```
String rawPassword = "admin";
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
boolean matches = encoder.matches(rawPassword, "$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG");
System.out.println(matches); 
// Должно быть true

```


<br><br>