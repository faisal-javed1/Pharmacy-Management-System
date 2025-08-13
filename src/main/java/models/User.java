package models;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdDate;

    public enum UserRole {
        ADMIN("Administrator"), 
        PHARMACIST("Pharmacist");
        
        private final String displayName;
        
        UserRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }

    // Constructors
    public User() {
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    public User(String username, String email, String passwordHash, UserRole role) {
        this();
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.id = generateId();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    // Role-based permission methods
    public boolean canManageInventory() {
        return role == UserRole.ADMIN;
    }
    
    public boolean canManageSuppliers() {
        return role == UserRole.ADMIN;
    }
    
    public boolean canManageUsers() {
        return role == UserRole.ADMIN;
    }
    
    public boolean canViewAlerts() {
        return role == UserRole.ADMIN;
    }
    
    public boolean canProcessSales() {
        return role == UserRole.ADMIN || role == UserRole.PHARMACIST;
    }
    
    public boolean canSearchMedicines() {
        return role == UserRole.ADMIN || role == UserRole.PHARMACIST;
    }
    
    public boolean canViewReports() {
        return role == UserRole.ADMIN;
    }

    private String generateId() {
        return "USR" + String.format("%03d", (int)(Math.random() * 1000));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
