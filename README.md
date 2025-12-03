# 📚 E-Library Management System

A modern **HTML/CSS/JS + Java Servlet + JDBC + MySQL** based E‑Library Management System.
This project is designed for academic use and includes both **frontend UI** and **backend server architecture** with database connectivity.

---

# 🚀 Features

## ✅ Frontend (HTML + CSS + JS)

* Modern responsive UI (blue/teal theme)
* Login (mock role switch)
* **Role-based dashboard** (Admin / Student)
* Book management UI (CRUD)
* User management UI (Admin only)
* Borrow history page
* Book details page
* Dynamic tables, modals, notifications, search bars
* Style guide for consistent design

## ✅ Backend (Java Servlets + JDBC)

* Java Servlet API (REST-like JSON endpoints)
* DAO layer (BookDAO, UserDAO)
* JDBC MySQL connection handler
* CRUD Servlets (Books, Users)
* Mock Auth servlet
* WAR‑package ready (Maven)

## ✅ Database (MySQL)

* `books`, `users`, `history` tables
* Foreign key relations
* Sample seed data
* Database user creation and privileges

---

# 📁 Project Structure

```
e-library/
├── frontend/
│   └── index.html              # Paste full UI file here
├── backend/
│   ├── pom.xml                 # Maven build configuration
│   ├── src/main/java/
│   │   └── com/elibrary/
│   │       ├── model/          # Book.java, User.java
│   │       ├── dao/            # DBConnection.java, BookDAO.java, UserDAO.java
│   │       └── servlet/        # BookServlet.java, UserServlet.java, AuthServlet.java
│   └── src/main/webapp/WEB-INF/web.xml
└── sql/
    └── schema.sql              # Create + seed DB
```

---

# 🛠 Requirements

* **VS Code** or IntelliJ / Eclipse
* **Java 11+**
* **Apache Tomcat 9 or 10**
* **MySQL 5.7+ / MariaDB**
* **Maven**

---

# 🏗 Setup Instructions

## 1️⃣ Import Project

1. Extract the project folder.
2. Open the root folder in **VS Code**.

---

## 2️⃣ Setup Database

1. Start MySQL
2. Open `sql/schema.sql`
3. Run the full script:

   * Creates `elibrary` database
   * Creates tables
   * Inserts sample records
   * Creates DB user `elibrary_user`

Update DB credentials in:

```
backend/src/main/java/com/elibrary/dao/DBConnection.java
```

---

## 3️⃣ Build Backend

In VS Code terminal:

```
cd backend
mvn clean package
```

This generates:

```
target/e-library.war
```

---

## 4️⃣ Deploy to Tomcat

1. Copy the WAR file into:

   ```
   tomcat/webapps/
   ```
2. Start Tomcat
3. Open in browser:

   ```
   http://localhost:8080/e-library/static/index.html
   ```

---

# 🔗 Connecting Frontend to Backend

Replace mock JS with real `fetch()` calls to your servlets:

Example (GET all books):

```javascript
const books = await fetch('/e-library/api/books')
                  .then(res => res.json());
```

Example (POST add book):

```javascript
await fetch('/e-library/api/books', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(bookObj)
});
```

---

# 📌 Notes

* Current Java files are placeholders — replace them with full implementations.
* Always secure sensitive DB credentials.
* Add validation + real authentication if using for production.

---

# ❤️ Author & Usage

Created for academic/college project use.
Feel free to modify and expand.

---

If you want, I can:
✅ Fill in **full real Java code** for all DAO + Servlet files
✅ Connect your UI automatically to backend
✅ Generate updated full project ZIP

Just tell me! 🚀
