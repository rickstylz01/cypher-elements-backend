# Event Management Backend

## Project Description
The Event Management Backend is a robust and secure API developed using Spring Boot. It offers a comprehensive solution for managing events and attendees. Users can register, log in, create events, manage participants, and perform various operations on events and attendees. The API ensures security through JSON Web Tokens (JWT) for authentication and authorization.

Whether you're organizing conferences, workshops, or social gatherings, the Event Management Backend simplifies the process of managing events and attendees, allowing you to focus on creating memorable experiences.

## Table of Contents
- [Features](#features)
- [Project Approach](#project-approach)
- [Entity-Relationship Diagram (ERD)](#entity-relationship-diagram-erd)
- [Model View Controller Design (MVC)](#model-view-controller-design-mvc)
- [REST API Endpoints](#rest-api-endpoints)
- [Agile Development](#agile-development)
- [Tools and Technologies Used](#tools-and-technologies-used)
- [Hurdles Encountered During Development](#hurdles-encountered-during-development)
- [Installation Instructions](#installation-instructions)
- [Resources & Acknowledgements](#resources--acknowledgements)
- [Contributors](#contributors)

## Features
- User Authentication and Authorization
    - Register new users.
    - Login with email and password.
    - JWT-based authentication for enhanced security.
- Event and Participant Management
    - Create, update, and delete events.
    - Add and remove participants from events.
    - View participants of specific events.

## Project Approach
The Event Management Backend project follows a systematic and user-centric approach to deliver a seamless event management experience. Key components of my approach include:

### User-Centric Design
- Detailed user stories were crafted to capture essential user interactions.
- Features were designed with a focus on user experience and ease of use.

### Entity-Relationship Diagram (ERD)
- An ERD was created to design the database structure effectively.
- Relationships between entities like users, events, and participants were clearly defined.

### Model View Controller Design (MVC)
- Follows the MVC architecture for separation of concerns.
- Requests are routed through controllers, processed by services, and interact with the database through repositories.

## REST API Endpoints
- **User Management:**
    - `POST /auth/users/register/`: Registers a new user with user data.
    - `POST /auth/users/login/`: Logs a user in.

- **Event Management:**
    - `POST /api/events/`: Creates a new event.
    - `GET /api/events/`: Gets all events.
    - `GET /api/events/{eventId}/`: Gets event details by ID.
    - `PUT /api/events/{eventId}/`: Updates an event by ID.
    - `DELETE /api/events/{eventId}/`: Deletes an event by ID.
    - `POST /api/events/{eventId}/participants/{participantId}/`: Adds a participant to an event.
    - `DELETE /api/events/{eventId}/participants/{participantId}/`: Removes a participant from an event.

## Agile Development
- Agile methodologies were adopted for rapid development and adaptability.
- Project divided into phases with specific objectives.

## Tools and Technologies Used
- **Spring Boot**: Version 2.7.16 for backend development.
- **Maven**: Build automation tool and dependency management.
- **Spring Security**: Ensures data security and access control.
- **JWT Tokens**: Provides authentication and authorization.
- **H2 Database**: Manages data storage during development.
- **Cucumber JVM**: Testing framework for Behavior Driven Development.
- **GitHub**: Version control and collaboration platform.

## Hurdles Encountered During Development
- **Cucumber Testing Step Definitions**: Overcame challenges by invalidating caches in IntelliJ.
- - **Data Transfer Objects (DTO)**: Implemented DTO to transfer data and avoid a circular reference.
- **Testing Public Endpoints**: Implemented effective methods to test public endpoints.

## Installation Instructions
1. **Install Maven**: Ensure Maven is installed on your system.
```bash
   mvn -v 
   ```
   If not installed, download and install it from the [official Apache Maven website](https://maven.apache.org/download.cgi).

2. **Navigate to Project Directory**: Go to the project's root directory where `pom.xml` is located.

3. **Install Dependencies**: Run the following Maven command to download and install project dependencies.
```bash
   mvn clean install
   ```

## Resources & Acknowledgements
### General Assembly Instructors
- Suresh Sigera: [GitHub](https://github.com/sureshSigera)
- Dhrubo Chowdhury: [GitHub](https://github.com/DhruboChowdhury)
- Leonardo Rodriguez: [GitHub](https://github.com/LeonardoRMR)

### Links:
- [JSON Authentication Token](https://jwt.io/)
- [Spring Framework Documentation](https://spring.io/projects/spring-framework)
- [MVN Repository](https://mvnrepository.com/)

## Contributors
- **Rick Maya**: [GitHub](https://github.com/RickMMaya) | [LinkedIn](https://www.linkedin.com/in/rickmaya/)
