FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080

# COPY app.jar app.jar
# COPY hello_project-0.0.1-SNAPSHOT.jar app.jar
# COPY mongo_project-0.0.1-SNAPSHOT.jar app.jar
COPY target/spring-boot-data-mongodb-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
