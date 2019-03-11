FROM openjdk:8-alpine

RUN apk update && apk add bash

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY target/assetmanager.jar $PROJECT_HOME/assetmanager.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://asset-mongo:27017/assets", "-Djava.security.egd=file:/dev/./urandom","-jar","./assetmanager.jar"]