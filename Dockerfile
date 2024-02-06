FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . .
RUN mvn -f /app/pom.xml clean package -D maven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]