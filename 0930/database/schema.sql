-- Run once on your own MySQL server. Existing data is not dropped.
CREATE DATABASE IF NOT EXISTS springdb CHARACTER SET utf8mb4;
USE springdb;
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user'
) CHARACTER SET utf8mb4;
