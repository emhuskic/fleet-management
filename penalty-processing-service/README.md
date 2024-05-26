# Penalty Processing Service

Penalty Processing Service acts as an intermediary between the Kafka message stream containing car heartbeats, the penalty calculation logic, and the Redis database for storing and retrieving penalty information. 
It provides an API for external clients to query penalty information for specific drivers.

### Why Redis as data source? 
It was required for penalties to be stored in a key-value pair manner (driverId -> penalty), and Redis serves as a lightweight database for such.
> **_NOTE:_**  If this was a production environment - Redis would be setup as DURABLE, for data preservation. 

### Kafka messages 
This service listens for Kafka messages on *heartbeat* channel. 
Heartbeat message should be in form of:
```
{
  "driverId": "UUID",
  "carId": "UUID",
  "speed": "Double",
  "latitude": "Double",
  "longitude": "Double"
}
```

### API Interface
This service produces the result of the penalty calculation for each driver on API endpoint:
```
/penalty/{driverId}
```
The response is in form of:
```
{
    "driverId": "UUID",
    "fine": "Double"
}
```

### Swagger
Swagger UI is available on:
`${APP_PATH}/q/swagger-ui`


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Dev UI is available in dev mode only at http://localhost:8080/q/dev/.

> **_NOTE 2:_**  Swagger UI is available on: http://localhost:8080/q/swagger-ui/


## Packaging and running the application
Building application with Docker Image:
```
quarkus build -Dquarkus.container-image.build=true
```

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.


## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

You can then execute your native executable with: `./target/penalty-processing-service-1.0.0-SNAPSHOT-runner`

