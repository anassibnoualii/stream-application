FROM gradle:8.4-jdk17 AS build
WORKDIR /app

COPY api/build.gradle api/settings.gradle /app/
COPY api .

RUN gradle clean bootJar

FROM openjdk:17-slim

COPY --from=build app/build/libs/*.jar /stream-api.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/stream-api.jar"]