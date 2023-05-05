
## Info
* Spring Boot 3.0.6
* MySQL 8.0.33
* Flyway 9.16.3

### Setup
1. ``docker-compose up -d``
2. ``./gradlew flywayMigrate``
3. ``./gradlew bootRun``

Database credentials

      MYSQL_DATABASE: 'assignment'
      MYSQL_USER: 'user'
      MYSQL_PASSWORD: 'password'
      MYSQL_ROOT_PASSWORD: 'afeeafaeggea'

Runs backend on http://localhost:8080

Front-end: https://github.com/tambettelvis/Cities.UI

To clean db run:
``
./gradlew flywayClean
``
