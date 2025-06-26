##
#### Запуск микросервиса

1. Перейти в терминале к модулю:

```bash
    cd frontend-service
  ```
2. Выполнить очистку проекта по команде:
  ```bash
    mvn clean
  ```

3. Запустить модуль командой:
  ```bash
        mvn spring-boot:run
  ```

<br>

**Примечание:** если НЕ установлен Maven требуется запустить с командой: ```./mvnw spring-boot:run ```

<br>
3. После запуска сервис доступен по адресу, если настроен application.yml, по адресу: http://localhost:8090/

http://localhost:8090/


### Незащищённые маршруты (доступны без авторизации):

#### `auth-service`:

HTTP:GET  URL:`/login` Назначение:Страница входа <br>                
HTTP:GET  URL:`/registration` Назначение:Страница регистрации <br>  
HTTP:POST  URL:`/registration` Назначение:Отправка формы регистрации <br>

#### `image-hosting`:

HTTP:GET  URL:`/` Назначение:Главная страница с изображениями (`index.html`) <br>  
HTTP:GET  URL:`/email-page` Назначение:Форма отправки email <br>     
HTTP:POST  URL:`/sendEmail` Назначение:Отправка email-сообщения <br>

### Защищённые маршруты (требуют авторизации):

HTTP:POST URL:`/upload` Назначение:Загрузка изображений <br>  
HTTP:GET URL:`/images/{id}` Назначение:Просмотр отдельного изображения (если не открыто публично) <br>
HTTP:GET URL:`/profile` Назначение:Профиль пользователя <br>
HTTP:GET URL:`/logout` Назначение:Выход из системы (чаще защищено Keycloak/Security) <br>
HTTP:Все URL:`/api/**` Назначение:Вызовы REST API (например, через gateway) <br>   