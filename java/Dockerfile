FROM maven:3.8.6-openjdk-18
COPY auth /home/app/src
WORKDIR /home/app/src/
RUN mvn clean
CMD mvn test
