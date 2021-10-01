# order-avro-model

###Reference: 
What is avdl & syntax:  https://avro.apache.org/docs/1.8.2/idl.html


###Key tasks in build process:

####generateProtocol:
Generates protocols from avdl files.

####generateSchema: 
Generates avro schema (avsc) file from protocols. This schema would register with kafka schema 
registry.
generateAvro -> generates java pojo objects from schema file(avsc).

###How build and generate java objects?

Local work space: ./gradlew clean build

###How to set up docker on local?

[iOS](https://docs.docker.com/docker-for-mac/) | [Windows](https://docs.docker.com/docker-for-windows/install/)

