package controllers;

import models.Sale;
import models.Medicine;
import utils.DatabaseManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SaleController {
    private DatabaseManager dbManager;
    private MedicineController medicineController;
    
    public SaleController() {
        this.dbManager = DatabaseManager.getInstance();
        this.medicineController = new MedicineController();
    }
    
    // CRUD Operations
    public List<Sale> getAllSales() {
        return dbManager.getAllSales();
    }
    
    public Sale getSaleById(String id) {
        return dbManager.getSaleById(id);
    }
    
    public boolean createSale(String customerName, String cashierId) {
        Sale sale = new Sale(customerName, cashierId);
        return dbManager.saveSale(sale);
    }
    
    public boolean updateSale(Sale sale) {
        return dbManager.saveSale(sale);
    }
    
    // Sale Processing
    public Sale createNewSale(String customerName, String cashierId) {
        Sale sale = new Sale(customerName, cashierId);
        dbManager.saveSale(sale);
        return sale;
    }
    
    public boolean addItemToSale(String saleId, String medicineId, int quantity) {
        Sale sale = getSaleById(saleId);
        Medicine medicine = medicineController.getMedicineById(medicineId);
        
        if (sale != null && medicine != null && 
            sale.getStatus() == Sale.SaleStatus.PENDING &&
            medicine.getStock() >= quantity) {
            
            sale.addItem(medicine, quantity);
            return updateSale(sale);
        }
        return false;
    }
    
    public boolean removeItemFromSale(String saleId, String medicineId) {
        Sale sale = getSaleById(saleId);
        if (sale != null && sale.getStatus() == Sale.SaleStatus.PENDING) {
            sale.removeItem(medicineId);
            return updateSale(sale);
        }
        return false;
    }
    
    public boolean completeSale(String saleId) {
        Sale sale = getSaleById(saleId);
        if (sale != null && sale.getStatus() == Sale.SaleStatus.PENDING && !sale.getItems().isEmpty()) {
            
            // Update medicine stock for each item
            for (Sale.SaleItem item : sale.getItems()) {
                if (!medicineController.reduceStock(item.getMedicineId(), item.getQuantity())) {
                    return false; // Stock update failed
                }
            }
            
            sale.completeSale();
            return updateSale(sale);
        }
        return false;
    }
    
    public boolean cancelSale(String saleId) {
        Sale sale = getSaleById(saleId);
        if (sale != null && sale.getStatus() == Sale.SaleStatus.PENDING) {
            sale.setStatus(Sale.SaleStatus.CANCELLED);
            return updateSale(sale);
        }
        return false;
    }
    
    public boolean applyDiscount(String saleId, double discount) {
        Sale sale = getSaleById(saleId);
        if (sale != null && sale.getStatus() == Sale.SaleStatus.PENDING && discount >= 0) {
            sale.setDiscount(discount);
            sale.calculateTotal();
            return updateSale(sale);
        }
        return false;
    }
    
    // Search and Filter
    public List<Sale> getSalesByCustomer(String customerName) {
        return getAllSales().stream()
                           .filter(sale -> sale.getCustomerName().toLowerCase()
                                              .contains(customerName.toLowerCase()))
                           .collect(Collectors.toList());
    }
    
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return getAllSales().stream()
                           .filter(sale -> sale.getSaleDate().isAfter(startDate) && 
                                         sale.getSaleDate().isBefore(endDate))
                           .collect(Collectors.toList());
    }
    
    public List<Sale> getSalesByStatus(Sale.SaleStatus status) {
        return getAllSales().stream()
                           .filter(sale -> sale.getStatus() == status)
                           .collect(Collectors.toList());
    }
    
    public List<Sale> getTodaysSales() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getSalesByDateRange(startOfDay, endOfDay);
    }
    
    // Statistics and Reports
    public double getTotalSalesAmount() {
        return getAllSales().stream()
                           .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                           .mapToDouble(Sale::getFinalAmount)
                           .sum();
    }
    
    public double getTodaysSalesAmount() {
        return getTodaysSales().stream()
                              .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                              .mapToDouble(Sale::getFinalAmount)
                              .sum();
    }
    
    public int getTotalSalesCount() {
        return (int) getAllSales().stream()
                                 .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                                 .count();
    }
    
    public int getTodaysSalesCount() {
        return (int) getTodaysSales().stream()
                                   .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                                   .count();
    }
    
    public List<Sale> getTopSales(int limit) {
        return getAllSales().stream()
                           .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                           .sorted((s1, s2) -> Double.compare(s2.getFinalAmount(), s1.getFinalAmount()))
                           .limit(limit)
                           .collect(Collectors.toList());
    }
    
    // Validation
    public boolean validateSaleItem(String medicineId, int quantity) {
        Medicine medicine = medicineController.getMedicineById(medicineId);
        return medicine != null && medicine.getStock() >= quantity && quantity > 0;
    }
}
