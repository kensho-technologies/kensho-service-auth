FROM maven:3.8.6-openjdk-18
WORKDIR /home/app/src/
COPY auth .
RUN openssl genrsa -out private_key.pem 2048
RUN mvn clean
CMD mvn test
