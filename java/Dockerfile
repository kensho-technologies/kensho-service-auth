FROM alpine/openssl:3.3.2 AS openssl
WORKDIR /home/
RUN openssl genrsa -out private_key.pem -traditional 2048

FROM maven:3.8.6-openjdk-18
WORKDIR /home/app/src/
COPY auth .
COPY --from=openssl /home/private_key.pem .
RUN mvn clean
CMD mvn test
