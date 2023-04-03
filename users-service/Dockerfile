#Build stage
FROM maven:3.6-openjdk-17-slim as build
ENV KEYCLOAK_DB_USER=postgres
ENV KEYCLOAK_DB_PASS=Y1JA779wa9UEHdQ9
ENV KEYCLOAK_DB_PORT=5432
ENV KEYCLOAK_DB_URL=pi2back-g2-db-keycloak.cy6nd2qhdavj.sa-east-1.rds.amazonaws.com
ENV USR_SVC_DB_PASS=6hpFI32U74D3
ENV USR_SVC_DB_URL=dbusers.pi.ronilsonalves.com
ENV USR_SVC_DB_USER=users-service
ENV USR_SVC_NAME=users-service
ENV USR_SVC_PORT=8081
ENV CLIENT_SECRET_BACKEND=A3OQ2NR9S85xMVMQ88t2CfEciVbXb2ol
ENV CLIENT_ID_BACKEND=backend
ENV CLIENT_SECRET_USERS=thR85ow0FqYJmJTan2wTdNaX9gCxwLbf
ENV CLIENT_ID_USERS=users-service
ENV KEYCLOAK_SERVER_URL=https://auth.pi.ronilsonalves.com/
ENV REALM_TOKEN_URI=https://auth.pi.ronilsonalves.com/realms/digitalmoney/protocol/openid-connect/token
ENV JWT_ISSUER_URI=https://auth.pi.ronilsonalves.com/realms/digitalmoney
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage
FROM amazoncorretto:17.0.6-alpine3.16
COPY auth.pi.ronilsonalves.com.cer /tmp
RUN keytool -import -alias keycloak -storepass changeit -keystore $JAVA_HOME/lib/security/cacerts -noprompt -trustcacerts -file /tmp/auth.pi.ronilsonalves.com.cer
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE ${USR_SVC_PORT}
ENTRYPOINT ["java","-jar","app.jar"]