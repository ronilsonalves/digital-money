#Build stage
FROM maven:3.6-openjdk-17-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Package stage
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE ${ACC_SVC_PORT}
ENTRYPOINT ["java","-jar","app.jar"]