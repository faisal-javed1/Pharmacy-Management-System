-- Pharmacy Management System Database Schema
-- MySQL Database Creation Script

-- Create Database
CREATE DATABASE IF NOT EXISTS pharmacy_management_system;
USE pharmacy_management_system;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS sale_items;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS low_stock_alerts;
DROP TABLE IF EXISTS medicines;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS users;

-- Users Table
CREATE TABLE users (
    id VARCHAR(10) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'PHARMACIST') NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Suppliers Table
CREATE TABLE suppliers (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_active (is_active)
);

-- Medicines Table
CREATE TABLE medicines (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL,
    expiry_date DATE NOT NULL,
    supplier_id VARCHAR(10),
    threshold INT NOT NULL DEFAULT 10,
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_stock (stock),
    INDEX idx_expiry (expiry_date),
    INDEX idx_supplier (supplier_id)
);

-- Sales Table
CREATE TABLE sales (
    id VARCHAR(10) PRIMARY KEY,
    customer_id VARCHAR(10),
    customer_name VARCHAR(100) NOT NULL,
    sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    discount DECIMAL(10,2) NOT NULL DEFAULT 0,
    final_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING',
    cashier_id VARCHAR(10) NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cashier_id) REFERENCES users(id),
    INDEX idx_customer_name (customer_name),
    INDEX idx_sale_date (sale_date),
    INDEX idx_status (status),
    INDEX idx_cashier (cashier_id)
);

-- Sale Items Table
CREATE TABLE sale_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id VARCHAR(10) NOT NULL,
    medicine_id VARCHAR(10) NOT NULL,
    medicine_name VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id),
    INDEX idx_sale_id (sale_id),
    INDEX idx_medicine_id (medicine_id)
);

-- Low Stock Alerts Table
CREATE TABLE low_stock_alerts (
    id VARCHAR(10) PRIMARY KEY,
    medicine_id VARCHAR(10) NOT NULL,
    medicine_name VARCHAR(200) NOT NULL,
    current_stock INT NOT NULL,
    threshold_stock INT NOT NULL,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') NOT NULL,
    status ENUM('ACTIVE', 'DISMISSED', 'RESOLVED') DEFAULT 'ACTIVE',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    dismissed_date DATETIME NULL,
    dismissed_by VARCHAR(10) NULL,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
    FOREIGN KEY (dismissed_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_medicine_id (medicine_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority)
);

-- Create Views for Common Queries

-- View for Medicine Stock Status
CREATE VIEW medicine_stock_view AS
SELECT 
    m.id,
    m.name,
    m.category,
    m.stock,
    m.threshold,
    m.price,
    m.expiry_date,
    s.name as supplier_name,
    CASE 
        WHEN m.stock = 0 THEN 'OUT_OF_STOCK'
        WHEN m.stock <= m.threshold THEN 'LOW_STOCK'
        ELSE 'IN_STOCK'
    END as stock_status,
    CASE 
        WHEN m.expiry_date < CURDATE() THEN 'EXPIRED'
        WHEN m.expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) THEN 'EXPIRING_SOON'
        ELSE 'VALID'
    END as expiry_status
FROM medicines m
LEFT JOIN suppliers s ON m.supplier_id = s.id;

-- View for Sales Summary
CREATE VIEW sales_summary_view AS
SELECT 
    s.id,
    s.customer_name,
    s.sale_date,
    s.final_amount,
    s.status,
    u.username as cashier_name,
    COUNT(si.id) as item_count
FROM sales s
LEFT JOIN users u ON s.cashier_id = u.id
LEFT JOIN sale_items si ON s.id = si.sale_id
GROUP BY s.id, s.customer_name, s.sale_date, s.final_amount, s.status, u.username;

-- View for Active Low Stock Alerts
CREATE VIEW active_alerts_view AS
SELECT 
    a.id,
    a.medicine_id,
    a.medicine_name,
    a.current_stock,
    a.threshold_stock,
    a.priority,
    a.created_date,
    m.category,
    s.name as supplier_name
FROM low_stock_alerts a
JOIN medicines m ON a.medicine_id = m.id
LEFT JOIN suppliers s ON m.supplier_id = s.id
WHERE a.status = 'ACTIVE'
ORDER BY 
    CASE a.priority 
        WHEN 'HIGH' THEN 1 
        WHEN 'MEDIUM' THEN 2 
        WHEN 'LOW' THEN 3 
    END,
    a.created_date DESC;

-- Stored Procedures

DELIMITER //

-- Procedure to update medicine stock
CREATE PROCEDURE UpdateMedicineStock(
    IN p_medicine_id VARCHAR(10),
    IN p_quantity_change INT,
    IN p_operation VARCHAR(10) -- 'ADD' or 'SUBTRACT'
)
BEGIN
    DECLARE current_stock INT;
    DECLARE new_stock INT;
    DECLARE threshold_value INT;
    
    -- Get current stock and threshold
    SELECT stock, threshold INTO current_stock, threshold_value
    FROM medicines 
    WHERE id = p_medicine_id;
    
    -- Calculate new stock
    IF p_operation = 'ADD' THEN
        SET new_stock = current_stock + p_quantity_change;
    ELSEIF p_operation = 'SUBTRACT' THEN
        SET new_stock = current_stock - p_quantity_change;
        IF new_stock < 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient stock';
        END IF;
    END IF;
    
    -- Update stock
    UPDATE medicines 
    SET stock = new_stock, updated_date = CURRENT_TIMESTAMP
    WHERE id = p_medicine_id;
    
    -- Check if low stock alert needed
    IF new_stock <= threshold_value THEN
        CALL CreateLowStockAlert(p_medicine_id);
    END IF;
