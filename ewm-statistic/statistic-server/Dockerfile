FROM amazoncorretto:11-alpine-jdk
COPY target/statistic-server-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]