-- Pharmacy Management System - Sample Data
-- Run this after creating the schema

USE pharmacy_management_system;

-- Insert Default Users
INSERT INTO users (id, username, email, password_hash, role, full_name, phone, is_active) VALUES
('USR001', 'admin', 'admin@pharmacy.com', 'hashed_admin_password', 'ADMIN', 'System Administrator', '+1-555-0001', TRUE),
('USR002', 'pharmacist', 'pharmacist@pharmacy.com', 'hashed_pharmacist_password', 'PHARMACIST', 'John Pharmacist', '+1-555-0002', TRUE),
('USR003', 'john_pharma', 'john@pharmacy.com', 'hashed_john_password', 'PHARMACIST', 'John Smith', '+1-555-0003', TRUE);

-- Insert Suppliers
INSERT INTO suppliers (id, name, contact_person, email, phone, address, is_active) VALUES
('SUP001', 'PharmaCorp Ltd', 'John Smith', 'john@pharmacorp.com', '+1-555-0123', '123 Medical St, Health City, HC 12345', TRUE),
('SUP002', 'MediSupply Inc', 'Sarah Johnson', 'sarah@medisupply.com', '+1-555-0456', '456 Pharma Ave, Medicine Town, MT 67890', TRUE),
('SUP003', 'HealthCare Distributors', 'Mike Wilson', 'mike@healthcare-dist.com', '+1-555-0789', '789 Supply Blvd, Drug District, DD 54321', TRUE),
('SUP004', 'Global Pharma Solutions', 'Lisa Chen', 'lisa@globalpharma.com', '+1-555-0321', '321 International Way, Pharma City, PC 98765', TRUE),
('SUP005', 'Medical Wholesale Co', 'Robert Brown', 'robert@medwholesale.com', '+1-555-0654', '654 Wholesale Rd, Supply Town, ST 13579', TRUE);

-- Insert Medicines
INSERT INTO medicines (id, name, category, stock, price, expiry_date, supplier_id, threshold, description) VALUES
('MED001', 'Paracetamol 500mg', 'Pain Relief', 150, 2.50, '2025-12-31', 'SUP001', 20, 'For relief of mild to moderate pain and fever'),
('MED002', 'Amoxicillin 250mg', 'Antibiotics', 8, 15.75, '2025-06-15', 'SUP002', 25, 'Antibiotic for bacterial infections'),
('MED003', 'Ibuprofen 400mg', 'Pain Relief', 12, 3.25, '2026-03-20', 'SUP001', 30, 'Anti-inflammatory pain reliever'),
('MED004', 'Aspirin 325mg', 'Pain Relief', 0, 1.75, '2025-08-10', 'SUP001', 50, 'Pain reliever and blood thinner'),
('MED005', 'Cough Syrup', 'Respiratory', 45, 8.50, '2025-09-15', 'SUP003', 15, 'For cough and throat irritation'),
('MED006', 'Vitamin C 1000mg', 'Vitamins', 200, 12.99, '2026-01-30', 'SUP002', 30, 'Immune system support vitamin'),
('MED007', 'Omeprazole 20mg', 'Digestive', 75, 18.50, '2025-11-20', 'SUP001', 25, 'Proton pump inhibitor for acid reflux'),
('MED008', 'Metformin 500mg', 'Diabetes', 120, 22.75, '2026-02-28', 'SUP002', 40, 'Type 2 diabetes medication'),
('MED009', 'Lisinopril 10mg', 'Cardiovascular', 90, 16.25, '2025-10-15', 'SUP003', 35, 'ACE inhibitor for blood pressure'),
('MED010', 'Albuterol Inhaler', 'Respiratory', 25, 45.00, '2025-07-30', 'SUP001', 10, 'Bronchodilator for asthma'),
('MED011', 'Cetirizine 10mg', 'Allergy', 180, 6.75, '2026-04-10', 'SUP002', 50, 'Antihistamine for allergies'),
('MED012', 'Simvastatin 20mg', 'Cardiovascular', 65, 28.50, '2025-12-05', 'SUP003', 30, 'Cholesterol-lowering medication'),
('MED013', 'Loratadine 10mg', 'Allergy', 3, 5.25, '2025-08-20', 'SUP001', 40, 'Non-drowsy antihistamine'),
('MED014', 'Dextromethorphan Syrup', 'Respiratory', 35, 11.75, '2025-11-12', 'SUP002', 20, 'Cough suppressant'),
('MED015', 'Calcium Carbonate', 'Supplements', 150, 9.99, '2026-03-15', 'SUP003', 25, 'Calcium supplement for bone health'),
('MED016', 'Diphenhydramine 25mg', 'Allergy', 85, 4.50, '2025-09-30', 'SUP004', 35, 'Antihistamine and sleep aid'),
('MED017', 'Hydrocortisone Cream 1%', 'Topical', 60, 7.25, '2025-10-25', 'SUP004', 20, 'Anti-inflammatory topical cream'),
('MED018', 'Multivitamin Tablets', 'Vitamins', 300, 15.99, '2026-05-20', 'SUP005', 50, 'Daily multivitamin supplement'),
('MED019', 'Acetaminophen 325mg', 'Pain Relief', 220, 1.99, '2025-11-30', 'SUP005', 40, 'Pain reliever and fever reducer'),
('MED020', 'Ranitidine 150mg', 'Digestive', 5, 12.50, '2025-07-15', 'SUP004', 30, 'H2 receptor antagonist for heartburn');

