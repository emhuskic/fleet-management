# Car Heartbeat Service

Car Heartbeat Service acts as a bridge between the trip data received from Kafka and the generation of heartbeat messages for cars in the system. It ensures that accurate information about ongoing trips and car states is maintained and disseminated to other parts of the transportation system.

Message Consumption from Kafka Channel "trips": The service listens for incoming messages from the Kafka channel named "trips". These messages typically represent trips taken by cars in the system.

1. **Trip Processing** Upon receiving a trip message, the service processes it to determine whether the trip has started or ended. If the trip has started (i.e., it has a start time but no end time), the service stores the trip information in Redis. If the trip has ended (i.e., it has both start and end times), the service deletes the corresponding trip information from Redis.

2. **Scheduled Heartbeat Generation** The service includes a scheduled method that runs every 10 seconds. This method is responsible for generating a "heartbeat" for each car in the system.

3. **Car State Calculation** During the heartbeat generation process, the service calculates the current state of each car. This state includes parameters such as speed, latitude, and longitude.

4. **Sending Heartbeat via Kafka** After calculating the car state, the service sends this information as a heartbeat message via the Kafka channel named "heartbeat". These heartbeat messages contain the current state of each car.

### Why Redis?
Redis serves as a lightweight datasource which stores active trips. It is used for keeping synchronisation of scheduled heartbeats, as each heartbeat queries active trips. 

### Heartbeat configuration
Heartbeat interval and initial delay can be set in configuration.
Environment variables to set are:
```
heartbeat.interval=10s
heartbeat.delayed=10s
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.


## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/car-heartbeat-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
