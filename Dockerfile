FROM amazoncorretto:17.0.0-alpine as builder
COPY build/libs/release-tracker-0.0.1-SNAPSHOT.jar release-tracker-0.0.1.jar
ENTRYPOINT ["java","-jar","/release-tracker-0.0.1.jar"]
