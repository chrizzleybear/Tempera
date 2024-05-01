FROM alpine:3.17
LABEL authors="leo"

WORKDIR /home
COPY src .
RUN apk --no-cache add --update maven
RUN sed -i 's/localhost:5432/postgres_db:5555/' \
    main/resources/application.properties
CMD ["mvn", "spring-boot:run"]
