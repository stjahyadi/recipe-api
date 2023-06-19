#!/bin/bash

echo '[+] Building the recipe project'
mvn clean install

echo '[+] Starting local database'
docker-compose up -d

echo '[+] Running the application'
mvn spring-boot:run