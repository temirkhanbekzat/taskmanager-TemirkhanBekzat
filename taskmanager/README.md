# Task Management System
### Разработано: Temirkhan Bekzat

## Описание проекта
Backend REST API для системы управления задачами. Разработано на Spring Boot 3.2 с PostgreSQL.

## Технологии
- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- MapStruct / ручной маппинг
- Swagger UI (OpenAPI 3)
- Docker + Docker Compose

## Сущности
1. **User** — пользователи системы (роли: USER, ADMIN, MANAGER)
2. **Project** — проекты (группируют задачи)
3. **Task** — задачи (основная сущность, с приоритетом и статусом)
4. **Category** — категории для задач
5. **Comment** — комментарии к задачам
6. **Attachment** — файловые вложения к задачам

## Запуск через Docker

```bash
docker-compose up --build
```

## Запуск локально

1. Запустите PostgreSQL (порт 5432, БД: taskmanager, user: postgres, pass: postgres)
2. Запустите приложение:
```bash
mvn spring-boot:run
```

## API Документация
После запуска: http://localhost:8080/swagger-ui.html

## Endpoints

### Auth (публичные)
- POST /api/auth/register — регистрация
- POST /api/auth/login — вход, получение JWT

### Tasks (защищённые)
- GET /api/tasks — список с пагинацией, сортировкой, поиском, фильтрацией
  - Параметры: search, status, priority, projectId, assigneeId, categoryId, page, size, sortBy, sortDir
- POST /api/tasks — создать задачу
- GET /api/tasks/{id} — получить задачу по ID
- PUT /api/tasks/{id} — обновить задачу
- DELETE /api/tasks/{id} — удалить задачу
- GET /api/tasks/my — мои задачи
- GET /api/tasks/project/{projectId} — задачи проекта
- POST /api/tasks/{id}/notify — отправить уведомление (async)
- GET /api/tasks/project/{projectId}/report — сгенерировать отчёт (async)

### Projects
- GET /api/projects — все проекты
- POST /api/projects — создать
- GET /api/projects/{id} — по ID
- PUT /api/projects/{id} — обновить
- DELETE /api/projects/{id} — удалить
- GET /api/projects/my — мои проекты

### Categories
- GET /api/categories
- POST /api/categories
- GET /api/categories/{id}
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

### Comments
- GET /api/tasks/{taskId}/comments
- POST /api/tasks/{taskId}/comments
- DELETE /api/tasks/{taskId}/comments/{commentId}

### Files
- POST /api/tasks/{taskId}/files — загрузить файл
- GET /api/tasks/{taskId}/files — список файлов
- GET /api/tasks/{taskId}/files/{id}/download — скачать
- DELETE /api/tasks/{taskId}/files/{id} — удалить

### Users
- GET /api/users/me — текущий пользователь
- GET /api/users/{id} — по ID
- GET /api/users — все (ADMIN)
- DELETE /api/users/{id} — удалить (ADMIN)

## Рекомендуемые коммиты для GitHub

```
1. Initial project setup: Spring Boot, pom.xml, project structure
2. Add entity: User with role enum
3. Add entity: Project
4. Add entity: Task with status and priority enums
5. Add entities: Category, Comment, Attachment
6. Add repositories for all entities with custom queries
7. Add DTO classes: request and response
8. Add manual mappers for Task, Project, User, Comment, Category
9. Add exception classes and GlobalExceptionHandler
10. Add JWT utility class and JWT auth filter
11. Add UserDetailsService implementation
12. Add Security configuration with JWT
13. Add AsyncConfig with thread pool executor
14. Add Swagger/OpenAPI configuration
15. Implement AuthService: register and login
16. Implement TaskService with pagination, filtering, search
17. Implement ProjectService CRUD
18. Implement CategoryService CRUD
19. Implement CommentService
20. Implement FileService: upload, download, async processing
21. Add controllers: Auth, Task, Project
22. Add controllers: Category, Comment, File, User
23. Add async methods: notification and report generation
24. Add Dockerfile with multistage build and health check
25. Add docker-compose.yml with PostgreSQL and health checks
```

## Структура проекта
```
src/main/java/com/bekzat/temirkhan/taskmanager/
├── config/          # Security, Swagger, Async configs
├── controller/      # REST controllers
├── dto/
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions + GlobalExceptionHandler
├── mapper/          # Entity -> DTO mappers
├── repository/      # JPA repositories
├── security/        # JWT util, filter, UserDetailsService
└── service/
    └── impl/        # Service implementations
```
