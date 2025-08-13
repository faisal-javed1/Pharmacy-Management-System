package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private String id;
    private String customerId;
    private String customerName;
    private LocalDateTime saleDate;
    private List<SaleItem> items;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private SaleStatus status;
    private String cashierId;

    public enum SaleStatus {
        PENDING, COMPLETED, CANCELLED, REFUNDED
    }

    // Constructors
    public Sale() {
        this.saleDate = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.status = SaleStatus.PENDING;
        this.discount = 0.0;
    }

    public Sale(String customerName, String cashierId) {
        this();
        this.customerName = customerName;
        this.cashierId = cashierId;
        this.id = generateId();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }

    public String getCashierId() { return cashierId; }
    public void setCashierId(String cashierId) { this.cashierId = cashierId; }

    // Business Logic Methods
    public void addItem(Medicine medicine, int quantity) {
        SaleItem item = new SaleItem(medicine.getId(), medicine.getName(), 
                                   medicine.getPrice(), quantity);
        items.add(item);
        calculateTotal();
    }

    public void removeItem(String medicineId) {
        items.removeIf(item -> item.getMedicineId().equals(medicineId));
        calculateTotal();
    }

    public void calculateTotal() {
        totalAmount = items.stream()
                          .mapToDouble(item -> item.getPrice() * item.getQuantity())
                          .sum();
        finalAmount = totalAmount - discount;
    }

    public void completeSale() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot complete sale with no items");
        }
        this.status = SaleStatus.COMPLETED;
        calculateTotal();
    }

    private String generateId() {
        return "INV" + String.format("%03d", (int)(Math.random() * 1000));
    }

    @Override
    public String toString() {
        return id + " - " + customerName + " - $" + String.format("%.2f", finalAmount);
    }

    // Inner class for Sale Items
    public static class SaleItem {
        private String medicineId;
        private String medicineName;
        private double price;
        private int quantity;
        private double subtotal;

        public SaleItem(String medicineId, String medicineName, double price, int quantity) {
            this.medicineId = medicineId;
            this.medicineName = medicineName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = price * quantity;
        }

        // Getters and Setters
        public String getMedicineId() { return medicineId; }
        public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { 
            this.quantity = quantity;
            this.subtotal = price * quantity;
        }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
}
