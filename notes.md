

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

# Secondary kafka, consume
docker exec -it \
   tutorial_kafka2_container \
   /opt/kafka/bin/kafka-console-consumer.sh \
      --bootstrap-server 192.168.32.2:9092 \
      --topic "my-topic" \
      --from-beginning

# This one receives only its own messages

# Secondary produce
docker exec -it \
   tutorial_kafka2_container \
   /opt/kafka/bin/kafka-console-producer.sh \
      --bootstrap-server tutorial_kafka_container:9092 \
      --topic "my-topic"
# This one sends messages locally


# python container
docker exec -it \
   tutorial_python_container \
   python3

```



```python

from kafka import KafkaProducer

producer = KafkaProducer(
   bootstrap_servers='192.168.32.2:9092',
   # ssl_cafile='cluster-ca-certificate.pem',
   # security_protocol='SASL_SSL',
   # sasl_mechanism='SCRAM-SHA-256',
   # sasl_plain_username='[USER NAME]' ,
   # sasl_plain_password='[USER PASSWORD]',
)


producer.send('my-topic', b'test')
producer.flush()
print('Published message')


# ----

from kafka import KafkaConsumer

consumer = KafkaConsumer(
   'my-topic',
   bootstrap_servers='tutorial_kafka_container:9092',
   # ssl_cafile='cluster-ca-certificate.pem',
   # security_protocol='SASL_SSL',
   # sasl_mechanism='SCRAM-SHA-256',
   # sasl_plain_username='[USER NAME]' ,
   # sasl_plain_password='[USER PASSWORD]',
   # auto_offset_reset='earliest',
   group_id='my-group'
)


try:
   for message in consumer:
      if message:
         print(f"Received message: {message.value.decode('utf-8')}")
except Exception as e:
    print(f"An exception occurred: {e}")
finally:
    consumer.close()

```


```py
from kafka import KafkaProducer
from kafka import KafkaConsumer

host_ip='192.168.48.4'
host_port='9092'
topic_name='my-topic'
bootstrapServer=f'{host_ip}:{host_port}'

producer = KafkaProducer(
   bootstrap_servers=bootstrapServer,
)

producer.send(topic_name, b'test')
producer.flush()

# print('Published message')

consumer = KafkaConsumer(
   topic_name,
   bootstrap_servers=bootstrapServer,
   group_id='my-group'
)


try:
   for message in consumer:
      if message:
         print(f"Received message: {message.value.decode('utf-8')}")
except Exception as e:
    print(f"An exception occurred: {e}")
finally:
    consumer.close()

```

After the python container is deployed
try to connect with kafka through python from different containers
If that does not work, consider using zookeeper?


pip install kafka-python


nc -zv 192.168.48.4 9092


Commands for `confluentinc/cp-kafka:7.4.4`
```bash

docker logs tutorial_kafka_container |  grep -i started

# Producer
docker exec -it \
   tutorial_kafka_container \
   kafka-console-producer \
      --bootstrap-server tutorial_kafka_container:9092 \
      --topic "my-topic"

# Consumer
docker exec -it \
   tutorial_kafka_container \
   kafka-console-producer \
      --bootstrap-server tutorial_kafka_container:9092 \
      --topic "my-topic"

```
