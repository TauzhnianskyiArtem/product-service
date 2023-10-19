FROM gradle:8.4.0-jdk21 AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY src ./src
COPY config ./config

RUN gradle build

FROM amazoncorretto:21-alpine

COPY --from=builder app/build/libs/*.jar app.jar

EXPOSE 8080 8443
ENTRYPOINT ["java", "-jar", "app.jar"]