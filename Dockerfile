FROM --platform=linux/amd64  eclipse-temurin:17-jre-alpine

COPY build/libs/*.jar /stream-app.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","/stream-app.jar"]