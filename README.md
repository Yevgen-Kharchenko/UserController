# UserController
## Overview
This User RESTful API allows for managing user information within a system. It supports standard CRUD operations and is ideal for testing user management functionalities. The API leverages an in-memory H2 database initialized with 10 test users (IDs 1-10) to simplify development and testing.

## API Functionality
- Create User: Registers a new user with validation to ensure the user is older than 18 years. Fields include Email, First Name, Last Name, Birth Date, and optional Address and Phone Number.
- Update User: Allows updating specific fields of an existing user or all fields at once.
- Delete User: Supports deleting a user by their ID.
- Search Users: Users can be searched by a range of birth dates, with validation to ensure the 'From' date is before the 'To' date.
  ## Technical Specifications
- Database: Utilizes an H2 in-memory database, pre-populated with 10 test users for immediate use.
- Testing: Comprehensive unit tests cover the code to ensure functionality correctness.
- Logging and Comments: The codebase includes detailed logging and comments to enhance readability and maintainability.
- Error Handling: Robust error handling for RESTful responses ensures clear client communication.

## Swagger UI for API Testing
The API is documented and can be interactively tested using Swagger UI. After launching the application, visit:

#### Copy code to test an endpoint:
    http://localhost:8080/swagger-ui/index.html


### Navigate to the desired API endpoint in the Swagger UI.
- Click the "Try it out" button.
- Input the required parameters.
- Click "Execute" to perform the API request and see the response directly in the browser.
## Running the API
Ensure you have Java and Maven installed. Start the server using the Spring Boot Maven plugin:


#### Copy code
    mvn spring-boot:run
The API will be available at http://localhost:8080.

## API Details and Requirements
Each user entity includes the following fields:

- Email (required): Must match a valid email pattern.
- First Name (required).
- Last Name (required).
- Birth Date (required): The date must be prior to the current date.
- Address (optional).
- Phone Number (optional).
## Functionality specifics:

- Create User: Requires the user to be at least 18 years old, with the age threshold specified in the application properties file.
- Update User: Allows partial or complete updates to user details.
- Delete User: Removes a user record based on user ID.
- Search Users: Fetch users within a specific birthdate range with proper date validation.

This API provides a robust platform for managing user data with full testing, logging, and error handling to ensure a high-quality developer experience.