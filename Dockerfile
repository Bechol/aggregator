FROM eclipse-temurin:21-jre-jammy
COPY build/libs/aggregator-0.0.1.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]