FROM python:3.11.8-alpine
LABEL authors="leo"

# Setup
WORKDIR /home
RUN pip install --upgrade pip
RUN pip install fastapi[all]

# Copy project and change to non-root user
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot
USER nonroot
COPY --chown=nonroot:nonroot . .

# Run
ENTRYPOINT ["uvicorn", "_test_api:app", "--host", "0.0.0.0", "--port", "80"]
