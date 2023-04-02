FROM gradle:7.1.0-jdk11 as build
WORKDIR /usr/app
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY gradle gradle
COPY src src
RUN ./gradlew build

FROM amazoncorretto:16-alpine-jdk
EXPOSE 8765
RUN mkdir /app
COPY --from=build /usr/app/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
