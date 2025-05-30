# IELTS Platform

## About the Project

**IELTS Platform** is a modern, user-friendly online system for managing the educational process, conducting tests, and automating the workflow of language centers specializing in IELTS exam preparation. The platform enables efficient collaboration between administrators, teachers, students, and their parents.

## Key Features

- **User Registration and Management:** Supports roles such as Admin, Teacher, Student, and Parent.
- **Group Assignment and Management:** Create and edit study groups, assign teachers and students.
- **IELTS Test Administration:** Online platform for taking IELTS tests, automatic grading, and performance statistics.
- **Scheduling:** Plan lessons and exams seamlessly.
- **Attendance and Progress Reports:** Detailed analytics for administrators, teachers, and parents.
- **Feedback and Notifications:** Notification and messaging system for all roles.
- **Tuition Payment:** Option for online payment of courses (if needed).

## Architecture

The platform follows a client-server architecture:
- **Backend:** Spring Boot (Java)
- **Frontend:** (optional) React/Vue/Angular
- **Database:** PostgreSQL or MySQL
- **Email/SMS Notifications:** Integration with external services (e.g., SendGrid, Twilio, etc.)

## Getting Started

1. Clone the repository:
    ```bash
    git clone https://github.com/shahob-atm/ielts-platform.git
    ```
2. Navigate to the project folder and configure environment variables (e.g., database parameters).
3. Run the backend application:
    ```bash
    ./mvnw spring-boot:run
    ```
4. (Optional) Start the frontend if you are using one.

## User Roles

- **Administrator:** Manages users, groups, schedules; approves new teachers and students; sends invitations.
- **Teacher:** Conducts lessons and tests, assigns grades, communicates with students.
- **Student:** Takes tests, tracks schedule, receives grades and notifications.
- **Parent:** Has access to their child’s results and attendance, can pay tuition.

## Security

- User registration is by invitation only from the administrator (via email or SMS).
- All passwords are stored in encrypted form (bcrypt).
- JWT authentication is used for the API.
- Data validation and role-based access control are enforced.

## Technologies

- Java 17+
- Spring Boot
- Spring Security (JWT)
- JPA/Hibernate
- PostgreSQL/MySQL
- Lombok
- Swagger/OpenAPI (documentation)
- Email/SMS integration

---

> ⚠️ **Note: This project is under active development. Major changes in functionality and architecture are possible. Use for familiarization or testing purposes only.**

**IELTS Platform** — your reliable tool for efficient IELTS preparation!
