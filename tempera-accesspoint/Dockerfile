FROM python:3.11.8-alpine AS build
LABEL authors="leo"

WORKDIR /home

COPY requirements.txt .
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

COPY ./src ./src
ENTRYPOINT ["/bin/sh", "app.sh"]
