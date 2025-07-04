FROM amazoncorretto:17
WORKDIR /app
COPY target/agents-server-mcp-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]