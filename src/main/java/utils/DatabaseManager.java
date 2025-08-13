package utils;

import java.util.ArrayList;
import java.util.List;

import dao.MedicineDAO;
import dao.UserDAO;
import models.LowStockAlert;
import models.Medicine;
import models.Sale;
import models.Supplier;
import models.User;

/**
 * Updated Database Manager that uses MySQL instead of in-memory storage
 * Acts as a facade for all DAO operations
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    
    // DAO instances
    private UserDAO userDAO;
    private MedicineDAO medicineDAO;
    // Add other DAOs as needed
    
    private DatabaseManager() {
        initializeDAOs();
        // Test database connection
        if (!testConnection()) {
            System.err.println("Warning: Database connection failed. Application may not work properly.");
        }
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeDAOs() {
        userDAO = new UserDAO();
        medicineDAO = new MedicineDAO();
    }
    
    private boolean testConnection() {
        try {
            DatabaseConnection dbConn = DatabaseConnection.getInstance();
            return dbConn.isDatabaseAccessible();
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    // User Operations
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public User getUserById(String id) {
        return userDAO.getUserById(id);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    public boolean saveUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            return userDAO.createUser(user);
        } else {
            return userDAO.updateUser(user);
        }
    }
    
    public boolean deleteUser(String id) {
        return userDAO.deleteUser(id);
    }
    
    public boolean isUsernameAvailable(String username) {
        return !userDAO.usernameExists(username);
    }
    
    public boolean isEmailAvailable(String email) {
        return !userDAO.emailExists(email);
    }
    
    // Medicine Operations
    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAllMedicines();
    }
    
    public Medicine getMedicineById(String id) {
        return medicineDAO.getMedicineById(id);
    }
    
    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineDAO.getMedicinesByCategory(category);
    }
    
    public boolean saveMedicine(Medicine medicine) {
        if (medicine.getId() == null || medicine.getId().isEmpty()) {
            return medicineDAO.createMedicine(medicine);
        } else {
            return medicineDAO.updateMedicine(medicine);
        }
    }
    
    public boolean deleteMedicine(String id) {
        return medicineDAO.deleteMedicine(id);
    }
    
    public List<Medicine> searchMedicines(String searchTerm) {
        return medicineDAO.searchMedicines(searchTerm);
    }
    
    public List<Medicine> getLowStockMedicines() {
        return medicineDAO.getLowStockMedicines();
    }
    
    public List<Medicine> getOutOfStockMedicines() {
        return medicineDAO.getOutOfStockMedicines();
    }
    
    public List<Medicine> getExpiredMedicines() {
        return medicineDAO.getExpiredMedicines();
    }
    
    public List<Medicine> getExpiringSoonMedicines(int days) {
        return medicineDAO.getExpiringSoonMedicines(days);
    }
    
    public boolean updateMedicineStock(String medicineId, int quantity, String operation) {
        return medicineDAO.updateStock(medicineId, quantity, operation);
    }
    
    public List<String> getMedicineCategories() {
        return medicineDAO.getCategories();
    }
    
    // Statistics Methods
    public int getTotalMedicines() {
        return medicineDAO.getTotalMedicineCount();
    }
    
    public int getLowStockCount() {
        return medicineDAO.getLowStockCount();
    }
    
    public double getTotalInventoryValue() {
        return medicineDAO.getTotalInventoryValue();
    }
    
    public double getTodaysSales() {
        // This would be implemented with SaleDAO
        // For now, return a placeholder value
        return 0.0;
    }
    
    public int getActiveSuppliers() {
        // This would be implemented with SupplierDAO
        // For now, return a placeholder value
        return 0;
    }
    
    // Placeholder methods for compatibility with existing code
    public List<Sale> getAllSales() {
        // TODO: Implement with SaleDAO
        return new ArrayList<>();
    }
    
    public Sale getSaleById(String id) {
        // TODO: Implement with SaleDAO
        return null;
    }
    
    public boolean saveSale(Sale sale) {
        // TODO: Implement with SaleDAO
        return false;
    }
    
    public List<Supplier> getAllSuppliers() {
        // TODO: Implement with SupplierDAO
        return new ArrayList<>();
    }
    
    public Supplier getSupplierById(String id) {
        // TODO: Implement with SupplierDAO
        return null;
    }
    
    public boolean saveSupplier(Supplier supplier) {
        // TODO: Implement with SupplierDAO
        return false;
    }
    
    public boolean deleteSupplier(String id) {
        // TODO: Implement with SupplierDAO
        return false;
    }
    
    public List<LowStockAlert> getAllAlerts() {
        // TODO: Implement with AlertDAO
        return new ArrayList<>();
    }
    
    public List<LowStockAlert> getActiveAlerts() {
        // TODO: Implement with AlertDAO
        return new ArrayList<>();
    }
    
    public boolean saveAlert(LowStockAlert alert) {
        // TODO: Implement with AlertDAO
        return false;
    }
    
    // Database connection info
    public void printDatabaseInfo() {
        DatabaseConnection.getInstance().printDatabaseInfo();
    }
    
    public boolean isDatabaseConnected() {
        return DatabaseConnection.getInstance().isDatabaseAccessible();
    }
}

