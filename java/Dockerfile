FROM maven:3.8.6-openjdk-18
WORKDIR /home/app/src/
COPY auth .
RUN mvn clean
CMD mvn test
