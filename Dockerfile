FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/product-service-0.0.1-SNAPSHOT-exec.jar product-service-0.0.1-SNAPSHOT-exec.jar

EXPOSE 8080 8443
ENTRYPOINT ["java", "-jar", "product-service-0.0.1-SNAPSHOT-exec.jar"]