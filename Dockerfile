#FROM openjdk:21-jdk
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8082
#ENTRYPOINT ["java","-jar","/app.jar"]


#FROM maven:3.8.7-openjdk-18-slim AS build
#WORKDIR /home/app
#COPY . /home/app
#RUN mvn -f /home/app/pom.xml clean install
#
#FROM openjdk:18-jdk
#VOLUME /tmp
#COPY --from=build /home/app/target/*.jar app.jar
#EXPOSE 8082
#ENTRYPOINT ["sh","-c","java -jar /app.jar"]




FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean install

FROM openjdk:21-jdk
VOLUME /tmp
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]