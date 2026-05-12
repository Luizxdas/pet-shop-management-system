# Pet Shop Management System

> Backend API for a pet grooming shop. Built with Spring Boot, PostgreSQL, and Spring Security, and orchestrated locally using Docker and Docker Compose.

## 🚀 Tech Stack & Architecture

### Core Backend
* **Java 21 & Spring Boot:** Core API framework.
* **PostgreSQL & Spring Data JPA:** Chosen for relational data consistency.
* **Spring Security:** Handles session authentication and role-based access control (`ROLE_ADMIN` vs `ROLE_CUSTOMER`).
* **Architecture:** Monolithic application structure to keep the codebase centralized.

### Infrastructure, Build & Deployment
* **Docker & Docker Compose:** Containerization and local environment automation.
* **GitHub Actions:** Workflow to run unit tests and build the project.

## 📋 System Requirements

### Functional Requirements
1. **User register and login:** * Uses Session Authentication.
2. **Admin account and panel for workers:** * Uses roles (`ROLE_ADMIN`, `ROLE_CUSTOMER`).
   * Uses `@PreAuthorize`.
3. **Homepage with information:** UI design delegated to AI.
4. **Page for tracking of services being done to pet:** * ENUM Status: Waiting, Grooming, Styling, Finishing, Finished/Done.
5. **View and choose services, schedule an appointment:**
   * Choose an available date and time.
   * Select services and preview total price.
   * Option to use pet pick-up and delivery service (includes fee).
   * Validation for date conflicts and lack of staff.
6. **Business Rules:** Appointments can be cancelled up to 24 hours in advance.
7. **Audit trails:** Log staff work with date and time.
8. **Database Strategy:** Use Spring Data JPA and relational PostgreSQL for consistency.

### Non-Functional Requirements
1. The system will follow a monolithic architecture to ensure data consistency.

## 🗄️ Database Schema

```mermaid
erDiagram
    USER ||--o{ PET : owns
    USER ||--o{ APPOINTMENT : books
    USER ||--o{ WORK_LOG : generates
    PET ||--o{ APPOINTMENT : "receives"
    APPOINTMENT ||--|{ APPOINTMENT_SERVICE : includes
    SERVICE ||--o{ APPOINTMENT_SERVICE : "part of"

    USER {
        bigint id PK
        string name
        string email "UK"
        string password
        string role "ROLE_ADMIN, ROLE_CUSTOMER"
    }

    PET {
        bigint id PK
        bigint owner_id FK
        string name
        string species
    }

    SERVICE {
        bigint id PK
        string name "e.g., Bath, Scissoring, Hydration"
        decimal base_price
    }

    APPOINTMENT {
        bigint id PK
        bigint customer_id FK
        bigint pet_id FK
        datetime scheduled_date
        string status "ENUM: Waiting, Grooming, Styling, Finishing, Done"
        boolean pickup_delivery_requested
        decimal delivery_fee
        decimal total_price
    }

    APPOINTMENT_SERVICE {
        bigint id PK
        bigint appointment_id FK
        bigint service_id FK
    }

    WORK_LOG {
        bigint id PK
        bigint staff_id FK
        datetime action_timestamp
        string action_description
    }
