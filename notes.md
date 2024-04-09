

application.properties
```
# Enable tomcat access logs
# https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html
server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.suffix=.log
server.tomcat.accesslog.prefix=access_log
server.tomcat.accesslog.file-date-format=.yyyy-MM-dd
server.tomcat.basedir=tomcat
server.tomcat.accesslog.directory=logs


# Spring Boot will write to the console and also to a log file called `spring.log`, at the path you specify.
logging.file.path=.             # write logs to the current directory
logging.file.path=/home/logs    # write logs to /home/logs
logging.file.path=/mnt/logdir   # write logs to /mnt/logdir

# Write to a specific file
logging.file.name=myapp.log

```



```bash

# List the topics
docker exec -it \
   tutorial_kafka_container \
   /opt/kafka/bin/kafka-topics.sh \
      --bootstrap-server localhost:9092 \
      --list


# Create a topic
docker exec -it \
   tutorial_kafka_container \
   /opt/kafka/bin/kafka-topics.sh \
      --bootstrap-server localhost:9092 \
      --create \
      --topic "my-topic"

# Consume a topic
docker exec -it \
   tutorial_kafka_container \
   /opt/kafka/bin/kafka-console-consumer.sh \
      --bootstrap-server localhost:9092 \
      --topic "my-topic" \
      --from-beginning

# Produce messages
docker exec -it \
   tutorial_kafka_container \
   /opt/kafka/bin/kafka-console-producer.sh \
      --bootstrap-server localhost:9092 \
      --topic "my-topic" \






```