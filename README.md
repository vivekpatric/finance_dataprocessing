**Project Overview**

This is the backend system for a Finance Dashboard application built using Spring Boot and PostgreSQL. The system manages financial records, users, and roles. It enforces access control based on user roles so that each type of user can only perform the actions they are permitted to do. All endpoints are secured using JWT-based authentication, and passwords are encrypted in the database using BCrypt.

src/main/java/com/finance/dataprocessing/

 model/     -       Entity classes and Enums (database tables)

 repository/  -     JPA Repository interfaces (database queries)

 security/    -     JWT utility, filter, and user details service

 config/       -    Spring Security configuration

 controller/   -    REST API endpoints

 dto/       -       Data Transfer Objects for API responses

exception/    -    Global exception handling

**Role.java**

An enum that defines the three roles available in the system. Each user is assigned exactly one role.

ROLE_VIEWER   →  Can only view the dashboard summary

ROLE_ANALYST  →  Can view records and access full dashboard analytics

ROLE_ADMIN    →  Full access: create, update, delete records and manage users

**Repository  Package**

Contains JPA Repository interfaces. Spring Data JPA automatically generates the SQL for standard operations. Custom database queries are written using JPQL (Java Persistence Query Language).

**UserRepository.java**

Extends JpaRepository which provides built-in methods for save, findById, findAll, and delete. Adds one custom method to look up a user by their username, which is needed during login.

**RecordRepository.java**

Extends JpaRepository for financial records and adds the following custom queries for the dashboard:

•	totalIncome() — Sums all records where type is INCOME

•	totalExpense() — Sums all records where type is EXPENSE

•	totalByCategory() — Groups all records by category and returns the total amount per category

•	findTop10ByOrderByDateDesc() — Returns the 10 most recent records sorted by date

•	monthlyTrends() — Groups records by year and month to show income and expense trends over time

•	weeklyTrends(startDate) — Groups records by date for the last 7 days

**security  Package**

This package handles everything related to authentication and token management.

**JwtUtil.java**

A utility class responsible for generating and validating JWT tokens.

•	generateToken(username) — Creates a signed JWT token valid for 1 hour

•	extractUsername(token) — Reads the username from inside the token

•	validateToken(token) — Returns true if the token is valid and not expired

 Tokens are signed using HMAC-SHA256 with a secret key,
 Token expiry is set to 1 hour from the time of login,
If a token is expired or tampered with, validateToken returns false

**JwtFilter.java**

A filter that runs on every incoming HTTP request before it reaches any controller. It extends OncePerRequestFilter which ensures it runs exactly once per request.
How it works:  It reads the Authorization header from the request. If the header contains a Bearer token and the token is valid, it extracts the username, loads the user's details, and sets the authentication in the Spring Security context. This tells Spring Security that the request is from an authenticated user.

**CustomUserDetailsService.java**

Implements Spring Security's UserDetailsService interface. When Spring Security needs to verify a user during authentication, it calls this service to load the user from the database by username and return their credentials and granted authorities (roles).

**config  Package**

**SecurityConfig.java**

Defines the Spring Security rules for the entire application. This is the central place where access control is configured.

•	/auth/** — Publicly accessible, no token required (for register and login)

•	All other endpoints — Require a valid JWT token

•	Session management is set to STATELESS, meaning no server-side sessions are created

•	The JwtFilter is added before the standard UsernamePasswordAuthenticationFilter

•	Method-level security is enabled using @EnableMethodSecurity which allows @PreAuthorize on individual controller methods

•	Passwords are encoded using BCryptPasswordEncoder

**controller  Package**

Contains the REST controllers that define the API endpoints. Each controller handles a specific area of the system.

AuthController.java  —  /auth

Handles user registration and login. These endpoints are publicly accessible and do not require a token.

POST	/auth/register	Creates a new user account. Password is BCrypt-hashed before saving.

POST	/auth/login	Validates credentials and returns a JWT token if correct.


**RecordController.java**

—  /records

Handles all financial record operations. Access is restricted based on role.

Method	Endpoint	Allowed Roles	Description

POST	/records	ADMIN only	Creates a new financial record

GET	/records	ADMIN, ANALYST	Returns all financial records

PUT	/records/{id}	ADMIN only	Updates an existing record

DELETE	/records/{id}	ADMIN only	Deletes a record

GET	/records/type/{type}	ADMIN, ANALYST	Filters records by INCOME or EXPENSE

**DashBoardController.java**  —  /dashboard

Returns aggregated summary data for the dashboard. All three roles can access this endpoint.

Method	Endpoint	Allowed Roles	Description

GET	/dashboard/summary	ADMIN, ANALYST, VIEWER	Returns full financial summary including totals, category breakdown, recent activity, and trends

UserController.java  —  /users

Allows admins to manage users. Only users with the ADMIN role can access these endpoints.

Method	Endpoint	Allowed Roles	Description

GET	/users	ADMIN only	Returns a list of all registered users

PUT	/users/{id}/role	ADMIN only	Changes the role of a specific user

PUT	/users/{id}/status	ADMIN only	Sets a user as active or inactive


**Role Behaviour**

The system has three roles. Each role represents a different type of user and has a clearly defined set of permissions.

**ADMIN**

The Admin is the person responsible for managing the entire finance system.

Can create financial records (income and expense entries)

Can update existing records if there is an error

Can delete records

Can view all records and filter by type

Can view the full dashboard summary

Can view all registered users

Can change a user's role

Can activate or deactivate a user account

**ANALYST**

The Analyst uses the data entered by the Admin to review and understand financial performance.

Can view all financial records

Can filter records by type (INCOME or EXPENSE)

Can view the full dashboard summary including trends and category totals

Cannot create, update, or delete records

Cannot manage users

**VIEWER**

The Viewer is a read-only stakeholder who only needs to see the overall financial picture.

Can view the dashboard summary (totals, balance, category breakdown, trends)

Cannot view individual transaction records

Cannot create, update, or delete anything

Cannot manage users




