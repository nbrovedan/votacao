FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/votacao/src
COPY pom.xml /home/votacao
RUN mvn -f /home/votacao/pom.xml clean package

FROM openjdk:11-jre
COPY --from=build /home/votacao/target/votacao.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]