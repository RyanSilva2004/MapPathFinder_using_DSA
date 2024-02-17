# Flight Management System

This is a Flight Management System implemented using various data structures. The system provides the following features:

## Features

### Flight Scheduling
- **Data Structure**: AVL Tree
- This feature allows the system to schedule flights based on their departure times. An AVL tree is used to ensure that the flight with the earliest departure time can be found quickly. The flight number is used as the key, and the flight details as the value.

### Seat Reservation
- **Data Structure**: 2D Array
- This feature handles seat reservations for each flight. A 2D array is used to represent the seating arrangement in the airplane (rows and columns), where each element in the array represents a seat.

### Passenger Information
- **Data Structure**: Hash Table
- This feature stores and retrieves passenger information. A hash table is used for this purpose, where the key is the passenger's ID and the value is an object containing the passenger's information.

### Flight Search
- **Data Structure**: Graph
- This feature allows passengers to search for flights between two locations. A graph is used to represent the network of flights, where each node is an airport and each edge is a flight.

### Baggage Tracking
- **Data Structure**: Doubly Linked List
- This feature tracks the movement of baggage from check-in to the airplane and then to baggage claim at the destination. A doubly linked list is used to represent this sequence of events, allowing for efficient updates and queries.

## CLI Interaction
The system provides a command-line interface for user interaction. The user can add flights, display all flights, find a flight, set the current location, and find flights from the current location.

## Inputs and Outputs
The inputs to the system include flight number, departure time, destination, passenger ID, passenger details, current location, and baggage ID. The outputs from the system include confirmation of flight addition, details of the next flight, confirmation of seat reservation, requested passenger details, shortest path from origin to destination, confirmation of baggage addition, and requested baggage location.
