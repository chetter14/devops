FROM openjdk:23-slim AS builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM openjdk:23-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
