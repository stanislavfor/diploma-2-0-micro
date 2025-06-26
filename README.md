# Приложение Java Spring Boot с микросервисами
## Страницы в браузере для работы сервисов (URL	- Назначение)
### Главная страница http://localhost:8090/
http://localhost:8090/ Главная страница (image-hosting) <br>
http://localhost:8090/login	Форма входа (auth-service) <br>
http://localhost:8090/registration Регистрация (auth-service) <br>
http://localhost:8090/email-page Email форма (image-hosting) <br>

##
#### Очистка проекта для пересборки и сборка проекта:
```bash
  mvn clean install
```
#### Выполнить тесты:
```bash
  mvn test
```
#### Временное отключение теста, если нужно собрать проект
С помощью команды '-DskipTests' можно временно пропустите тесты:
```bash
  mvn clean install -DskipTests
```
##
#### Последовательность действий (маршрутизация):

1. Пользователь заходит на http://localhost:8081/login, auth-service показывает login.html
2. Регистрируется по адресу	http://localhost:8081/registration,	регистрирует пользователя через Keycloak Admin API, получают роль USER.
3. Пользователь admin@admin.com с ролью ADMIN добавляется вручную в Keycloak.
3. Авторизованный через Keycloak пользователь переходит по редирект на "/" - это адрес http://localhost:8084, где попадает в image-hosting-service, который отображает index.html
4. Загружает изображения http://localhost:8084/, где доступ только при наличии сессии. Доступ к / и остальным страницам разрешен только аутентифицированным пользователям с ролью USER.
5. Пароли, указанные через API или Admin-консоль в Keycloak, автоматически хешируются по политике паролей Keycloak. Все пароли сохраняются в базе PostgreSQL keycloak_db с хешированием.

##
#### Базы данных
1. Микросервис auth-service	
- Назначение: Keycloak управляет пользователями
- База данных (PostgreSQL): keycloak_db
2. Микросервис image-service
- Назначение: Хранение ссылок на изображения
- База данных (PostgreSQL): images_db

##
#### Все зарегистрированные пользователи через /registration получают роль USER.

Пользователь admin@admin.com с ролью ADMIN добавляется вручную в Keycloak.
Все пароли сохраняются в базе PostgreSQL keycloak_db с хешированием.
Доступ к / и остальным страницам разрешен только аутентифицированным пользователям с ролью USER.
Ошибки доступа ведут на /oops.


Шаблоны login.html, registration.html, index.html, email-page.html работают с маршрутами, направленными через микросервис api-service. 
Все пути в адресе начинаются с /api/..., чтобы проксировать запросы к backend-сервисам (например, /api/auth/..., /api/images/...).


##
### Маршруты к шаблонам в браузере

Шаблоны отображаются по адресам:

#### Frontend `frontend-service`, порт `8090`

`http://localhost:8090/login`        Login Страница входа <br>
`http://localhost:8090/registration` Registration Страница регистрации <br>
`http://localhost:8090/`             Index (главная) Главная после авторизации <br>
`http://localhost:8090/email-page`   Email Page  Страница формы email <br>
`http://localhost:8090/oops`         Ошибка (oops)  Общая страница ошибок <br>



#### Auth-сервис `auth-service`

Если шаблоны отправляются `POST`- запросы, то направляться через `api-service` (порт `8085`): <br>
`http://localhost:8085/auth/login` `POST` Отправка логина <br>
`http://localhost:8085/auth/registration` `POST` Отправка регистрации <br>
`http://localhost:8085/auth/logout` `GET` Logout<br>
`http://localhost:8085/auth/afterLogin` `GET` После входа <br>



#### Image-хостинг `image-hosting-service`

`http://localhost:8085/images/add` `POST`Добавление изображения <br>
`http://localhost:8085/images/delete?id={id}` `GET` Удаление изображения <br>
`http://localhost:8085/images/edit` `POST` Редактирование изображения <br>


#### Email-сервис в image-hosting

`http://localhost:8090/email-page` `GET` Форма отображается <br>
`http://localhost:8085/images/sendEmail` `POST` Отправка email <br>


Примечание:
1. Пользователь открывает шаблон через `frontend-service` (порт `8090`).
2. Форма в шаблоне отправляет запрос через `api-service` (порт `8085`) к нужному сервису: `auth`, `images`.
3. Ответ возвращается пользователю через `frontend-service`.


##
### Примеры фактических маршрутов URL в браузере:
	
- Страница логина	http://localhost:8080/login
- Регистрация	http://localhost:8080/registration
- Email форма	http://localhost:8080/email-page
- Главная после входа	http://localhost:8080/
- Добавление изображений	http://localhost:8080/api/images/add
- Редактирование изображения	http://localhost:8080/api/images/edit
- Удаление изображения	http://localhost:8080/api/images/delete
- Запрос авторизации	http://localhost:8080/api/auth/login
- Регистрация пользователя	http://localhost:8080/api/auth/registration

##
#### Порты для микросервисов:
auth-service: 8081

frontend-service: 8082

image-hosting: 8084

api-gateway: 8090
