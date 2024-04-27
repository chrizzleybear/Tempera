FROM python:3.11.8-alpine
LABEL authors="leo"

WORKDIR /src
COPY _test_api.py .
RUN pip install --upgrade pip
RUN pip install fastapi[all]

ENTRYPOINT ["uvicorn", "_test_api:app", "--host", "0.0.0.0", "--port", "80"]
