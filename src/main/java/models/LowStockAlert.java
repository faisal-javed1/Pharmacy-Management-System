package models;

import java.time.LocalDateTime;

public class LowStockAlert {
    private String id;
    private String medicineId;
    private String medicineName;
    private int currentStock;
    private int threshold;
    private AlertPriority priority;
    private AlertStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime dismissedDate;
    private String dismissedBy;

    public enum AlertPriority {
        HIGH, MEDIUM, LOW
    }

    public enum AlertStatus {
        ACTIVE, DISMISSED, RESOLVED
    }

    // Constructors
    public LowStockAlert() {
        this.createdDate = LocalDateTime.now();
        this.status = AlertStatus.ACTIVE;
    }

    public LowStockAlert(String medicineId, String medicineName, int currentStock, int threshold) {
        this();
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.currentStock = currentStock;
        this.threshold = threshold;
        this.priority = calculatePriority();
        this.id = generateId();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { 
        this.currentStock = currentStock;
        this.priority = calculatePriority();
    }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public AlertPriority getPriority() { return priority; }
    public void setPriority(AlertPriority priority) { this.priority = priority; }

    public AlertStatus getStatus() { return status; }
    public void setStatus(AlertStatus status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getDismissedDate() { return dismissedDate; }
    public void setDismissedDate(LocalDateTime dismissedDate) { this.dismissedDate = dismissedDate; }

    public String getDismissedBy() { return dismissedBy; }
    public void setDismissedBy(String dismissedBy) { this.dismissedBy = dismissedBy; }

    // Business Logic Methods
    private AlertPriority calculatePriority() {
        if (currentStock == 0) {
            return AlertPriority.HIGH;
        } else if (currentStock <= threshold * 0.3) {
            return AlertPriority.HIGH;
        } else if (currentStock <= threshold * 0.6) {
            return AlertPriority.MEDIUM;
        } else {
            return AlertPriority.LOW;
        }
    }

    public void dismiss(String userId) {
        this.status = AlertStatus.DISMISSED;
        this.dismissedDate = LocalDateTime.now();
        this.dismissedBy = userId;
    }

    public void reactivate() {
        this.status = AlertStatus.ACTIVE;
        this.dismissedDate = null;
        this.dismissedBy = null;
    }

    public boolean isCritical() {
        return currentStock == 0;
    }

    private String generateId() {
        return "ALT" + String.format("%03d", (int)(Math.random() * 1000));
    }

    @Override
    public String toString() {
        return medicineName + " - Stock: " + currentStock + "/" + threshold + " (" + priority + ")";
    }
}
