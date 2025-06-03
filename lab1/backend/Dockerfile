FROM maven:3.9.6-eclipse-temurin-23 AS builder
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM openjdk:23-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
