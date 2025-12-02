# E-Library Management System

This repository contains a frontend prototype and a Java Servlet + JDBC backend scaffold for an E-Library Management System.

## Structure
- frontend/index.html — UI (single file)
- backend/ — Maven Java webapp (servlets, DAOs)
- sql/schema.sql — Database schema and seed data

## Quick start
1. Run the SQL script in `sql/schema.sql` on your MySQL server.
2. Update DB credentials in `backend/src/main/java/com/elibrary/dao/DBConnection.java`.
3. Build backend: `cd backend && mvn clean package`
4. Deploy generated WAR to Tomcat or run via your IDE.
5. Open frontend: `frontend/index.html` or deploy under `webapp/static/index.html`.



## VS Code and Tomcat quick run

1. In VS Code open the project folder.
2. Run the task **Build Backend (Maven)** from Terminal > Run Task.
3. Run the task **Run with Tomcat (mvn tomcat7:run)** to start an embedded Tomcat for development.
4. By default the app will run at `http://localhost:8080/` (the pom plugin uses root path). If you deploy as a WAR under a context path (e.g. `/e-library`), update `API_ROOT` in `frontend/index.html` accordingly.
5. To debug, configure Tomcat to enable JPDA and attach debugger to port 8000, or use the provided attach configuration.
# e-library-rutayan
