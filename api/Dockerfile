FROM --platform=linux/amd64  eclipse-temurin:17-jre-alpine
ARG VERSION=0.0.1
COPY api/build/libs/stream-api-${VERSION}.jar /stream-api.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","/stream-api.jar"]