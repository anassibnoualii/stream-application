FROM --platform=linux/amd64  eclipse-temurin:17-jre-alpine
ARG VERSION=0.0.1
COPY build/libs/stream-application-${VERSION}.jar /stream-app.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","/stream-app.jar"]