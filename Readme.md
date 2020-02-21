# Glofox Studio Technical Test

Technical test based in the creation of two APIs one to manage classes from a studio and another one to manage bookings.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Installing
Once the repository has been cloned move to the main folder of the project, once there:

Run the following command to build the app:
```
    - bash console: ./mvnw clean install
    - CMD: mvnw.cmd clean install
```

The app needs to be run now:
```
    - bash console: ./mvnw spring-boot:run
    - CMD: mvnw.cmd spring-boot:run
```

The app is now running, the endpoints can be found at:
```
localhost:8080/classes
localhost:8080/bookings
```

## Postman

In order to test the app manually Postman is needed. 
It can be downloaded and installed from: https://www.postman.com/downloads/