-- Insert Sample Sales
INSERT INTO sales (id, customer_name, sale_date, total_amount, discount, final_amount, status, cashier_id) VALUES
('INV001', 'John Doe', '2024-01-15 10:30:00', 5.00, 0.00, 5.00, 'COMPLETED', 'USR002'),
('INV002', 'Jane Smith', '2024-01-15 14:20:00', 22.25, 0.00, 22.25, 'COMPLETED', 'USR002'),
('INV003', 'Bob Johnson', '2024-01-16 09:15:00', 54.48, 2.00, 52.48, 'COMPLETED', 'USR003'),
('INV004', 'Alice Brown', '2024-01-16 16:45:00', 18.50, 0.00, 18.50, 'COMPLETED', 'USR002'),
('INV005', 'Charlie Wilson', '2024-01-17 11:30:00', 31.50, 1.50, 30.00, 'COMPLETED', 'USR003');

-- Insert Sale Items
INSERT INTO sale_items (sale_id, medicine_id, medicine_name, price, quantity, subtotal) VALUES
-- Sale INV001
('INV001', 'MED001', 'Paracetamol 500mg', 2.50, 2, 5.00),
-- Sale INV002
('INV002', 'MED002', 'Amoxicillin 250mg', 15.75, 1, 15.75),
('INV002', 'MED003', 'Ibuprofen 400mg', 3.25, 2, 6.50),
-- Sale INV003
('INV003', 'MED005', 'Cough Syrup', 8.50, 1, 8.50),
('INV003', 'MED006', 'Vitamin C 1000mg', 12.99, 3, 38.97),
('INV003', 'MED011', 'Cetirizine 10mg', 6.75, 1, 6.75),
-- Sale INV004
('INV004', 'MED007', 'Omeprazole 20mg', 18.50, 1, 18.50),
-- Sale INV005
('INV005', 'MED008', 'Metformin 500mg', 22.75, 1, 22.75),
('INV005', 'MED005', 'Cough Syrup', 8.50, 1, 8.50);

-- Generate Low Stock Alerts for medicines with low stock
INSERT INTO low_stock_alerts (id, medicine_id, medicine_name, current_stock, threshold_stock, priority, status) VALUES
('ALT001', 'MED002', 'Amoxicillin 250mg', 8, 25, 'HIGH', 'ACTIVE'),
('ALT002', 'MED004', 'Aspirin 325mg', 0, 50, 'HIGH', 'ACTIVE'),
('ALT003', 'MED003', 'Ibuprofen 400mg', 12, 30, 'MEDIUM', 'ACTIVE'),
('ALT004', 'MED013', 'Loratadine 10mg', 3, 40, 'HIGH', 'ACTIVE'),
('ALT005', 'MED020', 'Ranitidine 150mg', 5, 30, 'HIGH', 'ACTIVE');

-- Update last login for demo users
UPDATE users SET last_login = NOW() WHERE username IN ('admin', 'pharmacist');

-- Create some additional test data for better demonstration
INSERT INTO sales (id, customer_name, sale_date, total_amount, discount, final_amount, status, cashier_id) VALUES
('INV006', 'David Lee', '2024-01-18 08:45:00', 45.00, 0.00, 45.00, 'COMPLETED', 'USR002'),
('INV007', 'Emma Davis', '2024-01-18 13:20:00', 28.74, 0.00, 28.74, 'COMPLETED', 'USR003'),
('INV008', 'Michael Chen', '2024-01-19 10:15:00', 67.23, 5.00, 62.23, 'COMPLETED', 'USR002');

INSERT INTO sale_items (sale_id, medicine_id, medicine_name, price, quantity, subtotal) VALUES
-- Sale INV006
('INV006', 'MED010', 'Albuterol Inhaler', 45.00, 1, 45.00),
-- Sale INV007
('INV007', 'MED012', 'Simvastatin 20mg', 28.50, 1, 28.50),
-- Sale INV008
('INV008', 'MED018', 'Multivitamin Tablets', 15.99, 2, 31.98),
('INV008', 'MED019', 'Acetaminophen 325mg', 1.99, 5, 9.95),
('INV008', 'MED017', 'Hydrocortisone Cream 1%', 7.25, 3, 21.75);
