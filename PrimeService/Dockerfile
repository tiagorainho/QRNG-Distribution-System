FROM maven:3.8-openjdk-17 AS build
RUN mkdir -p /workspace


WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean install -Dmaven.test.skip=true


FROM adoptopenjdk/openjdk16

COPY --from=build /workspace/target/spring-boot-app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 
