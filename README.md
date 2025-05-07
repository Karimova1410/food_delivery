# Food Delivery Backend System

## Overview
This project is a **backend system** for a **Food Delivery Application** built using the **Spring Framework**. upports user authentication, restaurant management, menu management, and order processing with JWT and OAuth2 (Google/GitHub) authentication. The system is designed to be scalable, secure, and well-documented, making it easy to integrate with frontend applications.

---

## Features
1. **User Management**:
   - Email/password registration and login
   - Social login via Google and GitHub
   - JWT token authentication
   - Role-based access (ADMIN, CLIENT, RESTAURANT, COURIER)
   - Profile updates.
   - View order history.

2. **Restaurant Management**:
   - Admin can add, update, or delete restaurants.
   - Users can view a list of restaurants and their details.

3. **Menu Management**:
   - Admin can add, update, or delete menu items.
   - Users can view menus and filter items by category.

4. **Order Management**:
   - Users can place orders and track their status.
   - Admin can update order status (e.g., created, processing, delivering).

5. **API Documentation**:
   - Comprehensive API documentation using **OpenAPI** and **SwaggerUI**.

---

## Technologies Used
- **Backend Framework**: Spring Boot
- **Database**: H2 (for development), PostgreSQL (for production)
- **API Documentation**: OpenAPI with SwaggerUI
- **Testing**: JUnit, Mockito, MockMVC
- **Data Validation**: Hibernate Validator
- **Exception Handling**: Spring’s `@ControllerAdvice`
- **Spring Security**
- **JWT & OAuth2**

---
### SecurityConfiguration Class
The main security configuration handles:
- JWT authentication filter
- OAuth2 social login
- Role-based authorization
- CORS configuration
  
---

## Database Schema
The database consists of the following tables:
1. **User**: `id`, `name`, `email`, `password`, `role`
2. **Restaurant**: `id`, `name`, `address`
3. **Menu**: `id`, `restaurant_id`, `name`, `price`, `description`, `category`
4. **Order**: `id`, `user_id`, `restaurant_id`, `status`, `dishes`, `courier_id`
5. **RefreshToken**: `id`, `user_id`, `token`, `expiry_date`

---

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL (for production)
- H2 Database (for development)

### Steps to Run the Project
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Karimova1410/food_delivery.git
   cd food_delivery
   ```

2. **Configure the Database**:
   - For development, the application uses the **H2 database** by default. No additional configuration is required.
   - For production, update the `application-prod.properties` file with your PostgreSQL credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/food_delivery
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
   - The application will be running at `http://localhost:8080`.
   - Access the **SwaggerUI** documentation at `http://localhost:8080/swagger-ui.html`.

---

## Testing
The project includes **unit tests**, **integration tests**, and **exception handling tests**. To run the tests, use the following command:
```bash
mvn test
```

---

## API Documentation
The API is documented using **OpenAPI** and **SwaggerUI**. You can access the documentation by running the application and navigating to:
```
http://localhost:8080/swagger-ui.html
```

---

## Contributing
Contributions are welcome! If you'd like to contribute, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes with descriptive messages.
4. Submit a pull request.

---

## Contact
For any questions or feedback, please contact:
- **Name**: Karimova Roza
- **GitHub**: [Karimova1410](https://github.com/Karimova1410)
