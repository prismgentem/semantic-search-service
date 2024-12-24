FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/semantic-search-service-1.0.0.jar /app/semantic-search-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/semantic-search-service.jar"]