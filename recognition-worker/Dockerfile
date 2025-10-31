FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/recognition-worker-0.0.1-SNAPSHOT.jar app.jar
#COPY wait-for-it.sh wait-for-it.sh
#RUN apk add --no-cache bash
#RUN chmod +x wait-for-it.sh
EXPOSE 8085

#ENTRYPOINT ["./wait-for-it.sh", "naturedex-db:3306", "--timeout=60", "--strict", "--", "java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]