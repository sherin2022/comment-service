<<<<<<< HEAD
FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} comment-service.jar
EXPOSE 3015
ENTRYPOINT ["java","-jar","/comment-service.jar"]
=======
FROM openjdk:11
EXPOSE 3015
ADD target/demo-0.0.1-SNAPSHOT.jar comment-service.jar
ENTRYPOINT ["java","-jar","comment-service.jar"]
>>>>>>> 0edab2feb3b2b87d23b0d8c5afbb5ffeca810dcd
