FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/Run4Spring-0.1.1.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-XX:ErrorFile=/app/data/hs_err_pid_%p.log", "-XX:+PerfDisableSharedMem", "-jar", "/app/app.jar"]