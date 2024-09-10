#!/bin/bash

. .env
. .private/mongo.env

docker exec -it \
   "${PROJECT_NAME}_mongo_container" \
   mongofiles \
      -d gridfs \
      --host localhost \
      --port 27017 \
      --username rootuser \
      --password rootpass \
      --authenticationDatabase admin \
      get /data/db/temp/458288966_846619737575032_369096978524046278_n.jpg




