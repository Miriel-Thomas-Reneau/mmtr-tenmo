---
# TEnmo: TE Bucks Payment System

TEnmo is a RESTful API server for an online payment service that allows users to transfer "TE bucks" between friends, request funds, and convert TE bucks to USD. The project is built with modern Java and Spring Boot technologies, featuring robust authentication, role-based authorization, and integration with a real-world currency exchange API.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.2+**
	- Spring Web (REST API)
	- Spring Security (JWT-based authentication)
	- Spring Data JDBC (database access)
- **PostgreSQL** (relational database)
- **Maven** (build and dependency management)
- **JUnit 5** (testing)
- **Frankfurter Exchange Rate API** (for real-time EUR/USD rates)
- **Postman** (API testing collection included)

## Features

### User Management & Security

- **User Registration & Login**: Secure registration and login with hashed passwords.
- **JWT Authentication**: All endpoints (except registration/login) require a valid JWT token.
- **Role-Based Access**: Supports both regular users and administrators with different permissions.

### Account & Transfer Functionality

- **Account Balances**: Users can view their TE bucks and USD account balances.
- **Send TE Bucks**: Transfer TE bucks to other registered users (with validation for sufficient funds, no self-transfer, etc.).
- **Request TE Bucks**: Request funds from other users, with approval/rejection workflow.
- **Transfer History**: View all sent, received, and pending transfers.
- **Admin Controls**: Admins can view all transactions and user balances but cannot participate in transfers.

### Currency Conversion

- **TE Bucks to USD**: Users can convert TE bucks to USD (and vice versa) using the current EUR/USD exchange rate from the Frankfurter API.
- **USD Account**: USD balances are held in a separate account and cannot be transferred to other users.

### API & Database

- **RESTful Endpoints**: Well-structured endpoints for all operations (see `design/ApiDesignSheet.txt` for details).
- **PostgreSQL Database**: Schema and test data provided (`database/tenmo.sql`).
- **DAO Layer**: Clean separation of data access using Spring's JdbcTemplate.

### Testing

- **Integration Tests**: DAO and service layer tests using JUnit and a dedicated test database.
- **Postman Collection**: Provided for manual API testing (`postman/tenmo_postman_collection.json`).

## Getting Started

1. **Clone the repository**
2. **Set up PostgreSQL** and run `database/tenmo.sql` to create schema and seed data.
3. **Configure database credentials** in `src/main/resources/application.properties`.
4. **Build and run** the application:
	 ```
	 ./mvnw spring-boot:run
	 ```
5. **Import the Postman collection** and test the API.

## Project Structure

- `src/main/java/com/techelevator/tenmo/` - Main application code
	- `controller/` - REST controllers
	- `dao/` - Data access objects and interfaces
	- `model/` - Domain models and DTOs
	- `security/` - Security configuration and JWT utilities
	- `services/` - Business logic (e.g., currency conversion)
- `src/main/resources/` - Application properties
- `database/` - ERD and SQL scripts
- `design/` - API design documentation
- `postman/` - Postman collection for API testing
- `src/test/` - Integration and unit tests

## License

This project is for educational purposes.

---