package controllers;

import models.User;
import utils.DatabaseManager;
import utils.PasswordHasher;
import java.util.List;
import java.util.regex.Pattern;

public class UserController {
    private DatabaseManager dbManager;
    
    public UserController() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // Authentication
    public User authenticateUser(String username, String password) {
        User user = dbManager.getUserByUsername(username);
        if (user != null && user.isActive() && 
            PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            user.setLastLogin(java.time.LocalDateTime.now());
            dbManager.saveUser(user);
            return user;
        }
        return null;
    }
    
    // CRUD Operations
    public List<User> getAllUsers() {
        return dbManager.getAllUsers();
    }
    
    public User getUserById(String id) {
        return dbManager.getUserById(id);
    }
    
    public boolean createUser(String username, String email, String password, User.UserRole role) {
        // Validate input
        if (!isValidUserInput(username, email, password)) {
            return false;
        }
        
        // Check if username already exists
        if (dbManager.getUserByUsername(username) != null) {
            return false;
        }
        
        // Check if email already exists
        if (isEmailAlreadyExists(email)) {
            return false;
        }
        
        String hashedPassword = PasswordHasher.hashPassword(password);
        User user = new User(username, email, hashedPassword, role);
        return dbManager.saveUser(user);
    }
    
    public boolean updateUser(User user) {
        return dbManager.saveUser(user);
    }
    
    public boolean deleteUser(String id) {
        User user = dbManager.getUserById(id);
        if (user != null && user.getRole() != User.UserRole.ADMIN) {
            return dbManager.deleteUser(id);
        }
        return false; // Cannot delete admin users
    }
    
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = dbManager.getUserById(userId);
        if (user != null && PasswordHasher.verifyPassword(oldPassword, user.getPasswordHash())) {
            user.setPasswordHash(PasswordHasher.hashPassword(newPassword));
            return dbManager.saveUser(user);
        }
        return false;
    }
    
    public boolean toggleUserStatus(String id) {
        User user = dbManager.getUserById(id);
        if (user != null && user.getRole() != User.UserRole.ADMIN) {
            user.setActive(!user.isActive());
            return dbManager.saveUser(user);
        }
        return false;
    }
    
    public List<User> getUsersByRole(User.UserRole role) {
        return getAllUsers().stream()
                           .filter(user -> user.getRole() == role)
                           .collect(java.util.ArrayList::new, 
                                   java.util.ArrayList::add, 
                                   java.util.ArrayList::addAll);
    }
    
    // Validation Methods
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return dbManager.getUserByUsername(username.trim()) == null;
    }
    
    public boolean isEmailAlreadyExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return getAllUsers().stream()
                           .anyMatch(user -> user.getEmail().equalsIgnoreCase(email.trim()));
    }
    
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        String trimmedUsername = username.trim();
        
        // Check length
        if (trimmedUsername.length() < 3 || trimmedUsername.length() > 20) {
            return false;
        }
        
        // Check format (alphanumeric and underscore only)
        return trimmedUsername.matches("^[a-zA-Z0-9_]+$");
    }
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email.trim());
    }
    
    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // Check length
        if (password.length() < 6 || password.length() > 50) {
            return false;
        }
        
        // Check for at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        
        return hasLetter && hasNumber;
    }
    
    private boolean isValidUserInput(String username, String email, String password) {
        return isValidUsername(username) && 
               isValidEmail(email) && 
               isValidPassword(password);
    }
    
    // User Statistics
    public int getTotalUserCount() {
        return getAllUsers().size();
    }
    
    public int getActiveUserCount() {
        return (int) getAllUsers().stream().filter(User::isActive).count();
    }
    
    public int getUserCountByRole(User.UserRole role) {
        return getUsersByRole(role).size();
    }
    
    // Password strength checker
    public PasswordStrength checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordStrength.VERY_WEAK;
        }
        
        int score = 0;
        
        // Length check
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        
        // Character variety checks
        if (password.matches(".*[a-z].*")) score++; // lowercase
        if (password.matches(".*[A-Z].*")) score++; // uppercase
        if (password.matches(".*[0-9].*")) score++; // numbers
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++; // special chars
        
        // Return strength based on score
        if (score <= 2) return PasswordStrength.WEAK;
        if (score <= 4) return PasswordStrength.MEDIUM;
        if (score <= 5) return PasswordStrength.STRONG;
        return PasswordStrength.VERY_STRONG;
    }
    
    public enum PasswordStrength {
        VERY_WEAK("Very Weak"),
        WEAK("Weak"),
        MEDIUM("Medium"),
        STRONG("Strong"),
        VERY_STRONG("Very Strong");
        
        private final String displayName;
        
        PasswordStrength(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
