#!/bin/bash

ENV_FILE=".private/host.env"
if [ ! -f "$ENV_FILE" ]; then
   echo "GK> Error: can't find private environment file"
   exit 1
fi
. "$ENV_FILE"

docker compose stop || exit

pushd services/spring/demo
./mvnw clean package || exit

popd
docker compose up --build -d

docker logs -f datalake_spring_container
