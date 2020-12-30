FROM maven:3.6.3-openjdk-11 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B package --file pom.xml -DskipTests

FROM openjdk:11
COPY --from=build /workspace/target/restful-web-service-spring-web-flux.jar app.jar
EXPOSE 6379
ENTRYPOINT ["java","-jar","app.jar"]
