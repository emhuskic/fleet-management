# Fleet Tracking System

The Fleet Tracking System is the core system responsible for overseeing the operation and maintenance of a fleet of vehicles. It includes functionalities such as vehicle tracking, maintenance scheduling, driver management, trip planning, and reporting.

It consists of 3 microservices:
- Fleet Management Service
     - It facilitates the creation, updating, and deletion of records for cars and drivers while also handling trip management activities (such as creating, starting, stopping trips) and broadcasting trip events via Kafka messaging (channel: trips). **More info on this service can be found in `fleet-management-service/README.md`**
- Car Heartbeat Service
    -  Car Heartbeat Service acts as a bridge between the trip data received from Kafka and the generation of heartbeat messages for cars in the system. **More info on this service can be found in `car-heartbeat-service/README.md`**
- Penalty Processing Service
    - Penalty Processing Service acts as an intermediary between the Kafka message stream containing car heartbeats, the penalty calculation logic, and the Redis database for storing and retrieving penalty information. It provides an API for external clients to query penalty information for specific drivers. **More info on this service can be found in `penalty-processing-service/README.md`**

Each of the services communicate with each other via Kafka messages.

Entrypoint to the system is the `fleet-management-service` API.

## Running Fleet Tracking System
### Dockerized
In this folder a **docker-compose.yml** file can be found. 
This file contains setup for:
- ZooKeeper
- Kafka
- PostgreSQL
- Redis
- Fleet Management Service
- Car Heartbeat Service
- Penalty Processing Service

#### Prerequisites
Docker installed and running on the machine.

#### Build & Run
To build and run the application, run:
```
docker-compose up --build
```

### Running locally
#### Prerequisites
- Java 21 (for building locally)
- Quarkus CLI (see https://quarkus.io/guides/cli-tooling)

To start running locally all three microservices, navigate to all three directories: `fleet-management-service`, `penalty-processing-service`, `car-heartbeat-service`, and run:
```
quarkus dev
```
in each of the directories.

To build application with creation of Docker image, run (in each service directory):
```
 quarkus build -Dquarkus.container-image.build=true
```

To run tests, run (in each service directory):
```
./mvnw clean verify
```

## Swagger API
There is a Swagger API integrated with `fleet-management-service` and `penalty-processing-service`, so when the services are up and running, you can see the available API endpoints on ports 8080 and 8083.
`http://localhost:8083/q/swagger-ui/#/` - Penalty Processing Service Swagger UI
`http://localhost:8080/q/swagger-ui/#/`- Fleet Management Service Swagger UI

## Postman Collection
A happy path testing Postman collection can be found under root directory.
File: `fleet-tracking-system-collection.postman_collection.json`

## TODOs
- Custom Exceptions & exception handling
- 100% test coverage on all servicess
