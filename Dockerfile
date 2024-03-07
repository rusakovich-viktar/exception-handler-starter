FROM gradle:8.5-jdk-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies

COPY src src

RUN gradle build

RUN gradle publish