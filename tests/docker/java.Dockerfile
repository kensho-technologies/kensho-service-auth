# Java-maven test
FROM maven:3.8.6-openjdk-18
ARG PRIVATE_KEY_FILE
ARG CLIENT_ID
ARG SCOPES
COPY ${PRIVATE_KEY_FILE} /home/app/src/private_key.pem
ENV PRIVATE_KEY_FILE=/home/app/src/private_key.pem \
    SCOPES=${SCOPES} \ 
    CLIENT_ID=${CLIENT_ID}
COPY java/auth /home/app/src
WORKDIR /home/app/src/
RUN mvn clean
CMD mvn test
