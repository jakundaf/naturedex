FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh wait-for-it.sh
RUN apk add --no-cache bash
RUN chmod +x wait-for-it.sh
EXPOSE 8082
ENTRYPOINT ["./wait-for-it.sh", "naturedex-db:3306", "--timeout=60", "--strict", "--", "java", "-jar", "app.jar"]

