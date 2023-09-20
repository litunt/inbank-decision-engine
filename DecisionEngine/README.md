# Back-end. Decision Engine (Inbank test assignment)

## Requirements

* JDK 17
* Docker v20

## Structure of the source code

### Source code layers

* config - configuration layer, provides configurations for application
* controller - controller layer, accepts REST requests
* dto - model layer, contains the structure of models used throughout the application
* service - service layer, responsible for business logic
* exceptions - layer that contains exception handling objects
* util - helper layer, includes commonly used functionality

## Application configurations

Configuration file [application.yaml](src/main/resources/application.yaml),
with **environmental** variables

* `APP_PORT` - Java application port
* `APP_DB` - database schema name
* `APP_DB_USER` - database user username
* `APP_DB_PASSWORD` - database user password

## Installation guide

Application is built as `Docker image`

To build the application image, run the command:
```sh
docker build --no-cache -t decision-engine-backend .
```

## Running application locally

Run the command:
```sh
./gradlew DecisionEngine:bootRun
```

Running application tests:

```sh
./gradlew DecisionEngine:test
```