END //

-- Procedure to create low stock alert
CREATE PROCEDURE CreateLowStockAlert(
    IN p_medicine_id VARCHAR(10)
)
BEGIN
    DECLARE medicine_name_val VARCHAR(200);
    DECLARE current_stock_val INT;
    DECLARE threshold_val INT;
    DECLARE priority_val VARCHAR(10);
    DECLARE alert_exists INT DEFAULT 0;
    
    -- Get medicine details
    SELECT name, stock, threshold 
    INTO medicine_name_val, current_stock_val, threshold_val
    FROM medicines 
    WHERE id = p_medicine_id;
    
    -- Check if active alert already exists
    SELECT COUNT(*) INTO alert_exists
    FROM low_stock_alerts 
    WHERE medicine_id = p_medicine_id AND status = 'ACTIVE';
    
    -- Only create alert if none exists
    IF alert_exists = 0 THEN
        -- Determine priority
        IF current_stock_val = 0 THEN
            SET priority_val = 'HIGH';
        ELSEIF current_stock_val <= (threshold_val * 0.3) THEN
            SET priority_val = 'HIGH';
        ELSEIF current_stock_val <= (threshold_val * 0.6) THEN
            SET priority_val = 'MEDIUM';
        ELSE
            SET priority_val = 'LOW';
        END IF;
        
        -- Insert alert
        INSERT INTO low_stock_alerts (
            id, medicine_id, medicine_name, current_stock, 
            threshold_stock, priority, status
        ) VALUES (
            CONCAT('ALT', LPAD((SELECT COALESCE(MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)), 0) + 1 FROM low_stock_alerts), 3, '0')),
            p_medicine_id, medicine_name_val, current_stock_val, 
            threshold_val, priority_val, 'ACTIVE'
        );
    END IF;
END //

-- Procedure to complete a sale
CREATE PROCEDURE CompleteSale(
    IN p_sale_id VARCHAR(10)
)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE medicine_id_val VARCHAR(10);
    DECLARE quantity_val INT;
    
    -- Cursor to iterate through sale items
    DECLARE sale_cursor CURSOR FOR 
        SELECT medicine_id, quantity 
        FROM sale_items 
        WHERE sale_id = p_sale_id;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    
    -- Update sale status
    UPDATE sales 
    SET status = 'COMPLETED', updated_date = CURRENT_TIMESTAMP
    WHERE id = p_sale_id;
    
    -- Update stock for each item
    OPEN sale_cursor;
    read_loop: LOOP
        FETCH sale_cursor INTO medicine_id_val, quantity_val;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        CALL UpdateMedicineStock(medicine_id_val, quantity_val, 'SUBTRACT');
    END LOOP;
    CLOSE sale_cursor;
    
    COMMIT;
END //

DELIMITER ;

-- Triggers

-- Trigger to auto-generate IDs
DELIMITER //

CREATE TRIGGER before_user_insert 
BEFORE INSERT ON users 
FOR EACH ROW 
BEGIN
    IF NEW.id IS NULL OR NEW.id = '' THEN
        SET NEW.id = CONCAT('USR', LPAD((SELECT COALESCE(MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)), 0) + 1 FROM users), 3, '0'));
    END IF;
END //

CREATE TRIGGER before_supplier_insert 
BEFORE INSERT ON suppliers 
FOR EACH ROW 
BEGIN
    IF NEW.id IS NULL OR NEW.id = '' THEN
        SET NEW.id = CONCAT('SUP', LPAD((SELECT COALESCE(MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)), 0) + 1 FROM suppliers), 3, '0'));
    END IF;
END //

CREATE TRIGGER before_medicine_insert 
BEFORE INSERT ON medicines 
FOR EACH ROW 
BEGIN
    IF NEW.id IS NULL OR NEW.id = '' THEN
        SET NEW.id = CONCAT('MED', LPAD((SELECT COALESCE(MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)), 0) + 1 FROM medicines), 3, '0'));
    END IF;
END //

CREATE TRIGGER before_sale_insert 
BEFORE INSERT ON sales 
FOR EACH ROW 
BEGIN
    IF NEW.id IS NULL OR NEW.id = '' THEN
        SET NEW.id = CONCAT('INV', LPAD((SELECT COALESCE(MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)), 0) + 1 FROM sales), 3, '0'));
    END IF;
END //

DELIMITER ;

-- Create indexes for better performance
CREATE INDEX idx_medicines_stock_threshold ON medicines(stock, threshold);
CREATE INDEX idx_sales_date_status ON sales(sale_date, status);
CREATE INDEX idx_sale_items_sale_medicine ON sale_items(sale_id, medicine_id);
