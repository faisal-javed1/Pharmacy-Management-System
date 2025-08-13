package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private String id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private LocalDateTime createdDate;
    private List<String> medicineIds;

    // Constructors
    public Supplier() {
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
        this.medicineIds = new ArrayList<>();
    }

    public Supplier(String name, String contactPerson, String email, String phone, String address) {
        this();
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.id = generateId();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public List<String> getMedicineIds() { return medicineIds; }
    public void setMedicineIds(List<String> medicineIds) { this.medicineIds = medicineIds; }

    // Business Logic Methods
    public void addMedicine(String medicineId) {
        if (!medicineIds.contains(medicineId)) {
            medicineIds.add(medicineId);
        }
    }

    public void removeMedicine(String medicineId) {
        medicineIds.remove(medicineId);
    }

    public int getMedicineCount() {
        return medicineIds.size();
    }

    private String generateId() {
        return "SUP" + String.format("%03d", (int)(Math.random() * 1000));
    }

    @Override
    public String toString() {
        return name + " (" + contactPerson + ")";
    }
}
