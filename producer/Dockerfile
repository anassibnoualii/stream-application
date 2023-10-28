FROM gradle:8.4-jdk17 AS build
WORKDIR /app

COPY api/build.gradle producer/settings.gradle /app/
COPY producer .

RUN gradle clean bootJar

FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

COPY --from=build app/build/libs/*.jar /stream-producer.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/stream-producer.jar"]