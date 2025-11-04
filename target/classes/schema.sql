-- GreenLease Database Schema
-- MySQL/PostgreSQL compatible SQL script

-- Create database (MySQL)
-- CREATE DATABASE IF NOT EXISTS greenlease_db;
-- USE greenlease_db;

-- Create database (PostgreSQL)
-- CREATE DATABASE greenlease_db;

-- Table: users (for authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: landlords
CREATE TABLE IF NOT EXISTS landlords (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    company VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    sustainability_score DECIMAL(3,2) DEFAULT 0.00,
    total_properties INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: properties
CREATE TABLE IF NOT EXISTS properties (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    rent DECIMAL(10,2) NOT NULL,
    property_type VARCHAR(50) DEFAULT 'apartment',
    bedrooms INT DEFAULT 0,
    bathrooms INT DEFAULT 0,
    square_footage DECIMAL(8,2) DEFAULT 0.00,
    
    -- Eco-rating fields
    insulation_rating INT DEFAULT 0 CHECK (insulation_rating >= 0 AND insulation_rating <= 10),
    solar_panels BOOLEAN DEFAULT FALSE,
    solar_rating INT DEFAULT 0 CHECK (solar_rating >= 0 AND solar_rating <= 10),
    water_conservation_rating INT DEFAULT 0 CHECK (water_conservation_rating >= 0 AND water_conservation_rating <= 10),
    green_space_proximity DECIMAL(5,2) DEFAULT 0.00, -- in miles
    energy_efficiency_rating INT DEFAULT 0 CHECK (energy_efficiency_rating >= 0 AND energy_efficiency_rating <= 10),
    overall_eco_score DECIMAL(4,2) DEFAULT 0.00,
    
    -- Additional fields
    landlord_id BIGINT,
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (landlord_id) REFERENCES landlords(id) ON DELETE SET NULL
);

-- Table: feedback
CREATE TABLE IF NOT EXISTS feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    property_id BIGINT NOT NULL,
    tenant_name VARCHAR(100) NOT NULL,
    tenant_email VARCHAR(255) NOT NULL,
    overall_rating INT NOT NULL CHECK (overall_rating >= 1 AND overall_rating <= 5),
    eco_rating INT DEFAULT 0 CHECK (eco_rating >= 0 AND eco_rating <= 5),
    comment TEXT,
    is_verified BOOLEAN DEFAULT FALSE,
    is_recommended BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Specific eco-feature ratings
    insulation_experience INT DEFAULT 0 CHECK (insulation_experience >= 0 AND insulation_experience <= 5),
    energy_bill_satisfaction INT DEFAULT 0 CHECK (energy_bill_satisfaction >= 0 AND energy_bill_satisfaction <= 5),
    solar_system_satisfaction INT DEFAULT 0 CHECK (solar_system_satisfaction >= 0 AND solar_system_satisfaction <= 5),
    water_efficiency_satisfaction INT DEFAULT 0 CHECK (water_efficiency_satisfaction >= 0 AND water_efficiency_satisfaction <= 5),
    green_space_satisfaction INT DEFAULT 0 CHECK (green_space_satisfaction >= 0 AND green_space_satisfaction <= 5),
    
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
);

-- Create indexes for better performance (if not exists)
CREATE INDEX IF NOT EXISTS idx_properties_city ON properties(city);
CREATE INDEX IF NOT EXISTS idx_properties_eco_score ON properties(overall_eco_score);
CREATE INDEX IF NOT EXISTS idx_properties_rent ON properties(rent);
CREATE INDEX IF NOT EXISTS idx_properties_available ON properties(is_available);
CREATE INDEX IF NOT EXISTS idx_properties_solar ON properties(solar_panels);
CREATE INDEX IF NOT EXISTS idx_feedback_property ON feedback(property_id);
CREATE INDEX IF NOT EXISTS idx_feedback_verified ON feedback(is_verified);

-- Insert sample users (password is 'password' encrypted with BCrypt) - Only if not exists
INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@greenlease.com', 'ADMIN', TRUE, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('demo', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'demo@greenlease.com', 'USER', TRUE, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE username=username;

-- Insert sample landlords - Only if not exists
INSERT INTO landlords (id, first_name, last_name, email, phone, company, is_verified, sustainability_score, total_properties, created_at, updated_at) VALUES 
(1, 'John', 'Smith', 'john.smith@greenlandlord.com', '555-0101', 'EcoHomes LLC', TRUE, 8.5, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE email=email;

INSERT INTO landlords (id, first_name, last_name, email, phone, company, is_verified, sustainability_score, total_properties, created_at, updated_at) VALUES 
(2, 'Sarah', 'Johnson', 'sarah.j@sustainablerentals.com', '555-0102', 'Sustainable Rentals Inc', TRUE, 9.2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE email=email;

INSERT INTO landlords (id, first_name, last_name, email, phone, company, is_verified, sustainability_score, total_properties, created_at, updated_at) VALUES 
(3, 'Mike', 'Brown', 'mike.brown@email.com', '555-0103', 'Brown Properties', FALSE, 6.8, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE email=email;

INSERT INTO landlords (id, first_name, last_name, email, phone, company, is_verified, sustainability_score, total_properties, created_at, updated_at) VALUES 
(4, 'Lisa', 'Davis', 'lisa.davis@greenvision.com', '555-0104', 'Green Vision Properties', TRUE, 8.9, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE email=email;

-- Insert sample properties with eco-ratings - Only if not exists
INSERT INTO properties (id, title, description, address, city, state, zip_code, rent, property_type, bedrooms, bathrooms, square_footage, insulation_rating, solar_panels, solar_rating, water_conservation_rating, green_space_proximity, energy_efficiency_rating, overall_eco_score, landlord_id, image_url, is_available, created_at, updated_at) VALUES 
(1, 'Eco-Luxury Downtown Apartment', 'Modern 2-bedroom apartment with solar panels, energy-efficient appliances, and excellent insulation. Located near MG Road with easy access to public transportation.', '123 Green Avenue', 'Bangalore', 'Karnataka', '560001', 35000.00, 'apartment', 2, 2, 1200.00, 9, TRUE, 9, 8, 0.2, 9, 8.84, 1, 'https://images.unsplash.com/photo-1560518883-ce09059eeffa', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE title=title;

INSERT INTO properties (id, title, description, address, city, state, zip_code, rent, property_type, bedrooms, bathrooms, square_footage, insulation_rating, solar_panels, solar_rating, water_conservation_rating, green_space_proximity, energy_efficiency_rating, overall_eco_score, landlord_id, image_url, is_available, created_at, updated_at) VALUES 
(2, 'Solar-Powered Family Home', 'Beautiful 3-bedroom house with solar panel system, rainwater harvesting, and smart home energy management. Close to green spaces and parks.', '456 Anna Nagar', 'Chennai', 'Tamil Nadu', '600040', 28000.00, 'house', 3, 2, 1800.00, 8, TRUE, 10, 9, 0.1, 8, 8.82, 2, 'https://images.unsplash.com/photo-1570129477492-45c003edd2be', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE title=title;

INSERT INTO properties (id, title, description, address, city, state, zip_code, rent, property_type, bedrooms, bathrooms, square_footage, insulation_rating, solar_panels, solar_rating, water_conservation_rating, green_space_proximity, energy_efficiency_rating, overall_eco_score, landlord_id, image_url, is_available, created_at, updated_at) VALUES 
(3, 'Green Studio Near University', 'Compact studio with excellent insulation, energy-efficient fixtures, and close to VIT campus. Perfect for eco-conscious students.', '789 Katpadi Road', 'Vellore', 'Tamil Nadu', '632014', 12000.00, 'studio', 0, 1, 500.00, 7, FALSE, 0, 7, 0.3, 7, 6.31, 3, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE title=title;

INSERT INTO properties (id, title, description, address, city, state, zip_code, rent, property_type, bedrooms, bathrooms, square_footage, insulation_rating, solar_panels, solar_rating, water_conservation_rating, green_space_proximity, energy_efficiency_rating, overall_eco_score, landlord_id, image_url, is_available, created_at, updated_at) VALUES 
(4, 'Sustainable Condo with City Views', 'Modern 1-bedroom condo featuring solar heating, water-saving fixtures, and proximity to parks. Great for urban professionals.', '321 Indiranagar', 'Bangalore', 'Karnataka', '560038', 22000.00, 'condo', 1, 1, 750.00, 8, TRUE, 8, 8, 0.4, 8, 8.08, 4, 'https://images.unsplash.com/photo-1545324418-cc1a3fa10c00', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE title=title;

INSERT INTO properties (id, title, description, address, city, state, zip_code, rent, property_type, bedrooms, bathrooms, square_footage, insulation_rating, solar_panels, solar_rating, water_conservation_rating, green_space_proximity, energy_efficiency_rating, overall_eco_score, landlord_id, image_url, is_available, created_at, updated_at) VALUES 
(5, 'Energy-Efficient Townhouse', 'Spacious 3-bedroom townhouse with smart thermostats, LED lighting throughout, and water-efficient landscaping.', '654 Adyar Main Road', 'Chennai', 'Tamil Nadu', '600020', 25000.00, 'townhouse', 3, 3, 1500.00, 7, FALSE, 0, 8, 0.5, 8, 6.90, 1, 'https://images.unsplash.com/photo-1564013799919-ab600027ffc6', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE title=title;

-- Insert sample feedback
INSERT INTO feedback (
    property_id, tenant_name, tenant_email, overall_rating, eco_rating, comment, 
    is_verified, is_recommended, insulation_experience, energy_bill_satisfaction, 
    solar_system_satisfaction, water_efficiency_satisfaction, green_space_satisfaction
) VALUES
(1, 'Alice Cooper', 'alice.cooper@email.com', 5, 5, 
 'Amazing eco-friendly apartment! The solar panels really reduced our electricity bills, and the location near the park is perfect.', 
 TRUE, TRUE, 5, 5, 5, 4, 5),
(1, 'Bob Wilson', 'bob.wilson@email.com', 4, 4, 
 'Great place with excellent energy efficiency. The insulation keeps the apartment comfortable year-round.', 
 TRUE, TRUE, 5, 4, 4, 4, 4),
(2, 'Carol Martinez', 'carol.m@email.com', 5, 5, 
 'Perfect family home! The solar system covers most of our energy needs, and the kids love the nearby green spaces.', 
 TRUE, TRUE, 4, 5, 5, 5, 5),
(3, 'David Lee', 'david.lee@student.edu', 4, 3, 
 'Good value for a student. The energy efficiency helps keep utility costs low, which is important on a student budget.', 
 TRUE, TRUE, 4, 4, 0, 3, 3),
(4, 'Emma Thompson', 'emma.t@email.com', 5, 4, 
 'Love the sustainable features and the city views. The water-saving fixtures work well and the building management is responsive.', 
 TRUE, TRUE, 4, 4, 4, 5, 3);

-- Update landlord statistics based on properties
UPDATE landlords l SET 
    total_properties = (SELECT COUNT(*) FROM properties p WHERE p.landlord_id = l.id),
    sustainability_score = (SELECT AVG(overall_eco_score) FROM properties p WHERE p.landlord_id = l.id);