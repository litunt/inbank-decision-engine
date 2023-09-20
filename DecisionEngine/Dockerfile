FROM openjdk:17-jdk-slim-buster AS build
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY src src

RUN ./gradlew build

FROM openjdk:17-jdk-slim-buster AS base
WORKDIR /app
COPY --from=build /app/build/libs/decision-engine.jar .

CMD ["java", "-jar", "decision-engine.jar"]
