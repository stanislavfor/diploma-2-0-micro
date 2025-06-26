# Микросервис auth-service - это Spring Boot-приложение
## Запуск для микросервиса auth-service

- Через IDE, где нажать Run в главном классе `AuthServiceApplication`
- Через терминал:
  - Перейти в корневую папку микросервиса:
    ```bash
    cd auth-service
    ```
- Выполнить очистку проекта по команде:
  ```bash
    mvn clean
  ```
  - Выполнить команду запуска:
    ```bash    
    mvn spring-boot:run
    ```
После запуска сервис будет доступен по адресу (если в application.yml указан server.port: 8081): <br>
http://localhost:8090/


##
### Работа с сервисом Keycloak

Важно, чтобы микросервисы используют одинаковую конфигурацию Keycloak (Realm, Client).

1. В Keycloak -> Client diploma-app:

- redirect-uri: http://localhost:8084/*

- web origin: +

2. Настроить Spring Security в image-hosting-service как bearer-only: true.



После запуска Keycloak с Realm diploma-realm и клиента diploma-app, <br>
работа микросервиса uth-service обеспечивает разделение логики авторизации и регистрации.
В application.yml должен быть указан правильный Keycloak realm, client и credentials. <br>
Последовательность запуска:
- Keycloak запущен
- Realm diploma-realm создан
- Client diploma-app создан (тип: public или confidential)
- Включено Direct Access Grants  

```yaml
keycloak:
    realm: diploma-realm
    auth-server-url: http://localhost:8080/
    resource: diploma-app
    public-client: true
    credentials:
      secret: your-client-secret
```
##
#### Порядок выполнения регистрации и авторизации (шаблоны)

1. GET /login - форма логина.
2. Из login - кнопка "зарегистрироваться" ведёт на /registration.
3. GET /registration - форма регистрации.
4. POST /registration - создаёт пользователя в Keycloak с ролью USER, редиректит обратно.
5. Отправка формы логина запускает Keycloak-авторизацию. При успехе - /afterLogin, редирект на http://localhost:8084/.
6. Неавторизованные запросы -> /login. 
7. Ошибочные URL -> /oops, oops.html (на ошибки и 403/404).

#### Контроллер AuthController

AuthController.java должен обрабатывать страницы для безопасной авторизации:
- Страница логина - @GetMapping("/login")          
- Страница регистрации@GetMapping("/registration")  
- Отправка формы регистрации@PostMapping("/registration") <br>
Регистрация отправляет данные в Keycloak через KeycloakAdminService (через REST API Keycloak). <br>
После регистрации производится редирект на /login. <br>
Включено Direct Access Grants, если авторизация не через браузер.

##
#### Как использовать `AppProperties`

Класс AppProperties в Spring Boot - это конфигурационный бин, предназначенный для удобного доступа к пользовательским (кастомным) параметрам из файла application.yml или application.properties. <br>
Автоматически инициализируется Spring Boot при старте системы, расширяемый, то есть можно добавлять любое количество полей для защиты при работе с вложенными структурами. <br>
```
app:
  redirect-url: http://localhost:8084/
  auth:
    client-id: my-app
    client-secret: s3cr3t

```
, где:
```
public class AppProperties {
    private String redirectUrl;
    private Auth auth;

    @Getter @Setter
    public static class Auth {
        private String clientId;
        private String clientSecret;
    }
}

```
Применяется AppProperties для централизованного доступа к настройкам, используется один раз через @Autowired AppProperties, и доступ к значению 'redirectUrl' будет доступен во всех нужных местах.
Безопасен в работе с конфиденциальными параметрами, где можно передавать secret, API ключи, URLs и другие чувствительные параметры через application.yml. <br> 
Самостоятельно класс `AppProperties`, в коде не используется напрямую, например, не внедряется через `@Autowired` или не используется в контроллере/сервисе/конфигурации. <br>
Этот класс автоматически связывает значения из конфигурации, например, в application.yml:
```yaml
app:
  redirect-url: http://localhost:8084/
```
Чтобы это свойство `app.redirect-url` работало по назначению, `AppProperties` нужно внедрить в нужный компонент, где будете использоваться `redirectUrl`, например, использовать в `AuthController`:

```
package com.example.auth.controller;

import com.example.auth.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final AppProperties appProperties;

    @Autowired
    public AuthController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/afterLogin")
    public String redirectAfterLogin() {
        // Здесь используем redirectUrl из application.yml
        return "redirect:" + appProperties.getRedirectUrl(); // http://localhost:8084/
    }
}
```

Можно внедрять `AppProperties` в:
- Сервисы (`@Service`)
- Конфигурации (`@Configuration`)
- Безопасность (`SecurityConfig`), если делать `RedirectStrategy` вручную

Можно внедрять `AppProperties` в любой компонент (`@Controller`, `@Service`, и т.п.), <br> 
либо Spring Boot просто создаст бин без использования, либо использовать `AppProperties` в нужном месте.

##
#### Подключение к другим микросервисам, например, image-hosting

- Пользователь логинится через auth-service, после логина перенаправляется на http://localhost:8084/ (image-hosting).
- Для передачи авторизации в image-hosting-service используется сессия или токен доступа (если REST API)
- Можно добавить Spring Cloud Gateway или OAuth2 Resource Server.

Для поиска ошибок можно использовать команду:
  ```bash
    mvn spring-boot:run -X
  ```
