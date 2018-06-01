# demoticketingsystem
A simple ticketing system, written in Java, providing a REST API

## Getting Started

Download or clone the repo.


### Prerequisites

* Java 1.8 or higher
* Gradle


### Installing

To build the software and run its Unit Tests run:
```
cd complete
./gradlew build
```

## Running the tests

To start the REST web server run:
```
cd complete
java -jar build/libs/ticket-rest-service-0.1.0.jar
```

and then in another terminal window you can run your own REST queries (see below) or
run the provided test suite, which takes about 30 seconds:
```sh testsuite.sh```

You can see a sample output of that suite in testsuite.OUTPUT.txt


## Sample usage of API endpoints
* curl -i http://localhost:8080//v1/ticketService/numSeatsAvailable
* curl -i http://localhost:8080//v1/ticketService/showAuditoriumMap 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"
* curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=3&customerEmail=foo@bar.com'
* curl -i http://localhost:8080//v1/ticketService/reserveSeats -X POST -d 'seatHoldId=1&customerEmail=foo@bar.com'
* curl -i 'http://localhost:8080//v1/ticketService/showAuditoriumMap?seatHoldId=1'



## Design notes and highlights

I needed to use Java for this project and I felt that a REST API was the most useful and instructive interface.   So I started with this sample code and iteratively refined it: https://spring.io/guides/gs/rest-service/

As a REST API "true believer" I thought it was important to return 422 HTTP codes for API requests which are invalid, such as attempting to *reserve* a block of seats which haven't been previously *held*.   So I spent a lot of time learning how to generate 4xx codes in the Spring REST API framework.

Producing a human-readable map of the auditorium wasn't a project requirement but was an important design criterion for me.   The *showAuditoriumMap* REST API returns a JSON object which is a string of strings, and is readily converted to human-readable form using the "sed" and "tr" pipeline provided.   This *showAuditoriumMap* API accepts an optional *seatHoldId" parameter, which if provided causes the associated seats to be highlighted using capital letters, somewhat analogously to a real-world theater booking system.

The "scoreSeatQuality" function in SeatHold.java preferentially assigns seats which are "front and center" rather than just at the front of the auditorium.




## Built With

* Spring's REST API
* IntelliJ free edition
