#!/bin/bash

ENV_FILE=".private/dev.env"
if [ ! -f "$ENV_FILE" ]; then
   echo "GK> Error: can't find private environment file"
   exit 1
fi
. "$ENV_FILE"

docker-compose stop || exit

pushd services/spring/demo
./mvnw clean package || exit

popd
docker-compose up --build -d

# docker exec -it \
#    tutorial_kafka_container \
#    /opt/kafka/bin/kafka-topics.sh \
#       --bootstrap-server localhost:9092 \
#       --create \
#       --topic "my-topic"

# echo "GK> TOPIC LIST:"
# docker exec -it \
#    tutorial_kafka_container \
#    /opt/kafka/bin/kafka-topics.sh \
#       --bootstrap-server localhost:9092 \
#       --list

