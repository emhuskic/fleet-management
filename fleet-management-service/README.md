# Fleet Management Service

Fleet Management Service provides comprehensive functionality for managing cars, drivers, and trips within a fleet. It facilitates the creation, updating, and deletion of records for cars and drivers while also handling trip management activities (such as creating, starting, stopping trips) and broadcasting trip events via Kafka messaging (channel: trips).

### Database
Database used for storing Fleet data is PostgreSQL. This allows us to create relations between Drivers & Cars through Trip entities. All Car & Driver can be soft deleted, as we would want to keep history of Trips even after they have been removed from the system.

### Swagger
All API documentation can be found on:
`${APP_PATH}/q/swagger-ui`

## Happy Path usage
The simple creation of Car, Driver and Trip with Starting and Stopping is explained below.

### Create a Car
```
POST /cars
{
  "licensePlate": "ABC12",
  "model": "XYZ",
  "color": "blue",
  "manufacturer": "Toyota"
}

```
Response is in form of:
```
{
    "id": "b6a8d0ec-bdb4-4ebb-80de-91bd8f4fac03",
    "licensePlate": "ABC12",
    "model": "XYZ",
    "color": "blue",
    "manufacturer": "Toyota",
    "deleted": false
}
```


### Create a Driver
```
POST /drivers
{
    "firstName": "Jane",
    "lastName": "Doe",
    "driversLicenseNo": "D1234567",
    "dateOfBirth": "1980-01-01",
    "deleted": false
}
```
Response is in form of:
```
{
    "id": "cd34d88f-692b-4368-af64-25e81a58462d",
    "firstName": "Emina",
    "lastName": "Doe",
    "driversLicenseNo": "D1234567",
    "dateOfBirth": "1980-01-01",
    "deleted": false
}
```

### Create a Trip
```
POST /trips
{
    "carId": "cd34d88f-692b-4368-af64-25e81a58462d",
    "driverId": "cd34d88f-692b-4368-af64-25e81a58462d"
}
```
Response is in form of:
```
{
    "id": "7ef15468-b1c4-46a8-8985-a256a512f9e7",
    "driver": {
        "id": "085b6a3f-2fe9-445e-8cbf-54525b897ec9",
        "firstName": "Emina",
        "lastName": "Doe",
        "driversLicenseNo": "D1234567",
        "dateOfBirth": "1980-01-01",
        "deleted": false
    },
    "car": {
        "id": "dadb66dc-7de2-4e87-b295-c862acb2718f",
        "licensePlate": "ABC12",
        "model": "XYZ",
        "color": "blue",
        "manufacturer": "Toyota",
        "deleted": false
    },
    "startTime": null,
    "endTime": null
}
```

### Start a Trip
```
POST /trips/start
{
    "carId": "cd34d88f-692b-4368-af64-25e81a58462d",
    "driverId": "cd34d88f-692b-4368-af64-25e81a58462d"
}
```
OR
```
POST /trips/start
{
    "tripId": "cd34d88f-692b-4368-af64-25e81a58462d"
}
```
This will invoke sending a Kafka message to start tracking this trip.

### Stop a Trip
```
POST /trips/stop
{
    "carId": "cd34d88f-692b-4368-af64-25e81a58462d",
    "driverId": "cd34d88f-692b-4368-af64-25e81a58462d"
}
```
OR
```
POST /trips/stop
{
    "tripId": "cd34d88f-692b-4368-af64-25e81a58462d"
}
```
This will invoke sending a Kafka message to stop tracking this trip.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

> **_NOTE:_**  Swagger UI is available on: http://localhost:8080/q/swagger-ui/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/fleet-management-service-1.0.0-SNAPSHOT-runner`
