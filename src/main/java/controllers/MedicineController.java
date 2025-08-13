package controllers;

import models.Medicine;
import utils.DatabaseManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineController {
    private DatabaseManager dbManager;
    
    public MedicineController() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // CRUD Operations
    public List<Medicine> getAllMedicines() {
        return dbManager.getAllMedicines();
    }
    
    public Medicine getMedicineById(String id) {
        return dbManager.getMedicineById(id);
    }
    
    public boolean createMedicine(String name, String category, int stock, double price,
                                LocalDate expiryDate, String supplier, int threshold, String description) {
        Medicine medicine = new Medicine(name, category, stock, price, expiryDate, supplier, threshold);
        medicine.setDescription(description);
        return dbManager.saveMedicine(medicine);
    }
    
    public boolean updateMedicine(Medicine medicine) {
        return dbManager.saveMedicine(medicine);
    }
    
    public boolean deleteMedicine(String id) {
        return dbManager.deleteMedicine(id);
    }
    
    // Search and Filter Operations
    public List<Medicine> searchMedicines(String searchTerm) {
        return getAllMedicines().stream()
                               .filter(med -> med.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                            med.getId().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                            med.getCategory().toLowerCase().contains(searchTerm.toLowerCase()))
                               .collect(Collectors.toList());
    }
    
    public List<Medicine> getMedicinesByCategory(String category) {
        return dbManager.getMedicinesByCategory(category);
    }
    
    public List<Medicine> getLowStockMedicines() {
        return getAllMedicines().stream()
                               .filter(Medicine::isLowStock)
                               .collect(Collectors.toList());
    }
    
    public List<Medicine> getOutOfStockMedicines() {
        return getAllMedicines().stream()
                               .filter(Medicine::isOutOfStock)
                               .collect(Collectors.toList());
    }
    
    public List<Medicine> getExpiredMedicines() {
        return getAllMedicines().stream()
                               .filter(Medicine::isExpired)
                               .collect(Collectors.toList());
    }
    
    public List<Medicine> getExpiringSoonMedicines(int days) {
        return getAllMedicines().stream()
                               .filter(med -> med.isExpiringSoon(days))
                               .collect(Collectors.toList());
    }
    
    // Stock Management
    public boolean updateStock(String medicineId, int newStock) {
        Medicine medicine = getMedicineById(medicineId);
        if (medicine != null) {
            medicine.setStock(newStock);
            return updateMedicine(medicine);
        }
        return false;
    }
    
    public boolean addStock(String medicineId, int quantity) {
        Medicine medicine = getMedicineById(medicineId);
        if (medicine != null) {
            medicine.addStock(quantity);
            return updateMedicine(medicine);
        }
        return false;
    }
    
    public boolean reduceStock(String medicineId, int quantity) {
        Medicine medicine = getMedicineById(medicineId);
        if (medicine != null && medicine.getStock() >= quantity) {
            medicine.reduceStock(quantity);
            return updateMedicine(medicine);
        }
        return false;
    }
    
    // Business Logic
    public boolean isStockAvailable(String medicineId, int requiredQuantity) {
        Medicine medicine = getMedicineById(medicineId);
        return medicine != null && medicine.getStock() >= requiredQuantity;
    }
    
    public List<String> getCategories() {
        return getAllMedicines().stream()
                               .map(Medicine::getCategory)
                               .distinct()
                               .sorted()
                               .collect(Collectors.toList());
    }
    
    public List<String> getSuppliers() {
        return getAllMedicines().stream()
                               .map(Medicine::getSupplier)
                               .distinct()
                               .sorted()
                               .collect(Collectors.toList());
    }
    
    // Statistics
    public int getTotalMedicineCount() {
        return getAllMedicines().size();
    }
    
    public int getLowStockCount() {
        return getLowStockMedicines().size();
    }
    
    public double getTotalInventoryValue() {
        return getAllMedicines().stream()
                               .mapToDouble(med -> med.getPrice() * med.getStock())
                               .sum();
    }
}
