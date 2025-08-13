package models;

import java.time.LocalDate;

public class Medicine {
    private String id;
    private String name;
    private String category;
    private int stock;
    private double price;
    private LocalDate expiryDate;
    private String supplier;
    private int threshold;
    private String description;

    // Constructors
    public Medicine() {}

    public Medicine(String name, String category, int stock, double price, 
                   LocalDate expiryDate, String supplier, int threshold) {
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.threshold = threshold;
        this.id = generateId();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Business Logic Methods
    public boolean isLowStock() {
        return stock <= threshold;
    }

    public boolean isOutOfStock() {
        return stock == 0;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon(int days) {
        return expiryDate.isBefore(LocalDate.now().plusDays(days));
    }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stock -= quantity;
    }

    public void addStock(int quantity) {
        this.stock += quantity;
    }

    private String generateId() {
        return "MED" + String.format("%03d", (int)(Math.random() * 1000));
    }

    @Override
    public String toString() {
        return name + " (" + id + ") - Stock: " + stock + " - $" + price;
    }
}
