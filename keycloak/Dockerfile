FROM quay.io/keycloak/keycloak:20.0.2-1 as builder

COPY import /opt/keycloak/data/import
COPY certs /etc/x509/https
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:20.0.2-1
COPY --from=builder /opt/keycloak/data/import/ /opt/keycloak/data/import/
COPY --from=builder /etc/x509/https/ /etc/x509/https/
COPY --from=builder /opt/keycloak/lib/quarkus/ /opt/keycloak/lib/quarkus/