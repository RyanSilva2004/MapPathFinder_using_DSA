# Flight Management System

This project is a Flight Management System implemented using various data structures. The system provides the following features:

## Features

### Flight Scheduling
- **Data Structure**: Min Heap
- This feature schedules flights based on their departure times. A min heap is used to ensure that the flight with the earliest departure time can be found quickly. The heap is ordered by departure time, so the root of the heap always contains the next flight to depart.
- **CLI Interaction**: The user can add a flight by entering the flight number, departure time, and destination. The system will then insert the flight into the heap. The user can also request the next flight, and the system will return and remove the flight with the earliest departure time from the heap.
- **Inputs**: Flight number, departure time, destination.
- **Outputs**: Confirmation of flight addition, details of the next flight.

### Seat Reservation
- **Data Structure**: 2D Array
- This feature handles seat reservations for each flight. A 2D array is used to represent the seating arrangement in the airplane (rows and columns), where each element in the array represents a seat.
- **CLI Interaction**: The user can reserve a seat by entering the flight number and the desired seat. The system will then mark the corresponding element in the 2D array as reserved.
- **Inputs**: Flight number, seat number.
- **Outputs**: Confirmation of seat reservation.

### Passenger Information
- **Data Structure**: Binary Search Tree
- This feature stores and retrieves passenger information. A Binary Search Tree is used for this purpose, where the key is the passenger's ID and the value is an object containing the passenger's information. The BST allows for efficient search, insertion, and deletion operations, which are essential for managing passenger information.
- **CLI Interaction**: The user can add a passenger by entering the passenger's ID and details. The system will then insert the passenger's information into the BST. The user can also request a passenger's details by entering the passenger's ID, and the system will search the BST for the passenger's information.
- **Inputs**: Passenger ID, passenger details.
- **Outputs**: Confirmation of passenger addition, requested passenger details.

### Flight Search
- **Data Structure**: Graph
- This feature allows passengers to search for flights between two locations. A graph is used to represent the network of flights, where each node represents an airport and each edge represents a flight.
- **CLI Interaction**: The user can search for a flight by entering the origin and destination. The system will then perform a search on the graph to find the shortest path.
- **Inputs**: Origin, destination.
- **Outputs**: Shortest path from origin to destination.

### Baggage Tracking
- **Data Structure**: Doubly Linked List
- This feature tracks the movement of baggage from check-in to the airplane and then to baggage claim at the destination. A doubly linked list is used to represent this sequence of events, allowing for efficient updates and queries.
- **CLI Interaction**: The user can add a baggage item by entering the baggage ID and the current location. The system will then add the baggage item to the doubly linked list. The user can also request the location of a baggage item by entering the baggage ID.
- **Inputs**: Baggage ID, current location.
- **Outputs**: Confirmation of baggage addition, requested baggage location.
