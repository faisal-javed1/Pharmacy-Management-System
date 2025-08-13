# Pharmacy Management System

A comprehensive Java-based pharmacy management system with MySQL database integration, featuring role-based access control, inventory management, sales processing, and user management.

## 🚀 Features

### 🔐 **Authentication & Authorization**
- **Role-Based Access Control**: Admin and Pharmacist roles with different permissions
- **Secure Login System**: Password hashing and validation
- **User Registration**: Complete signup process with validation
- **Session Management**: Secure user sessions with automatic logout

### 💊 **Medicine Management**
- **Comprehensive Inventory**: Complete medicine database with categories
- **Stock Management**: Real-time stock tracking and updates
- **Search & Filter**: Advanced search by name, category, or ID
- **Expiry Tracking**: Monitor expired and expiring medicines
- **Low Stock Alerts**: Automatic alerts for medicines running low

### 🛒 **Sales Management**
- **Point of Sale**: Complete sales processing system
- **Shopping Cart**: Add/remove items with quantity management
- **Receipt Generation**: Detailed receipts for customers
- **Sales History**: Complete transaction history (Admin only)
- **Real-time Stock Updates**: Automatic inventory updates after sales

### 👥 **User Management** (Admin Only)
- **User Creation**: Add new users with role assignment
- **User Monitoring**: Track user activity and login history
- **Account Management**: Enable/disable user accounts
- **Role Management**: Assign and modify user roles

### 📊 **Reporting & Analytics** (Admin Only)
- **Sales Reports**: Detailed sales analytics and summaries
- **Inventory Reports**: Stock levels and inventory valuation
- **Low Stock Reports**: Critical stock alerts and recommendations
- **User Activity**: Monitor system usage and user actions

### 🏥 **Supplier Management** (Admin Only)
- **Supplier Database**: Complete supplier information management
- **Contact Management**: Track supplier contacts and details
- **Medicine Sourcing**: Link medicines to their suppliers

## 🛠️ Technology Stack

- **Backend**: Java 11+ with Swing GUI
- **Database**: MySQL 8.0+
- **Architecture**: MVC (Model-View-Controller) Pattern
- **Data Access**: DAO (Data Access Object) Pattern
- **Build Tool**: Maven
- **Dependencies**: MySQL Connector, HikariCP, Jackson, Apache Commons

## 📋 Prerequisites

- **Java Development Kit (JDK) 11 or higher**
- **MySQL Server 8.0 or higher**
- **Maven 3.6 or higher**
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🚀 Installation & Setup

