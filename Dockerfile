FROM openjdk:11
EXPOSE 3015
ADD target/demo-0.0.1-SNAPSHOT.jar comment-service.jar
ENTRYPOINT ["java","-jar","comment-service.jar"]