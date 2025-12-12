FROM eclipse-temurin:21-jdk-jammy as build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -q -N -e -B -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8080
ENV JAVA_OPTS="-Xms256m -Xmx512m"
COPY --from=build /workspace/target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
