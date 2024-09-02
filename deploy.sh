#!/bin/bash

docker compose stop || exit

pushd services/spring/demo
./mvnw clean package || exit
popd

docker compose up \
   --build \
   -d 

   # --remove-orphans \


docker logs -f datalake_spring_container
