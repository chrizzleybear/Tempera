FROM alpine:3.17
LABEL authors="leo"

# Setup
WORKDIR /home
RUN apk --no-cache add --update maven

# Copy project and change to non-root user
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot
RUN chown -R nonroot /home
USER nonroot
COPY --chown=nonroot:nonroot . .

# Run
CMD ["mvn", "spring-boot:run"]
