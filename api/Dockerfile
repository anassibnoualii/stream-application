FROM gradle:8.4-jdk17 AS build
WORKDIR /app

COPY api/build.gradle api/settings.gradle /app/
COPY api .

RUN gradle clean bootJar

FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

COPY --from=build app/build/libs/*.jar /stream-api.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/stream-api.jar"]