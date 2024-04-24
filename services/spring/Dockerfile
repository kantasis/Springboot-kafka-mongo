FROM eclipse-temurin:17-jdk-alpine

# Bezkoder suggests this:
# FROM maven:3.8.5-openjdk-17

# I don't think this one is necessary
# Ports are not exposed to host machines, only exposed to other services.
# TODO: Remove this
EXPOSE 8080

COPY demo/target/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
