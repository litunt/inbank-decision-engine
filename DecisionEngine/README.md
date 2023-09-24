# Back-end. Decision Engine (Inbank test assignment)

## Requirements

* JDK 17
* Docker v20

## Structure of the source code

### Source code layers

* config - configuration layer, provides configurations for application
* controller - controller layer, accepts REST requests
* decisionengine - layer of the whole decision engine logic
* dto - model layer, contains the structure of models used throughout the application
* enums - holds enumerate value classes
* exceptions -layer that contains exception handling objects
* model - contains entity classes
* repository - layer with repository interfaces to get data from the database
* service - service layer, responsible for business logic
* utils - helper layer, includes commonly used functionality
* validation - layer that contains validation handling

## Application configurations

Configuration file [application.yaml](src/main/resources/application.yaml),
with **environmental** variables

* `APP_PORT` - Java application port
* `APP_DB` - database schema name
* `APP_DB_USER` - database user username
* `APP_DB_PASSWORD` - database user password

## Installation guide

Application is built as `Docker image`

## Database

Given project uses relation database PostgreSQL. Database schema, including necessary tables and constraints is
being generated automatically on application startup. Java library Hibernate helps with correct detection of
object-enity relations.

## Database migration
As a database migration tool is used Flyway. Migrations are located under `src/resources/db/migration` package
and file naming is structured as `V1__name`, where `V1` indicates the new version of the database that changes bring.

## Building
Although there is also an option to create a `Dockerfile` to build an application image for further usage, `Gradle`
build tool actually provides a ready functionality to build `Docker` image of the application using `Gradle task`
named `bootBuildImage`. This task also allows to upload the image to `DockerHub registry` using ` docker { publishRegistry {...} }`
block by providing `DockerHub` credentials, but in this particular project this part was emitted.
Application is built as `Docker` container along with corresponding _Gradle_ parameters as _properties_.

### Docker container parameters

* `imageNameProp` - name of application container

### Building application Docker container with commands
Image building

```sh
./gradlew bootBuildImage -x test -PimageName=decision-engine-backend
```

## Running application locally

Run the command:
```sh
./gradlew bootRun
```

Running application tests:

```sh
./gradlew DecisionEngine:test
```

## Using application locally
To use application by URL on local machine after successfully setting up and running `Docker` containers,
the application is accessible by `http://localhost:8081/api` URL.

## Application healthcheck

In order to make sure what is the status of the application, is it up and running etc., the Spring Boot framework's
library Actuator is being used. It makes possible to navigate by `app-url/actuator/health` (e.g. regarding
this specific project and running locally `http://localhost:8081/api/actuator/health`) and get the information.
This provides the data about application's current status, is application up or down, what is the version of the application etc.
