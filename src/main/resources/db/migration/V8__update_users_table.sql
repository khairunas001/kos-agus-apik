ALTER TABLE users
MODIFY roles ENUM('admin', 'customers') NOT NULL DEFAULT 'customers';
