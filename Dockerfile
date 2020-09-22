FROM openjdk:8-jdk-alpine
LABEL maintainer="glenn.jo@kakaocorp.com"

VOLUME /tmp

ARG JAR_FILE=build/libs/spring-async-sample2-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} spring-async-sample2.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-jar", "/spring-async-sample2.jar"]

#사용 포트
EXPOSE 8081