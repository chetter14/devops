FROM openjdk:23-slim
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
COPY ./target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
