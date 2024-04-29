FROM python:3.11.8-alpine
LABEL authors="leo"

WORKDIR /home
COPY requirements.txt .
RUN pip install --upgrade pip
RUN pip install -r requirements.txt
COPY . .
CMD ["/bin/sh", "app.sh"]
