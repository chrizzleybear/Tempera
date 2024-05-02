FROM python:3.11.8-alpine
LABEL authors="leo"

# Setup
WORKDIR /home
COPY requirements.txt .
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

# Copy project and change to non-root user
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot
USER nonroot
COPY --chown=nonroot:nonroot . .

# Run
CMD ["/bin/sh", "app.sh"]
