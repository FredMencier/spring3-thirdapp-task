# spring3-thirdapp-task
Sample App With Springboot3

Basic app : Task app with JPA
- 4 enities (Person, Address, Task, Audit)
- Container Managed EntityManager
    - @PersistenceContext (nécessite le starter spring-boot-starter-data-jpa)
    - Definition des datasources dans le application.properties
    - @Transactional (La gestion manuelle de la transaction n'est pas autorisé dans ce context)
- Base MySql for Server
- Base H2 in Memory for TU
- 1 Controller Rest : TaskController
    - search by id, status
    - create task
    - update task
    - delete task
    - get all tasks
- **1 WebFilter : log du temps passé dans la méthode**
- **Gestion des exceptions et application/json+error**
- Add and retrieve custom header value in header http
- Logging (Slf4j)
- Properties with .properties file
- Junit 5 (@SpringBootTest ne fonctionne pas sinon)
- Utilisation de RestTemplate pour les tests MVC