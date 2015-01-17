# PDB [![Build Status](https://travis-ci.org/VictorEDA/pdb.svg?branch=master)](https://travis-ci.org/VictorEDA/pdb)

## Key Technologies
- Java 8
- Jersey 2 REST Server/Client (JAX-RS 2.0)
- Maven
- Cassandra
- Spring (for configuration)

## Requirements
### Ubuntu
- Java 8
  - `apt-get install -y software-properties-common`
  - `add-apt-repository ppa:webupd8team/java`
  - Press ENTER
  - `apt-get update -qq`
  - `apt-get install -y oracle-java8-installer`
  - Click Ok and Yes
  - Update `JAVA_HOME` environment variable to point to install directory, like: `JAVA_HOME=/usr/lib/jvm/java-8-oracle/`
- Maven
  - `apt-get install maven`

## Run Tests
`mvn test`

## Deploy
`mvn install -DskipTests`
`mvn -pl server tomcat7:run -DskipTests`

This runs embedded Tomcat server on port 8080.
