#!/bin/bash

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

