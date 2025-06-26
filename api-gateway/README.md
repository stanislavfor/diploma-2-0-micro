# Микросервис api-gateway
## Запуск для микросервиса api-gateway

- Через IDE, где нажать Run в главном классе 'ApiGatewayApplication'
- Через терминал:
    - Перейти в корневую папку микросервиса:
      ```bash
      cd auth-service
      ```
- Выполнить очистку проекта по команде:
  ```bash
    mvn clean
  ```  
  или

  ```bash
   mvn clean install
  ```

- Выполнить команду запуска:
  ```bash    
  mvn spring-boot:run
  ```
- Работает запущеное приложение на адресе:
     http://localhost:8090/ <br><br>

- Команда остановить запущенный микросервис:
  ```bash
   Stop-Process -Id (Get-NetTCPConnection -LocalPort 8090).OwningProcess -Force

  ```

## `login.html` (авторизация пользователя)

```html
<form method="post" action="http://localhost:8085/auth/login">
    <label for="login">Email:</label>
    <input type="email" id="login" name="login" required>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Войти</button>
</form>

<!-- Кнопка перехода к регистрации -->
<a href="http://localhost:8090/registration">Зарегистрироваться</a>
```


## `registration.html` (регистрация нового пользователя)

```html
<form method="post" action="http://localhost:8085/auth/registration">
    <label for="login">Email (логин):</label>
    <input type="email" id="login" name="login" required>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Зарегистрироваться</button>
</form>

<!-- Кнопка возврата к логину -->
<a href="http://localhost:8090/login">Назад ко входу</a>
```



## `email-page.html` (форма обратной связи)

```html
<form method="post" action="http://localhost:8085/images/sendEmail">
    <label for="username">Ваше имя:</label>
    <input type="text" id="username" name="username" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>

    <label for="message">Сообщение:</label>
    <textarea id="message" name="message" rows="6" required></textarea>

    <button type="reset">Сбросить</button>
    <button type="submit">Отправить</button>
</form>
```


## `index.html` - основная страница

```
<!-- Добавление нового изображения -->
<form method="post" action="http://localhost:8085/images/add" enctype="multipart/form-data">
    <input type="text" name="title" placeholder="Название изображения" required>
    <input type="file" name="image" accept="image/*" required>
    <button type="submit">Загрузить</button>
</form>

<!-- Отображение загруженных изображений -->
<div class="gallery">
    <div class="image-item" th:each="item : ${images}">
        <img th:src="@{${item.link}}" alt="uploaded">
        <p th:text="${item.title}"></p>

        <!-- Редактирование -->
        <form method="post" action="http://localhost:8085/images/edit">
            <input type="hidden" name="id" th:value="${item.id}">
            <input type="text" name="title" placeholder="Новое название">
            <button type="submit">Редактировать</button>
        </form>

        <!-- Удаление -->
        <form method="get" th:action="@{'http://localhost:8085/images/delete?id=' + ${item.id}}">
            <button type="submit">Удалить</button>
        </form>
    </div>
</div>
```


##
#### Маршруты URL через API-сервис (коротко)

Действия GET на Главной странице `http://localhost:8090/` <br>
Действия POST Загрузка изображения `http://localhost:8085/images/add` <br>
Действия GET Удаление изображения `http://localhost:8085/images/delete?id=ID` <br>
Действия POST Редактирование заголовка `http://localhost:8085/images/edit` <br>


##
#### Примечания:

- Работа с изображениями для шаблона `index.html` в `image-hosting-service`.
- Маршруты для шаблонов `login.html`, `registration.html`, `email-page.html`, если они отображаются через `frontend-service` (на порту `localhost:8090`), отправляют свои данные через `api-service` (на порту `localhost:8085`):
- Сервис `frontend-service` - отображает HTML-шаблоны (порт `8090`). Модуль `frontend-service` передает `id`, `title`, `link` из контроллера как `model.addAttribute("images", ...)`.
- Сервис `api-service` - принимает `POST`-запросы (`localhost:8085/auth/...`, `.../images/...`) и пересылает их в нужный backend.
- Все `form action` указывают на API (порт `8085`), потому что `frontend-service` служит только для отображения страниц.
- Внутри шаблонов можно указывать абсолютные пути с `http://localhost:...` как маршруты при использовании нескольких портов, или использовать Thymeleaf с `th:action="@{...}"`, если они находятся на одном порту, через reverse proxy, например, nginx.
- `GET`-переходы, например, выполняются к `/` через `http://localhost:8090/`.
- HTML-шаблоны отдаются `frontend-service` (`http://localhost:8090`).
- Все действия, добавление, редактирование, удаление для кнопок и форм `edit`, `delete`, `add` проходят через `api-service` (`http://localhost:8085`).

## Маршруты подробнее

### Незащищённые маршруты (через API Gateway, порт localhost:8080):

#### `auth-service` (доступ по префиксу `/api/auth`):
```
HTTP:GET   http://localhost:8080/api/auth/login              — Страница входа
HTTP:GET   http://localhost:8080/api/auth/registration       — Страница регистрации
HTTP:POST  http://localhost:8080/api/auth/registration       — Отправка формы регистрации
```

#### `image-hosting` (доступ по префиксу `/api/images`):
```
HTTP:GET   http://localhost:8080/api/images/                 — Главная страница (index.html)
HTTP:GET   http://localhost:8080/api/images/email-page       — Форма отправки email
HTTP:POST  http://localhost:8080/api/images/sendEmail        — Отправка email-сообщения
```

### Защищённые маршруты (через API Gateway, требуют авторизации):
```
HTTP:POST  http://localhost:8080/api/images/upload           — Загрузка изображений
HTTP:GET   http://localhost:8080/api/images/images/{id}      — Просмотр изображения
HTTP:GET   http://localhost:8080/api/auth/profile            — Профиль пользователя
HTTP:GET   http://localhost:8080/api/auth/logout             — Выход из системы
HTTP:ANY   http://localhost:8080/api/**                      — Остальные вызовы API, защищённые через Spring Security или Keycloak
```

### Список маршрутов микросервисов через API Gateway:

#### Auth Service (`localhost:8081`, проксируется через `/api/auth/**`):

* `GET  /api/auth/login` — Страница входа
* `GET  /api/auth/registration` — Страница регистрации
* `POST /api/auth/registration` — Отправка формы регистрации
* `GET  /api/auth/profile` — Профиль пользователя (требует авторизации)
* `GET  /api/auth/logout` — Выход из системы (требует авторизации)



#### Image Hosting (`localhost:8084`, проксируется через `/api/images/**`):

* `GET  /api/images/` — Главная страница (index.html)
* `GET  /api/images/email-page` — Форма отправки email
* `POST /api/images/sendEmail` — Отправка email-сообщения
* `POST /api/images/upload` — Загрузка изображения (требует авторизации)
* `GET  /api/images/images/{id}` — Просмотр изображения (возможно защищено)

