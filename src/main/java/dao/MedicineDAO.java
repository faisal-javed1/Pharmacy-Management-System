package dao;

import models.Medicine;
import utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Medicine operations
 */
public class MedicineDAO {
    private DatabaseConnection dbConnection;
    
    public MedicineDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    // Create a new medicine
    public boolean createMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (name, category, stock, price, expiry_date, supplier_id, threshold, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            int result = dbConnection.executeUpdate(sql,
                medicine.getName(),
                medicine.getCategory(),
                medicine.getStock(),
                medicine.getPrice(),
                medicine.getExpiryDate(),
                medicine.getSupplier(), // This should be supplier_id
                medicine.getThreshold(),
                medicine.getDescription()
            );
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error creating medicine: " + e.getMessage());
            return false;
        }
    }
    
    // Get medicine by ID
    public Medicine getMedicineById(String id) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMedicine(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting medicine by ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return null;
    }
    
    // Get all medicines
    public List<Medicine> getAllMedicines() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id ORDER BY m.name";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Update medicine
    public boolean updateMedicine(Medicine medicine) {
        String sql = "UPDATE medicines SET name = ?, category = ?, stock = ?, price = ?, expiry_date = ?, supplier_id = ?, threshold = ?, description = ? WHERE id = ?";
        
        try {
            int result = dbConnection.executeUpdate(sql,
                medicine.getName(),
                medicine.getCategory(),
                medicine.getStock(),
                medicine.getPrice(),
                medicine.getExpiryDate(),
                medicine.getSupplier(), // This should be supplier_id
                medicine.getThreshold(),
                medicine.getDescription(),
                medicine.getId()
            );
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating medicine: " + e.getMessage());
            return false;
        }
    }
    
    // Delete medicine
    public boolean deleteMedicine(String id) {
        String sql = "DELETE FROM medicines WHERE id = ?";
        
        try {
            int result = dbConnection.executeUpdate(sql, id);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting medicine: " + e.getMessage());
            return false;
        }
    }
    
    // Search medicines by name or ID
    public List<Medicine> searchMedicines(String searchTerm) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.name LIKE ? OR m.id LIKE ? OR m.category LIKE ? ORDER BY m.name";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            String searchPattern = "%" + searchTerm + "%";
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Get medicines by category
    public List<Medicine> getMedicinesByCategory(String category) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.category = ? ORDER BY m.name";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting medicines by category: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Get low stock medicines
    public List<Medicine> getLowStockMedicines() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.stock <= m.threshold ORDER BY m.stock ASC";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting low stock medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Get out of stock medicines
    public List<Medicine> getOutOfStockMedicines() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.stock = 0 ORDER BY m.name";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting out of stock medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Get expired medicines
    public List<Medicine> getExpiredMedicines() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.expiry_date < CURDATE() ORDER BY m.expiry_date";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting expired medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Get medicines expiring soon
    public List<Medicine> getExpiringSoonMedicines(int days) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.expiry_date <= DATE_ADD(CURDATE(), INTERVAL ? DAY) AND m.expiry_date >= CURDATE() ORDER BY m.expiry_date";
        List<Medicine> medicines = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setInt(1, days);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting expiring soon medicines: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return medicines;
    }
    
    // Update stock using stored procedure
    public boolean updateStock(String medicineId, int quantity, String operation) {
        String sql = "CALL UpdateMedicineStock(?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, medicineId);
            pstmt.setInt(2, quantity);
            pstmt.setString(3, operation);
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
    
    // Get distinct categories
    public List<String> getCategories() {
        String sql = "SELECT DISTINCT category FROM medicines ORDER BY category";
        List<String> categories = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return categories;
    }
    
    // Get medicine statistics
    public int getTotalMedicineCount() {
        String sql = "SELECT COUNT(*) FROM medicines";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total medicine count: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return 0;
    }
    
    public int getLowStockCount() {
        String sql = "SELECT COUNT(*) FROM medicines WHERE stock <= threshold";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting low stock count: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return 0;
    }
    
    public double getTotalInventoryValue() {
        String sql = "SELECT SUM(stock * price) FROM medicines";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total inventory value: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return 0.0;
    }
    
    // Helper method to map ResultSet to Medicine object
    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setId(rs.getString("id"));
        medicine.setName(rs.getString("name"));
        medicine.setCategory(rs.getString("category"));
        medicine.setStock(rs.getInt("stock"));
        medicine.setPrice(rs.getDouble("price"));
        
        Date expiryDate = rs.getDate("expiry_date");
        if (expiryDate != null) {
            medicine.setExpiryDate(expiryDate.toLocalDate());
        }
        
        // Use supplier name if available, otherwise use supplier_id
        String supplierName = rs.getString("supplier_name");
        if (supplierName != null) {
            medicine.setSupplier(supplierName);
        } else {
            medicine.setSupplier(rs.getString("supplier_id"));
        }
        
        medicine.setThreshold(rs.getInt("threshold"));
        medicine.setDescription(rs.getString("description"));
        
        return medicine;
    }
}
