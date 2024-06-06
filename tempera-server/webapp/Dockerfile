FROM node:20-alpine AS node
LABEL authors="lpr"

WORKDIR /home

RUN npm install -g @angular/cli@17.3.2
COPY . .
RUN npm install

ENTRYPOINT ["ng", "serve", "--host", "0.0.0.0"]
EXPOSE 4200
