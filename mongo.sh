#!/bin/bash

. .env
. .private/mongo.env

echo $MONGODB_DATABASE

if [[ ! -z $1 ]]; then 
   docker exec -ti \
      "${PROJECT_NAME}_mongo_container" \
      ${1}
   exit $?
fi

docker exec -it \
   "${PROJECT_NAME}_mongo_container" \
   mongosh \
      --username ${MONGO_INITDB_ROOT_USERNAME} \
      --password ${MONGO_INITDB_ROOT_PASSWORD} \
      --host "localhost" \
      --port ${MONGO_PORT} \
      --db ${MONGODB_DATABASE} \
      --authenticationDatabase admin \




