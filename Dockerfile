
#WORKDIR /src

FROM   openjdk:17-jdk-alpine3.14 AS build
WORKDIR /app
COPY . .
ARG FILE_JAR=target/*.jar

COPY ${FILE_JAR} iTech_Heaven.jar


ENTRYPOINT ["java", "-jar", "iTech_Heaven.jar"]

EXPOSE 8080