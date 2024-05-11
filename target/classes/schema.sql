CREATE TABLE IF NOT EXISTS "user" (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
  birthday DATE,
  address VARCHAR(255),
  phone VARCHAR(255),
  deleted BOOLEAN
);