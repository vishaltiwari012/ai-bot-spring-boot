FROM maven:3.9.4-eclipse-temurin-21-alpine As build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/learn-ai-0.0.1-SNAPSHOT.jar learn-ai.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","learn-ai.jar"]