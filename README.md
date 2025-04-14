# Simulated Banking Full Stack Project

## Table of Contents
- [Features](#features)
  - [Signup](#signup)
  - [Login](#login)
  - [Account](#account)
  - [Transaction](#transaction)
  - [Transaction History](#transaction-history)
  - [Logout](#logout)
- [Tech Stack](#tech-stack)
  - [Frontend](#frontend)
  - [Backend](#backend)
  - [Authentication](#authentication)
  - [Devtool](#devtool)
- [Setup](#setup)
  - [Backend Requirements](#backend-requirements)
  - [Backend Setup Instructions](#backend-setup-instructions)
  - [frontend Requirements](#frontend-requirements)
  - [frontend Setup Instructions](#frontend-setup-instructions)

# Features

## Signup

Signup by filling in the form and pressing submit. This sends a request to the backend. After verifying uniqueness of username and email, and the completion of all fields, the backend will create a new user and account.

<img src="./Images/signup-interface.png" alt="My Photo" width="600"/>
<img src="./Images/signup-success.png" alt="My Photo" width="600"/>

## Login

Login by entering username and password. The backend will receive this request and validate the password against the hashed password for that username to see if they match. If the password is correct, a http-only cookie will be sent in the response containing a authentication token which is valid for one hour. The user will then be redirected to the account page.

<img src="./Images/login-interface.png" alt="My Photo" width="600"/>

## Account

The account page sends a request to the backend with the authentication token. If it is valid, the backend will retrieve and send basic account information to the client.

<img src="./Images/signin-newaccount.png" alt="My Photo" width="600"/>

<img src="./Images/account-with-history.png" alt="My Photo" width="600"/>

## Transaction

In the interface, only the transaction type, "transfer", will show the field of "recipient username". When submitted, the backend will verify account balances, existence of the recipient, and process the transaction. 

<img src="./Images/Sample-transaction.png" alt="My Photo" width="600"/>

<img src="./Images/transaction-fail.png" alt="My Photo" width="600"/>

## Transaction History

The transaction history page allows users to view all transactions that involve them, including transactions where they are marked as a recipient. It uses pageable, and allows users to change the page, or the page size. It is sorted with the most recent transactions being shown first. Though it only shows calendar date, precise date/time is used.

<img src="./Images/transaction-history-page2.png" alt="My Photo" width="600"/>

## Logout

Logout clears all stored data, including cookies and other locally stored account data. It makes a request to the backend, and the backend responds with an empty http-only authentication token, overwriting the previously stored one. 

<img src="./Images/logout-confirm.png" alt="My Photo" width="600"/>


# Tech Stack

## Frontend
 - Vue 3: Frontend framework (Composition API)
 - Vite: Dev server
 - Vue Router: client-side routing
 - Pinia: State management
 - Axios: Backend communication
 - Tailwind CSS: Styling
 - JavaScript/HTML: Core technologies

## Backend
 - Spring Boot: Java backend framework
 - Spring Web: RESTful API creation
 - PostgreSQL: Relational database
 - Spring Data JPA: Java Persistence API
 - JDBC/Hibernate: Database access with JPA
 - schema.sql: DB blueprint

## Authentication
 - JWT (JSON Web Token): Stateless authentication
 - HTTP-only Cookies: Secure client-side toekn storage

## Devtool
 - Maven: java build tool and dependency management
 - Node.js + npm: Frontend dependency management
 - VS Code: Frontend IDE
 - IntelliJ Idea: Backend IDE
 - PoastgreSQL CLI: DB management

# Setup

## Backend Requirements
 - Java 17+
 - Maven
 - PostgreSQL
   - A postgreSQL database named 'userdb'
   - An environment variable, 'DB_PASSWORD' for the PostgreSQL 'postgres' user


## Backend Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/jmeyer2030/BankingWebApp.git

cd bankingwebapp/banking-backend
```

### 2. Make sure PostgreSQL is running locally, then make the database

```bash
psql -U postgres
CREATE DATABASE userdb;
```

### 3. Set the environment variable
For a session evironment variable:
```bash
$env:DB_PASSWORD = "password"
```
Or create a persistent environment variable:
```bash
[System.Environment]::SetEnvironmentVariable("DB_PASSWORD", "password", "User")
```

### 4. Build and run the backend
In the banking-backend directory
```bash
mvn clean install
mvn spring-boot:run
```

## Frontend Requirements
 - Node.js
 - npm

 ## Frontend Setup Instructions



 ### Install Dependencies
navigate to "bankingwebapp/banking-frontend" and install dependencies
 ```bash
 cd banking-frontend
 npm install
 ```

### Run

 ```bash
 npm run dev
 ```
