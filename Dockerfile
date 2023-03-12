FROM openjdk:8-jdk-alpine
LABEL maintainer="chimaemmanuel.ezeamama@gmail.com"
VOLUME /main-app
ADD target/ad-exchange-channel-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "/app.jar"]