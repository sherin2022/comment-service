FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} comment-service.jar
EXPOSE 3015
ENTRYPOINT ["java","-jar","/comment-service.jar"]