### 1. Clone the Repository
\`\`\`bash
git clone https://github.com/your-username/pharmacy-management-system.git
cd pharmacy-management-system
\`\`\`

### 2. Database Setup
\`\`\`bash
# Start MySQL server
sudo systemctl start mysql  # Linux
# or
brew services start mysql   # macOS

# Create database and user
mysql -u root -p
\`\`\`

\`\`\`sql
-- Create database
CREATE DATABASE pharmacy_management_system;

-- Create user (optional)
CREATE USER 'pharmacy_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON pharmacy_management_system.* TO 'pharmacy_user'@'localhost';
FLUSH PRIVILEGES;
\`\`\`

### 3. Run Database Scripts
\`\`\`bash
# Navigate to project directory
cd pharmacy-management-system

# Run schema creation
mysql -u root -p pharmacy_management_system < database/schema.sql

# Run sample data insertion
mysql -u root -p pharmacy_management_system < database/seed_data.sql
\`\`\`

### 4. Configure Database Connection
Edit `src/main/java/utils/DatabaseConnection.java`:
\`\`\`java
private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy_management_system";
private static final String DB_USER = "root"; // Your MySQL username
private static final String DB_PASSWORD = ""; // Your MySQL password
\`\`\`

### 5. Build and Run
\`\`\`bash
# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="views.LoginFrame"

# Or create executable JAR
mvn clean package
java -jar target/pharmacy-management-system-1.0.0-jar-with-dependencies.jar
\`\`\`

## 👤 Default Login Credentials

### Administrator Account
- **Username**: `admin`
- **Password**: `admin123`
- **Permissions**: Full system access

### Pharmacist Account
- **Username**: `pharmacist`
- **Password**: `pharma123`
- **Permissions**: Medicine search and sales processing

## 📖 Usage Guide

### For Administrators
1. **Login** with admin credentials
2. **Manage Inventory**: Add, edit, and monitor medicines
3. **Process Sales**: Handle customer transactions
4. **User Management**: Create and manage user accounts
5. **View Reports**: Access comprehensive system reports
6. **Monitor Alerts**: Check low stock and system alerts

### For Pharmacists
1. **Login** with pharmacist credentials
2. **Search Medicines**: Find medicines for customers
3. **Process Sales**: Complete customer transactions
4. **Generate Receipts**: Print customer receipts
5. **View Medicine Details**: Access medicine information

## 🗄️ Database Schema

### Core Tables
- **users**: User accounts and authentication
- **medicines**: Medicine inventory and details
- **suppliers**: Supplier information and contacts
- **sales**: Sales transactions and history
- **sale_items**: Individual items in each sale
- **low_stock_alerts**: Automated stock alerts

### Key Features
- **Stored Procedures**: Automated stock management
- **Triggers**: Auto-generated IDs and timestamps
- **Views**: Optimized queries for common operations
- **Indexes**: Performance optimization for searches

## 🔧 Configuration

### Database Configuration
\`\`\`java
// DatabaseConnection.java
private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy_management_system";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
\`\`\`

### Application Settings
- **Connection Pooling**: HikariCP for optimal performance
- **Auto-reconnection**: Automatic database reconnection
- **Transaction Management**: ACID compliance for data integrity
- **Error Handling**: Comprehensive error logging and recovery

## 🧪 Testing

\`\`\`bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserControllerTest

# Generate test coverage report
mvn jacoco:report
\`\`\`

## 📦 Building for Production

\`\`\`bash
# Create production build
mvn clean package -Pproduction

# The executable JAR will be in target/ directory
java -jar target/pharmacy-management-system-1.0.0-jar-with-dependencies.jar
\`\`\`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues:

1. **Check Prerequisites**: Ensure all requirements are met
2. **Database Connection**: Verify MySQL is running and accessible
3. **Logs**: Check console output for error messages
4. **Documentation**: Review this README and code comments
5. **Issues**: Create a GitHub issue with detailed information

## 🔮 Future Enhancements

- **Web Interface**: REST API and web-based frontend
- **Barcode Scanning**: Integration with barcode scanners
- **Email Notifications**: Automated alerts and reports
- **Multi-location Support**: Support for multiple pharmacy branches
- **Advanced Analytics**: Business intelligence and forecasting
- **Mobile App**: Android/iOS companion applications

## 📊 System Requirements

### Minimum Requirements
- **RAM**: 2GB
- **Storage**: 500MB free space
- **Java**: JDK 11+
- **MySQL**: 8.0+

### Recommended Requirements
- **RAM**: 4GB or higher
- **Storage**: 1GB free space
- **Java**: JDK 17+
- **MySQL**: 8.0+ with SSD storage

---

**Developed with ❤️ for efficient pharmacy management**
\`\`\`

## 🎯 **Database Integration Complete!**

I've successfully created a comprehensive MySQL database integration for the Pharmacy Management System with the following components:

### **📊 Database Schema**
- **Complete MySQL schema** with tables, views, stored procedures, and triggers
- **Optimized indexes** for better query performance
- **Foreign key relationships** ensuring data integrity
- **Auto-generated IDs** using triggers
- **Sample data** for immediate testing

### **🔗 Database Connection**
- **Singleton connection manager** with auto-reconnection
- **Connection pooling** support with HikariCP
- **Transaction management** for data consistency
- **Error handling** and logging
- **Resource management** to prevent memory leaks

### **📁 Data Access Layer**
- **DAO pattern implementation** for clean separation
- **UserDAO** and **MedicineDAO** with full CRUD operations
- **Prepared statements** to prevent SQL injection
- **Result set mapping** to model objects
- **Search and filter capabilities**

### **⚙️ Build Configuration**
- **Maven POM** with all necessary dependencies
- **MySQL Connector** and connection pooling
- **Build plugins** for executable JAR creation
- **SQL Maven plugin** for automated database setup
- **Development and production profiles**

### **📖 Documentation**
- **Comprehensive README** with setup instructions
- **Database setup guide** with SQL commands
- **Configuration instructions** for different environments
- **Usage guide** for administrators and pharmacists

The system now provides:
- **Persistent data storage** replacing in-memory storage
- **Professional database design** with proper relationships
- **Automated stock management** using stored procedures
- **Performance optimization** with indexes and views
- **Data integrity** with constraints and transactions
- **Easy deployment** with Maven build system

To get started, simply run the SQL scripts to create the database, configure the connection settings, and build the project with Maven!
