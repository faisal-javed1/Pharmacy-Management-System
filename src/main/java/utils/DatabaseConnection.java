package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database Connection Manager for MySQL
 * Handles connection pooling and database operations
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pms";
    private static final String DB_USER = "root"; // Change as needed
    private static final String DB_PASSWORD = "12345678"; // Change as needed
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection properties
    private static final String CONNECTION_PROPERTIES = 
        "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    
    private DatabaseConnection() {
        try {
            initializeConnection();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private void initializeConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName(DB_DRIVER);
            
            // Create connection with properties
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("useUnicode", "true");
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("autoReconnect", "true");
            props.setProperty("failOverReadOnly", "false");
            props.setProperty("maxReconnects", "3");
            
            connection = DriverManager.getConnection(DB_URL + CONNECTION_PROPERTIES, props);
            
            // Test connection
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection established successfully!");
                
                // Set auto-commit to true for normal operations
                connection.setAutoCommit(true);
                
                // Test with a simple query
                testConnection();
            }
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Please add mysql-connector-java to your classpath.", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.err.println("Please ensure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'pharmacy_management_system' exists");
            System.err.println("3. Username and password are correct");
            System.err.println("4. MySQL JDBC driver is in classpath");
            throw e;
        }
    }
    
    private void testConnection() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 as test")) {
            
            if (rs.next()) {
                System.out.println("Database connection test successful!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
    }
    
    public Connection getConnection() {
        try {
            // Check if connection is still valid
            if (connection == null || connection.isClosed() || !connection.isValid(5)) {
                System.out.println("Reconnecting to database...");
                initializeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection validity: " + e.getMessage());
            try {
                initializeConnection();
            } catch (SQLException ex) {
                System.err.println("Failed to reconnect: " + ex.getMessage());
            }
        }
        return connection;
    }
    
    // Utility method to close resources
    public static void closeResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }
        
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing PreparedStatement: " + e.getMessage());
        }
        
        // Note: We don't close the main connection as it's managed by the singleton
        // Only close if it's a different connection instance
        try {
            if (conn != null && conn != instance.connection) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
    }
    
    public static void closeResources(PreparedStatement pstmt, Connection conn) {
        closeResources(null, pstmt, conn);
    }
    
    public static void closeResources(ResultSet rs, PreparedStatement pstmt) {
        closeResources(rs, pstmt, null);
    }
    
    // Execute a query and return ResultSet (caller must close resources)
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        
        // Set parameters
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        
        return pstmt.executeQuery();
    }
    
    // Execute an update/insert/delete query
    public int executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = getConnection().prepareStatement(sql);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
    
    // Execute insert and return generated key
    public String executeInsertWithGeneratedKey(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating record failed, no rows affected.");
            }
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new SQLException("Creating record failed, no ID obtained.");
            }
        } finally {
            closeResources(rs, pstmt);
        }
    }
    
    // Begin transaction
    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }
    
    // Commit transaction
    public void commitTransaction() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }
    
    // Rollback transaction
    public void rollbackTransaction() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
    }
    
    // Check if database exists and is accessible
    public boolean isDatabaseAccessible() {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users LIMIT 1")) {
            return true;
        } catch (SQLException e) {
            System.err.println("Database accessibility check failed: " + e.getMessage());
            return false;
        }
    }
    
    // Get database metadata
    public void printDatabaseInfo() {
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            System.out.println("=== Database Information ===");
            System.out.println("Database Product: " + metaData.getDatabaseProductName());
            System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + metaData.getDriverName());
            System.out.println("Driver Version: " + metaData.getDriverVersion());
            System.out.println("URL: " + metaData.getURL());
            System.out.println("Username: " + metaData.getUserName());
            System.out.println("============================");
        } catch (SQLException e) {
            System.err.println("Error getting database metadata: " + e.getMessage());
        }
    }
    
    // Close the connection (should be called on application shutdown)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    // Shutdown hook to ensure connection is closed
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.closeConnection();
            }
        }));
    }
}
