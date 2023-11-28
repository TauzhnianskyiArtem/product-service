FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/*-exec.jar app.jar

EXPOSE 8080 8443
ENTRYPOINT ["java", "-jar", "app.jar"]