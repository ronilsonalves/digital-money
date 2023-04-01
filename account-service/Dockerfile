#Build stage
FROM maven:3.6-openjdk-17-slim as build
ENV ACC_SVC_DB_PASS=05L7zz7emrXY
ENV ACC_SVC_DB_URL=dbaccounts.pi.ronilsonalves.com
ENV ACC_SVC_DB_USER=accounts-service
ENV ACC_SVC_NAME=accounts-service
ENV ACC_SVC_PORT=8082
ENV KEYCLOAK_DB_USER=postgres
ENV KEYCLOAK_DB_PASS=Y1JA779wa9UEHdQ9
ENV KEYCLOAK_DB_PORT=5432
ENV KEYCLOAK_DB_URL=pi2back-g2-db-keycloak.cy6nd2qhdavj.sa-east-1.rds.amazonaws.com
ENV CLIENT_SECRET_BACKEND=A3OQ2NR9S85xMVMQ88t2CfEciVbXb2ol
ENV CLIENT_ID_BACKEND=backend
ENV CLIENT_SECRET_USERS=thR85ow0FqYJmJTan2wTdNaX9gCxwLbf
ENV CLIENT_ID_USERS=users-service
ENV KEYCLOAK_SERVER_URL=https://auth.pi.ronilsonalves.com/
ENV REALM_TOKEN_URI=https://auth.pi.ronilsonalves.com/realms/digitalmoney/protocol/openid-connect/token
ENV JWT_ISSUER_URI=https://auth.pi.ronilsonalves.com/realms/digitalmoney
ENV S3_ACCESS_KEY_ID=AKIAV4YTWNKPA6IVRPH6
ENV JWT_ISSUER_URI=qxveqjjk7EpuNti8QFQiw6Qedw/KDpghoQ95d5iE
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage
FROM amazoncorretto:17.0.6-alpine3.16
COPY auth.pi.ronilsonalves.com.cer /tmp
RUN keytool -import -alias keycloak -storepass changeit -keystore $JAVA_HOME/lib/security/cacerts -noprompt -trustcacerts -file /tmp/auth.pi.ronilsonalves.com.cer
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE ${ACC_SVC_PORT}
ENTRYPOINT ["java","-jar","app.jar"]