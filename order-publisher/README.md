# order-publisher

This project is to publisher order events to Kafka with avro schema dependency.

###How to spin up local Kafka server & zoom keeper?
Install Docker
[Macbook](https://docs.docker.com/docker-for-mac/install/) || [Windows](https://docs.docker.com/docker-for-windows/install/)

Refer [Docker compose](docker-compose.yml) 
to spin up zookeper, control center, schema-registry, ksqldb, broker instances

Once docker instance is installed succesfully go to your project folder  where 'docker-compose' file exists and run 
'docker-compose up -d' command

blow below urls to see schema-registry, broker and topic details.

[schema-registry](http://localhost:8081/subjects/)
[Kafka Cluster](http://localhost:9021/)


