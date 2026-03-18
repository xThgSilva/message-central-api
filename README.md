# Message Central API
Message Central API is a RESTful backend built with Spring Boot that handles user authentication, user management and real-time messaging.
It provides secure endpoints using JWT and enables real-time communication through WebSocket integration. 
<br />
This API is designed to be consumed by a React frontend application.
<div align="center">

![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
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
After logging in, your JWT token will be stored in your browser's `sessionStorage` and automatically sent in the request headers in website (see the considerations at the end.). For requests via Postman and Insomnia, you can send your token as follows.
````
Authorization: Bearer your_token_here
````
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
* **Body:**

```json
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

## User Endpoints
### **All router below require authentication**

### 1. GET `/user?page={pageNumber}&size={quantityUsersToReturn}`
List all users

**Request**

* **URL:** `/user?page={pageNumber}&size={quantityUsersToReturn}`
* **Method:** GET
* **Headers:**
    * Authorization: User JWT token

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

* **URL:** `/user{id}`
* **Method:** GET
* **Headers:**
    * Authorization: User JWT token

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
# Message Endpoints
## 1. POST `/message/send`
Send a messsage to user

**Request**

* **URL:** `/message/send`
* **Method:** POST
* **Header:**
  * Content-Type: application/json
  * Authorization: User JWT token
* **Body:**

```json
{
    "senderUserId": 1,  // Who send
    "recipientUserId": 2,   // Who receive
    "content": "Your message"
}
```
**Response**

* **Status Code:** 200 OK
* **Body:**

```json
{
    "content": "Your message here",
    "id": 3,
    "recipientUserId": 2,
    "sendAt": "2026-03-18T12:35:43.2495222",
    "senderUserId": 1
}
```
### Possible errors
**Error - Sender Not Found**

* **Status Code:** 404 Not Found
* **Body:**

```json
{
    "status": 404,
    "error": "Not Found.",
    "message": "Sender not found",
    "timestamp": "2026-03-18T12:38:09.7697814"
}
```
**Error - Recipient Not Found**

* **Status Code:** 404 Not Found
* **Body:**

```json
{
    "status": 404,
    "error": "Not Found.",
    "message": "Recipient not found",
    "timestamp": "2026-03-18T12:38:09.7697814"
}
```
## 2. GET `message/conversation/{senderId}/{recipientId}`
Returns a conversation between 2 users

**Request**

* **URL:** `message/conversation/{senderId}/{recipientId}`
* **Method:** GET
* **Header:**
  * Authorization: User JWT token

**Response**

* **Status Code:** 200 OK
* **Body: (Using senderId = 1 and recipientId = 2)**

```json
[
    {
        "content": "Hello",
        "id": 1,
        "recipientUserId": 2,
        "sendAt": "2026-03-16T15:27:00.790076",
        "senderUserId": 1
    },
    {
        "content": "Hello, how are you?",
        "id": 2,
        "recipientUserId": 1,
        "sendAt": "2026-03-16T15:27:26.03531",
        "senderUserId": 2
    },
    {
        "content": "Your message here",
        "id": 3,
        "recipientUserId": 1,
        "sendAt": "2026-03-18T12:35:43.249522",
        "senderUserId": 2
    }
]
```
# Dependencies
Dependencies used in the project
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
        
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa-test</artifactId>
			<scope>test</scope>
		</dependency>
		
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation-test</artifactId>
			<scope>test</scope>
		</dependency>
		
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
      		<groupId>org.springframework.boot</groupId>
      		<artifactId>spring-boot-starter-security</artifactId>
    	</dependency>

   		<dependency>
      		<groupId>org.springframework.boot</groupId>
      		<artifactId>spring-boot-starter-security-test</artifactId>
      		<scope>test</scope>
    	</dependency>

    	<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>

		<dependency>
    		<groupId>io.jsonwebtoken</groupId>
    		<artifactId>jjwt-api</artifactId>
    		<version>0.11.5</version>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
    		<artifactId>jjwt-impl</artifactId>
    		<version>0.11.5</version>
    		<scope>runtime</scope>
		</dependency>

		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
    		<scope>runtime</scope>
		</dependency>

		<dependency>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-websocket</artifactId>
		</dependency>
```
# Error Response
Error response structure
```json
{
    "status": "Error Code (400, 404, 409...)",
    "error": "Message (Bad Request, Not Found)",
    "message": "Explain the reason for the error.",
    "timestamp": "2026-03-18T12:38:09.7697814 (Error moment)"
}
```
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

# Front-end
See also the project's front-end
[![Access API](https://img.shields.io/badge/Access_API-Front_End-black?style=for-the-badge&logo=springboot)](https://github.com/xThgSilva/message-central-web)

> If you have any questions or suggestions, feel free to open an issue.
