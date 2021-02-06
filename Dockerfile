FROM adoptopenjdk/maven-openjdk11:latest as MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package -Dmaven.test.skip=true

FROM adoptopenjdk:11-jre-hotspot

WORKDIR app/
COPY --from=MAVEN_BUILD /build/target/homework-*-SNAPSHOT.jar /app/application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "application.jar"]