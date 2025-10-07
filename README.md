# ShareIt - sharing service

ShareIt is a multimodule backend service developed with layered architecture, developed as part of the Yandex Practicum - Java developer program. The application allows users to share items, send booking requests, and manage approvals, forming a social rental / sharing system.

---

## Technologies Used

- **Java 21**
- **Spring Boot** – framework  
- **Spring Data JPA** – Persistence layer abstraction  
- **Hibernate** – ORM for relational database mapping  
- **H2 / PostgreSQL** – Databases for development and production  
- **Bean Validation** – Input validation  
- **JUnit 5** – Unit and integration testing  
- **Mockito** – Mocking framework for service-level testing
- **MapStruct** - Implementation of mappings
- **Maven** – Project modules and dependency management  
- **Docker / Docker Compose** – Containerized local deployment  

---

## Features

- **User Management**
  – CRUD operations
  - Input validation for user data (e.g., email, name)
  - Exception handling for invalid input or missing users
  
- **Item Management**
  - CRUD operations
  - Search items by name or description  
  - Retrieve items by owner ID  
  - Add comments to items after use

- **Booking System**
  - Create a booking – users can request to rent an item for specific dates
  - Get detailed booking information for users and owners
  - Confirm or reject a booking – item owners approve or decline requests  
  - Retrieve all bookings for a user – users see their bookings  
  - Retrieve all bookings for items owned by a user – owners view bookings on their items  
 
- **Item Request Management**
  - Create an item request – users can submit a request for an item they wish to borrow  
  - View own requests – users can see their requests with responses  
  - View all requests – users can browse requests made by others  
  - View details of a specific request including responses  

