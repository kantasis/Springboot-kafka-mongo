# TODO:
network naming
post request
request the data
pagination
datatype of the incoming document

# CRUD

GET /api/data           getAllData(query): returns all records in the collection [retrieve]
GET /api/data/{id}      getData(id): returns a specific document [retrieve]
POST /api/data          createData(data) creates a document [create]
PUT /api/data/{id}      udpateData(id, data) edit a document [update]
DELETE /api/data/{id}   deleteData(id) remove record with id [delete]
DELETE /api/data/       deleteAllData() remove all records [delete]


```bash
docker exec -i \
   datalake_mongo_container \
   mongosh \
      --username rootuser \
      --password rootpass \
<<EOF
use datalake_db;
db.dynamic_collection.find()
db.dynamic_collection.drop()
EOF

```

```java

   template.updateFirst(
      query(
         where("name").is("Joe")
      ), 
      update("age", 35), 
      Person.class
   );

```

## Bezcoder:
https://www.bezkoder.com/spring-boot-mongodb-crud/
https://github.com/bezkoder/spring-boot-data-mongodb

Methods	Urls	Actions
POST	/api/tutorials	create new Tutorial
GET	/api/tutorials	retrieve all Tutorials
GET	/api/tutorials/:id	retrieve a Tutorial by :id
PUT	/api/tutorials/:id	update a Tutorial by :id
DELETE	/api/tutorials/:id	delete a Tutorial by :id
DELETE	/api/tutorials	delete all Tutorials
GET	/api/tutorials/published	find all published Tutorials
GET	/api/tutorials?title=[keyword]	find all Tutorials which title contains keyword









# Auth 

User -> [authentication filter] <-> Authentication Manager <-> Authentication Provider <-> User Details Service
                                 -> Security Context
## bezcoder
https://www.bezkoder.com/spring-boot-jwt-auth-mongodb/

localhost:8080/api/content/all
localhost:8080/api/content/user
localhost:8080/api/content/mod
localhost:8080/api/content/admin

localhost:8080/api/auth/signin
localhost:8080/api/auth/signup


### Revisit

```bash
ENDPOINT="http://localhost:8080"

# Request a public resource
curl \
   --request GET \
   --header "Content-Type: application/json" \
   "${ENDPOINT}/api/content/all" \
   --data "55555"


# Request a public resource advanced
curl \
   --request GET \
   --header "Content-Type: application/json" \
   "${ENDPOINT}/api/content/all" \
   --data @- <<EOF
{
   "username": "george",
   "email": "asdf@asdf.com",
   "password": 1234567,
   "roles": ["user"]
}
EOF

# Register
curl \
   --request POST \
   --header "Content-Type: application/json" \
   "${ENDPOINT}/api/auth/signup" \
   --data @- <<EOF
{
   "username": "george",
   "email": "asdf@asdf.com",
   "password": 1234567,
   "roles": ["user"]
}
EOF

# Login
curl \
   --request POST \
   --header "Content-Type: application/json" \
   "${ENDPOINT}/api/auth/signin" \
   --data @- \
   << EOF | python3 -c "import sys, json; print(json.load(sys.stdin)['token'])"
{
    "username": "george",
    "password": "1234567"
}
EOF
# The process does not end here. This will give you a token which you need to put in the header like this:
TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnZW9yZ2UiLCJpYXQiOjE3MTM1MTMyMjUsImV4cCI6MTcxMzU5OTYyNX0.4xxCwDd-G5DphPn7LTjfTHJi1UG-4I0QBjvTYU8AnUc

# Request a private resource
curl \
   --header "Content-Type: application/json" \
   --header "Authorization: Bearer ${TOKEN}" \
   --request GET \
   "${ENDPOINT}/api/content/user"

# Parse JSON response
curl \
   --request GET \
   --header "Content-Type: application/json" \
   "${ENDPOINT}/api/content/all" \
   --data @- \
   << EOF | python3 -c "import sys, json; print(json.load(sys.stdin)['password'])"
{
    "username": "george",
    "password": "1234567"
}
EOF

```

### ERROR:
THE NAME OF THE VARIABLE MATTERS:
this one costed me 4 days of search
```java
   private RoleEnum name;
   private RoleEnum name;
```

### snippets
```bash
docker exec -i \
   datalake_mongo_container \
   mongosh \
      --username rootuser \
      --password rootpass \
<<EOF
use datalake_db;
db.roles.insertMany([
   { name: "ROLE_ADMIN" },
   { name: "ROLE_MODERATOR" },
   { name: "ROLE_USER" },
])
EOF

docker logs -f tutorial_spring_container

db.roles.drop()

use datalake_db;
db.data.find()
db.roles.find()
db.users.find()
```



Register some users with /signup api

```json
{
   "username": "george",
   "email": "asdf@asdf.com",
   "password": 12345,
   "roles": ["user"]
}
```




## amigo
https://www.youtube.com/watch?v=KxqlJblhzfI
https://github.com/ali-bouali/spring-boot-3-jwt-security


> Bezcoder has a JPA authentication tutorial which may be good too
https://www.bezkoder.com/spring-boot-jwt-authentication/

> Parameter 0 of constructor in com.example.demo.security.ApplicationConfig required a bean of type 'com.example.demo.security.UserRepository' that could not be found.

## rytis-codes-auth
> These videos use deprecated libraries
https://github.com/rytis-codes/spring-security



## teddy-smith-auth
https://www.youtube.com/watch?v=GjN5IauaflY&list=PL82C6-O4XrHe3sDCodw31GjXbwRdCyyuY&ab_channel=TeddySmith





# mongo 
https://www.baeldung.com/spring-data-mongodb-tutorial

```bash
docker exec -it \
   datalake_mongo_container \
   mongosh \
      --username rootuser \
      --password rootpass


docker exec -it \
   tutorial_kafka_container \
   kafka-topics \
      --bootstrap-server localhost:9092 \
      --list

docker logs -f tutorial_spring_container
# 66191a1a5f89af0846895208
# mongodb://<credentials>@127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.2.2

docker exec -i \
   tutorial_mongo_container \
   mongosh \
      --username rootuser \
      --password rootpass \
<<EOF
use datalake_db;
db.dataModel.find();
EOF

```

```js
use datalake_db;
show collections;
db.dataModel.find();
```
echo 'show databases' | 



# kafka
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

producer.send(topic_name, b'ASDFASDFASDF')
producer.flush()

# print('Published message')

consumer = KafkaConsumer(
   topic_name,
   bootstrap_servers=bootstrapServer,
   group_id='my-group'
)

consumer.topics()


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


KAFKA_LISTENERS: 
   EXTERNAL_SAME_HOST://:29092
   INTERNAL://:9092
KAFKA_ADVERTISED_LISTENERS: 
   EXTERNAL_SAME_HOST://localhost:29092
   INTERNAL://kafka:9092
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP:
   EXTERNAL_SAME_HOST:PLAINTEXT
   INTERNAL:PLAINTEXT

http://localhost:8080/send?message=1313131313


```xml
<dependency>	
    <groupId>org.springframework.data</groupId>	
    <artifactId>spring-boot-starter-data-mongodb</artifactId>	
    <version>2.7.11</version>	
</dependency>
```