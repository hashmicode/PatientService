FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/patient-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /patientservice.jar
ENTRYPOINT ["java", "-jar", "/patientservice.jar"]
