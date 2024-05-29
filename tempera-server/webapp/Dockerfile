FROM node:20-alpine AS node
LABEL authors="lpr"


FROM amazoncorretto:17-alpine-jdk AS build
WORKDIR /home

# Copy the node executables over to the java image
# since both are required to build the angular API client
COPY --from=node /usr/lib /usr/lib
COPY --from=node /usr/local/lib /usr/local/lib
COPY --from=node /usr/local/include /usr/local/include
COPY --from=node /usr/local/bin /usr/local/bin

RUN npm install -g @angular/cli@17.3.2
COPY . .
RUN npm install
#RUN sed -i s/localhost/back-end/g package.json;
#RUN npm run build-api

EXPOSE 4200
ENTRYPOINT ["ng", "serve", "--host", "0.0.0.0"]
