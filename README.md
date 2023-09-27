# Decision Engine (Inbank test assignment)

## Project description

### Business idea
The idea of test assignment was to design such system that would make a decision on if the person who 
applies for a loan can get the desired amount of money to be paid in asked period of months, based on
input data given and this particular user profile properties. The Decision Engine always tries to make
the best offer for the applicant that is possible.

### Result
The result of the assignment is working full-stack application with backend that is responsible for
heavy logic and calculations, and frontend part that provides UI solution to send requests and see
the result returned by backend application.

As technical solution for the main logic (decision engine), I have implemented "Chain of responsibility"
programming pattern. This seemed to be the most suitable solution in given situation, when it is important
to perform various checks and validations with additional logic on each step.

In real-life situation, user data is being requested from external registries. However, this part is being
simplified in current project since I decided to put the main emphasis on engine logic. I have chosen the simplest
option - database with readonly rights and pre-inserted data for testing. 

Of course, this type of situation could also be imitated by having the second application and making
two backend applications to communicate using `AMQP` protocol. Why `AMQP` protocol would be more preferable
to `HTTP` when communication in microservices' system? Because `AMQP` protocol allows for 
decoupled and asynchronous communication, this protocol is really reliable and ensures message delivery by
using queues and putting messages into the queue until it is ok to send it further. These are probably main
pros considering this particular case (asking user data from external registry).

### Technology stack

- **Front-end:** Angular 16, TypeScript, PrimeNG, RxJS
- **Back-end:** Java, Spring Boot, JPA, Flyway

## Architecture

### Back-end
To look through the structure of backend application, please look at following specifications.
Docs: [DecisionEngine/README.md](DecisionEngine/README.md)

### Front-end
To look through the structure of frontend application, please look at following specifications.
Docs: [frontend/README.md](frontend/README.md) 

## Running application locally with [docker-compose.yaml](docker/docker-compose.yaml) file

### Requirements

- Docker v20
- Docker Compose v2

Run command

```sh
docker-compose up -d
```
The command starts all the listed services and the result is running `Docker` containers of 
frontend and backend applications, as well as PostgreSQL database and Flyway
(runs only once to perform migration tasks).
