# Book Catalog API

A simple Spring Boot RESTful API for managing books and categories, built as a personal project to solidify backend development fundamentals using the Spring Boot framework.

---

## Table of Contents
- [Features](#-features)
- [Highlights](#-highlights)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [Running the Application](#-running-the-application)
- [Testing](#-testing)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Contact](#-contact)
- [License](#-license)

---

## ✨ Features
- CRUD operations for books and categories
    - *Note: Category deletion is omitted intentionally. Categories are **archived** (set as inactive) to preserve relationships with existing books.
- Input validation and global exception handling
- Integration and unit tests using JUnit and MockMvc
- RESTful API design
- Layered architecture (Controller, Service, Repository)

## 🚀 Highlights
- ✅ Test-Driven Development (TDD) approach
- ✅ 71 unit and integration tests combined
- ✅ RESTful API with Spring Boot 3.5
- ✅ JPA with H2 (in-memory)

## 🧰 Tech Stack
- Java 21 (or Java 24, confirm your JDK)
- Spring Boot 3.5.3
- Spring Data JPA
- Spring Security (Basic Authentication)
- H2 Database (In-memory) / MySQL (local)
- Hibernate (via JPA)
- RESTful API
- Maven (build tool)
- JUnit 5 & Mockito (testing frameworks)
- Postman (API Testing)

## 🚀 Getting Started
### Prerequisites
- Java 17+
- Maven 3.8+
- Git

### Clone the repository
````
git clone https://github.com/Acrexia07/book-catalog-api.git
cd book-catalog-api
````

## ▶️ **Running the Application**
To run the app using the in-memory H2 DB:
````
mvn spring-boot:run
````
or build and run:
````
mvn clean install
java -jar target/book-catalog-api-0.0.1-SNAPSHOT.jar
````
The app will start at:
http://localhost:8080

Login credentials for testing Basic Auth:
````makefile
Username: acrexia  
Password: dummy
````

## 🧪 Testing
To execute all unit and integration tests:
````
mvn test
````
- Unit and integration tests were written using JUnit 5 and MockMvc.
- API endpoints were manually tested using **Postman** for validation during development.

✅ 71 test cases (service + repository + controller layers + security)

Test coverage includes:
- Successful and failed Book creation
- Book and Category fetching
- Validations
- Exception handling

## API Endpoints
### 📖 Book
| Method | Endpoint                     | Description                        |
|--------|------------------------------|------------------------------------|
| GET    | `/api/books`                 | Get all books                      |
| GET    | `/api/books/{id}`            | Get book by ID                     |
| GET    | `/api/categories/{id}/books` | Get all books by category ID       |
| POST   | `/api/books`                 | Create a new book                  |
| PUT    | `/api/books/{id}`            | Update book by ID                  |
| DELETE | `/api/books/{id}`            | Delete book by ID                  |

### 📚 Category
| Method | Endpoint               | Description                    |
|--------|------------------------|--------------------------------|
| GET    | `/api/categories`      | Get all categories             |
| GET    | `/api/categories/{id}` | Get category by ID             |
| POST   | `/api/categories`      | Create a new category          |
| PUT    | `/api/categories/{id}` | Update category by ID          |


## Project Structure
```
src
├── main
│   ├── java
│   │   └── com.marlonb.book_catalog_api
│   │       ├── controller
│   │       ├── exception
│   │       │   └── custom
│   │       ├── model
│   │       │   ├── dto
│   │       │   └── mapper
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── utils
│   └── resources
└── test
    └── java
        └── com.marlonb.book_catalog_api
            ├── controller
            ├── repository
            ├── security
            ├── service
            └── utils
                ├── test_assertions
                └── test_data
```

## 📃 API Documentation
This project follows a RESTful approach.
All available endpoints, request formats, and sample responses are documented via Postman.

👉 Live Documentation: https://documenter.getpostman.com/view/46122428/2sB34mje2Q

## 📧 Contact
Marlon Barnuevo - marlzestien@gmail.com
Project Link: https://github.com/Acrexia07/book-catalog-api

## 🪪 License
This project is licensed under the [MIT LICENSE](LICENSE).
