FROM gradle:7.1.0-jdk11 as build
WORKDIR /usr/app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
# Only download dependencies
# Eat the expected build failure since no source code has been copied yet
RUN gradle clean build > /dev/null 2>&1 || true

#COPY gradle gradle
COPY src src
RUN gradle build

FROM amazoncorretto:16-alpine-jdk
EXPOSE 8765
RUN mkdir /app
COPY --from=build /usr/app/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
