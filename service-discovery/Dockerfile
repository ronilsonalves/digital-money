#Build stage
FROM maven:3.6-openjdk-17-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage
FROM amazoncorretto:17.0.6-alpine3.16
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]