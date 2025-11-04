-- Run this in MySQL Workbench or MySQL Shell to reset your database
-- This will delete all old data and allow the application to recreate with Indian cities

USE greenlease_db;

-- Drop all tables in the correct order (to respect foreign key constraints)
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS properties;
DROP TABLE IF EXISTS landlords;
DROP TABLE IF EXISTS users;

-- That's it! Now restart your Spring Boot application
-- It will automatically recreate all tables with Indian city data:
-- - Bangalore (Karnataka)
-- - Chennai (Tamil Nadu)
-- - Vellore (Tamil Nadu)
