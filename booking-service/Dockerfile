FROM openjdk:17-jdk-slim

WORKDIR /app

COPY booking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9095

ENTRYPOINT ["java", "-jar", "app.jar"]