# Message Central API
Message Central API is a RESTful backend built with Spring Boot that handles user authentication, user management and real-time messaging.
It provides secure endpoints using JWT and enables real-time communication through WebSocket integration. 
<br />
This API is designed to be consumed by a React frontend application.
<div align="center">

![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-black.svg?style=for-the-badge&logo=Eclipse&logoColor=orange)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
</div>

# Technologies
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Token)
- BCrypt
- Maven Project

# Security and Authentication
## Security Technologies
- Spring Security: Security and Authentication Framework
- JWT (JSON Web Token): Stateless tokens for authentication 
- BCryp: For password encryption
- JJWT: Java library for generating and validating tokens

## How to send your token 
After logging in, your JWT token will be stored in your browser's `sessionStorage` and automatically sent in the request headers in website (see considerations below). For requests via Postman and Insomnia, you can send your token as follows.
````
Authorization: Bearer your_token_here
````
### Considerations regarding token security
This API uses JWT (JSON Web Token) for authentication and authorization. When a user successfully logs in, the API generates a signed JWT containing
the user's identity. This token must be sent in subsequent requests using the `Authorization` header with the Bearer scheme: <br />
Authorization: Bearer your_token_here

The backend validates the token on each request, ensuring that only authenticated
users can access protected endpoints.

### Security Considerations
In the current architecture, the JWT is stored on the client side (frontend),
which is responsible for sending it in each request.

The API is stateless, meaning it does not store session data. All authentication
information is contained within the token itself.

For production environments, recommended best practices include:

- Using short-lived access tokens
- Implementing refresh tokens for session renewal
- Storing tokens in `HttpOnly` and `Secure` cookies instead of client-side storage
- Applying proper validation and expiration handling

# Endpoints
**Public routes**, dont'n require authentication

## For User Authentication

### 1. POST `/user/register`
Register a new user in the system and send a welcome email (if email exists)

**Request**
* **URL:** `/user/register`
* **Method:** POST
* **Header:**
  * Content-Type: application/json
* **Body:**

```json
{
    "name": "John",
    "email": "john@email.com",
    "password": "johnpassword123"
}
```
**Response**

* **Status Code:** 200 OK
* **Body:**

```json
{
    "email": "john@email.com",
    "id": 1,
    "name": "John"
}
```

### Possible errors
**Error - Email Already Registered**

* **Status Code:** 409 Conflict
* **Body:**

```json
{
    "status": 409,
    "error": "Conflict.",
    "message": "Email already registered.",
    "timestamp": "2026-03-17T14:58:25.0439757"
}
```

**Error - Invalid password characters**

* **Status Code:** 400 Bad Request
* **Body:**

```json
{
    "status": 400,
    "error": "Bad Request.",
    "message": "The password must be at least 5 characters long",
    "timestamp": "2026-03-17T14:58:49.7228121"
}
```

### 2. POST `/user/login`
Authenticates and return a JWT token

**Request**

* **URL:** `/user/login`
* **Method:** POST
* **Header:**
  * Content-Type: application/json
* **Body:**

```json
{
    "email": "john@email.com",
    "password": "password123"
}
```
**Response**

* **Status Code:** 200 OK
* **Cookie:** `jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...` (HttpOnly, valid for 1 hour)
* **Body:**

```
{
    "message": "Login Succesully",
    "token": "your_jwt_token_here"
}
```

### Possible errors
**Error - Invalid e-mail**

* **Status Code:** 401 Unauthorized
* **Body:**

```json
{
    "status": 401,
    "error": "Unauthorized.",
    "message": "Invalid e-mail.",
    "timestamp": "2026-03-17T15:05:46.4228825"
}
```

**Error - Invalid password**

* **Status Code:** 401 Unauthorized
* **Body:**

```json
{
    "status": 401,
    "error": "Unauthorized.",
    "message": "Invalid password",
    "timestamp": "2026-03-17T15:07:15.7164522"
}
```
--- 

## User Endpoints
### **All router below require authentication**

### 1. GET `/user?page={pageNumber}&size={quantityUsersToReturn}`
List all users

**Request**

* **URL:** `/user?page={pageNumber}&size={quantityUsersToReturn}`
* **Method:** GET
* **Authentication:** JWT token

**Response**

* **Status Code:** 200 OK
* **Body: (Using size = 3 for example)**

```json
{
    "content": [
        {
            "email": "john@email.com",
            "id": 1,
            "name": "John"
        },
        {
            "email": "email2@gmail.com",
            "id": 2,
            "name": "User 2"
        },
        {
            "email": "email3@gmail.com",
            "id": 3,
            "name": "User 3"
        }
    ],
    "empty": false,
    "first": true,
    "last": true,
    "number": 0,
    "numberOfElements": 3,
    "pageable": {
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 3,
        "paged": true,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "unpaged": false
    },
    "size": 3,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "totalElements": 3,
    "totalPages": 1
}
```

### 2. GET `/user/{id}`
Find user by Id

**Request**

* **URL:** `/user{id}}`
* **Method:** GET
* **Authentication:** JWT token

**Response**

* **Status Code:** 200 OK
* **Body: (Using id = 1)**

```json
{
    "email": "john@email.com",
    "id": 1,
    "name": "John"
}
```

### 3. POST `user/delete`
Delete a user account using its own token.

**Request**

* **URL:** `/user/delete`
* **Method:** DELETE
* **Authentication:** User JWT token

**Response**

* **Status Code:** 204 No Content
* **Body:**

```json
```

### 4. PUT `user/update`
Update the user's account details using their own token.

**Request**
* **URL:** `/user/update`
* **Method:** PUT
* **Header:**
  * Content-Type: application/json
  * Authorization: User JWT token
* **Body: (The user only needs to enter the fields they wish to update. In this case, the name and password will be updated.)**

```json
{
    "name": "new name",
    // "email": "john@email.com", (not update)
    "password": "new password"
}
```
**Response**

* **Status Code:** 200 OK
* **Body:**

```json
{
    "email": "john@email.com",
    "id": 1,
    "name": "new name"
}
```
### Possible errors
**Error - Email already exists**

* **Status Code:** 409 Conflict
* **Body:**

```json
{
    "status": 401,
    "error": "Conflict.",
    "message": "Email already registered and cannot be used.",
    "timestamp": "2026-03-17T15:05:46.4228825"
}
```

**Error - Invalid password**

* **Status Code:** 401 Unauthorized
* **Body:**

```json
{
    "status": 401,
    "error": "Unauthorized.",
    "message": "Invalid password",
    "timestamp": "2026-03-17T15:07:15.7164522"
}
```