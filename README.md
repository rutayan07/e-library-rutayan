📚 E-Library Management System

A modern full-stack web application for managing books, users, and borrowing operations.

🚀 Overview

The E-Library Management System is a full-stack project built using:

Frontend: HTML, CSS, JavaScript

Backend: Java Servlets, JDBC

Database: MySQL

External APIs: Google Books API / RapidAPI (for extended book details)

The system supports role-based features for Admin and Students and provides a smooth, responsive UI with modern components like modals, toasts, sidebar navigation, and dynamic tables.

🎯 Features
🔹 For Admin

Add, edit, delete books

Manage users

View all borrowing records

Access detailed book information through integrated Book API

Dashboard with KPIs (Total Books, Active Students, Borrow Stats)

🔹 For Students

Browse and search books

View detailed book pages

Borrow books

View personal borrowing history

Clean and responsive UI

🔹 Other Features

API-based book details (thumbnail, pages, price, description)

Modern UI — teal/blue theme, soft shadows, rounded corners

Dynamic modals, toasts, tables, and filtering

Fallback offline mode with 40 books preloaded

REST API backend

Session-based login system

🏗️ Project Structure
e-library/
├── frontend/
│   └── index.html        ← paste your full UI file here
├── backend/
│   ├── pom.xml
│   ├── src/main/java/com/elibrary/
│   │   ├── model/
│   │   │   ├── Book.java
│   │   │   └── User.java
│   │   ├── dao/
│   │   │   ├── DBConnection.java
│   │   │   ├── BookDAO.java
│   │   │   └── UserDAO.java
│   │   └── servlet/
│   │       ├── BookServlet.java
│   │       ├── UserServlet.java
│   │       └── AuthServlet.java
│   └── src/main/webapp/WEB-INF/web.xml
└── sql/
    └── schema.sql
    └──Migration_add_password.sql
    └──migration_history.sql

🗄️ Database Schema (MySQL)
users

| id | name | email | password | role |

books

| id | title | author | isbn | copies | description |

borrowings

| id | userId | bookId | borrowDate | returnDate | status |

🔌 Backend API Endpoints
📘 Books
Method	Endpoint	Description
GET	/api/books	Get all books (with search filters)
POST	/api/books	Add a new book (Admin)
DELETE	/api/books/{id}	Delete book by ID
GET	/api/books/details?isbn=	Get detailed book info (Google Books/RapidAPI)
🔐 Authentication
Method	Endpoint	Description
POST	/api/auth/signup	User registration
POST	/api/auth/login	Login and generate session
POST	/api/auth/logout	Logout user
👥 Users
Method	Endpoint	Description
GET	/api/users	List all users (Admin)
GET	/api/users/{id}	Get user profile
PUT	/api/users/{id}	Update user
🔄 Borrowing
Method	Endpoint	Description
POST	/api/borrow	Borrow a book
PUT	/api/return/{loanId}	Return book
GET	/api/borrowing-history	User borrowing history
⚙️ Setup Instructions
1️⃣ Install Requirements

Java JDK 17+

Apache Maven

MySQL Server

Tomcat or Jetty

2️⃣ Import Database

Run the provided SQL script:

CREATE DATABASE elibrary;
USE elibrary;

-- tables: users, books, borrowings
-- insert sample data (40 books already included)

3️⃣ Configure Database in Backend

In DBConnection.java:

private static final String URL = "jdbc:mysql://localhost:3306/elibrary";
private static final String USER = "root";
private static final String PASS = "yourpassword";

4️⃣ Build & Run Backend
Option A — Jetty
mvn clean package
mvn jetty:run


Backend runs at:

http://localhost:8080

5️⃣ Run Frontend

Just open:

frontend/index.html


or place it inside webapp/.

🎨 UI Components Included

Sidebar navigation

Dashboard cards & KPIs

Modal forms (Add/Edit)

Book detail popup

Notification toasts

Responsive tables

Search & filter bars

Style guide page

📷 Screenshots (placeholders)

Add your screenshots here later

/screenshots/dashboard.png
/screenshots/books.png
/screenshots/book-detail.png
/screenshots/history.png

🧠 API Integration

The system integrates with:

✔ Google Books API

For ISBN-based details:

Description

Thumbnail

Page count

Authors

Publishing info

✔ RapidAPI (Optional)

For price estimation and extended metadata.

If the API fails → Offline Mode automatically loads 40 predefined books.

🤝 Team Contribution

Add names and roles here:

Member	Role
You	Frontend Developer
—	Backend Developer
—	Database Designer
—	Documentation
🏁 Conclusion

This project demonstrates a complete, modern full-stack application with:

REST APIs

JDBC + SQL

Advanced UI/UX

External API integration

Role-based system

Borrow/Return workflow

It is suitable for academic projects, mini projects, and professional portfolio work.
