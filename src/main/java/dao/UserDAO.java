package dao;

import models.User;
import utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User operations
 */
public class UserDAO {
    private DatabaseConnection dbConnection;
    
    public UserDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    // Create a new user
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, role, full_name, phone, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            int result = dbConnection.executeUpdate(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole().name(),
                user.getUsername(), // Using username as full_name for now
                "", // Empty phone for now
                user.isActive()
            );
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    // Get user by ID
    public User getUserById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return null;
    }
    
    // Get user by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return null;
    }
    
    // Get all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY created_date DESC";
        List<User> users = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return users;
    }
    
    // Update user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?, full_name = ?, phone = ?, is_active = ?, last_login = ? WHERE id = ?";
        
        try {
            int result = dbConnection.executeUpdate(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole().name(),
                user.getUsername(), // Using username as full_name
                "", // Empty phone
                user.isActive(),
                user.getLastLogin(),
                user.getId()
            );
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    // Delete user
    public boolean deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ? AND role != 'ADMIN'"; // Prevent admin deletion
        
        try {
            int result = dbConnection.executeUpdate(sql, id);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // Check if username exists
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return false;
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return false;
    }
    
    // Get users by role
    public List<User> getUsersByRole(User.UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_date DESC";
        List<User> users = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, role.name());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting users by role: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return users;
    }
    
    // Update last login
    public boolean updateLastLogin(String userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        
        try {
            int result = dbConnection.executeUpdate(sql, userId);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            return false;
        }
    }
    
    // Get user statistics
    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total user count: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return 0;
    }
    
    public int getActiveUserCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_active = TRUE";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = dbConnection.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active user count: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt);
        }
        return 0;
    }
    
    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        
        // Handle nullable timestamps
        Timestamp lastLoginTs = rs.getTimestamp("last_login");
        if (lastLoginTs != null) {
            user.setLastLogin(lastLoginTs.toLocalDateTime());
        }
        
        Timestamp createdDateTs = rs.getTimestamp("created_date");
        if (createdDateTs != null) {
            user.setCreatedDate(createdDateTs.toLocalDateTime());
        }
        
        return user;
    }
}
