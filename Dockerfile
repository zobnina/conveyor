FROM openjdk:17-alpine
RUN mkdir /app
WORKDIR /app
COPY /target/conveyor.jar /app/conveyor.jar
ENTRYPOINT java -jar /app/conveyor.jar
EXPOSE 8081