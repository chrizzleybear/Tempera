FROM alpine:3.17 AS build
LABEL authors="leo"

WORKDIR /home
RUN apk --no-cache add --update maven

# Download dependencies
# Separating this and the next step means that docker won't re-install all dependencies
# every time there is a change in source code because this step will be cached.
ADD ./pom.xml ./pom.xml
RUN mvn clean package -Dmaven.main.skip=true -Dmaven.test.skip=true && rm -r target

# Build package with source code
ADD ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine-jdk AS deploy
COPY --from=build /home/target/skeleton-*.jar /home/skeleton.jar
ENTRYPOINT ["java", "-jar", "/home/skeleton.jar"]
