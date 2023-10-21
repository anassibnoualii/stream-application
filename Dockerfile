FROM --platform=linux/amd64  openjdk:17.0.1-jdk-slim
COPY build/libs/*.jar /stream-app.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","/stream-app.jar"]