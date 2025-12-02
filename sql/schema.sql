-- MySQL schema for e-library
CREATE DATABASE IF NOT EXISTS elibrary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elibrary;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  role ENUM('student','admin') NOT NULL DEFAULT 'student'
);

CREATE TABLE books (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(200),
  isbn VARCHAR(50),
  copies INT DEFAULT 1,
  description TEXT
);

CREATE TABLE history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  user_id INT NOT NULL,
  borrow_date DATE NOT NULL,
  return_date DATE DEFAULT NULL,
  status ENUM('borrowed','returned') DEFAULT 'borrowed',
  FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- create a user for the application (example)
CREATE USER IF NOT EXISTS 'elibrary_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON elibrary.* TO 'elibrary_user'@'localhost';
FLUSH PRIVILEGES;

-- seed data
INSERT INTO users (name,email,role) VALUES ('Alice Sharma','alice@example.com','student'),('Dr. Raj','raj@example.edu','admin');
INSERT INTO books (title,author,isbn,copies,description) VALUES
('Intro to Algorithms','Cormen','9780262033848',3,'Algorithmic foundations'),
('Clean Code','Robert C. Martin','9780132350884',2,'A handbook of agile software craftsmanship');
