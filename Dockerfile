FROM gradle:8.0.2-jdk19 as build
WORKDIR /usr/app
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
# Run a failed build on purpose so the gradle daemon will be running for subsequent changes to source files
# Even though it will be registered as "incompatible", it still cuts way down on initialization time
RUN gradle clean build > /dev/null 2>&1 || true
COPY src src
RUN gradle build

FROM amazoncorretto:20-alpine-jdk
EXPOSE 8765
RUN mkdir /app
COPY --from=build /usr/app/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
