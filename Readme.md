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
```
Once postman is installed:
    1. Open Postman
    2. File -> Import
    3. Search for the postman collection with all the rest calls in the project folder with path:
        3.1.  src/main/resources/postman collection
```


## Comments
- Persistence

    I decided to not link the two entities as there is only the posibility of one class per given day the date is an unique identifier for the booking respect to the class.
    Besides as it ws not necessary to take overbooking into account I let it out.

- Validation

    Validation is done in two different ways, one using Spring validation features and the more complex using custom validation classes.
    Just added some validation for the fields and some constraints to prevent classes overlapping (it is mentioned one class per day) also the bookings are only allowed when the date requested has a class.
    
- Testing
    
    Unit tests have been added for all the classes with logic and I would have like to implement an integration test suite using Postman but given to the lack of time I only added the rest calls to the Postman collection without any flow to test the functionality together.
