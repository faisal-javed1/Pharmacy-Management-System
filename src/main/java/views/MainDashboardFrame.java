package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import models.User;
import utils.DatabaseManager;
import views.medicines.MedicineSearchPanel;
import views.sales.SalesManagementPanel;

public class MainDashboardFrame extends JFrame {
    private User currentUser;
    private DatabaseManager dbManager;
    private JTabbedPane tabbedPane;
    private JLabel userInfoLabel;
    private JLabel statsLabel;
    
    public MainDashboardFrame(User user) {
        this.currentUser = user;
        this.dbManager = DatabaseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
        updateStats();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        userInfoLabel = new JLabel();
        statsLabel = new JLabel();
        updateUserInfo();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content - Tabbed Pane
        setupTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        
        // Status Bar
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Pharmacy Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        // User Info Panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(userInfoLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void setupTabbedPane() {
        // Role-based tab setup
        if (currentUser.getRole() == User.UserRole.ADMIN) {
            setupAdminTabs();
        } else if (currentUser.getRole() == User.UserRole.PHARMACIST) {
            setupPharmacistTabs();
        }
        
        // Set tab properties
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    private void setupAdminTabs() {
        // Dashboard Overview
        tabbedPane.addTab("Dashboard", createAdminDashboardPanel());
        
        
        // Sales Management
        tabbedPane.addTab("Sales", new SalesManagementPanel(currentUser));
        
        
        // Reports
        tabbedPane.addTab("Reports", createReportsPanel());
    }
    
    private void setupPharmacistTabs() {
        // Simple Dashboard for Pharmacist
        tabbedPane.addTab("Dashboard", createPharmacistDashboardPanel());
        
        // Medicine Search (Read-only)
        tabbedPane.addTab("Medicine Search", new MedicineSearchPanel());
        
        // Sales Management (Process sales only)
        tabbedPane.addTab("Sales", new SalesManagementPanel(currentUser));
    }
    
    private JPanel createAdminDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Stats Panel
        JPanel statsPanel = createAdminStatsPanel();
        dashboardPanel.add(statsPanel, BorderLayout.NORTH);
        
        // Quick Actions Panel
        JPanel quickActionsPanel = createAdminQuickActionsPanel();
        dashboardPanel.add(quickActionsPanel, BorderLayout.CENTER);
        
        return dashboardPanel;
    }
    
    private JPanel createPharmacistDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(BorderFactory.createTitledBorder("Welcome"));
        JLabel welcomeLabel = new JLabel("<html><center>Welcome to Pharmacy Management System<br/>" +
                                       "Use the tabs above to search medicines and process sales</center></html>");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomePanel.add(welcomeLabel);
        
        // Quick Stats for Pharmacist
        JPanel quickStatsPanel = createPharmacistStatsPanel();
        
        // Quick Actions for Pharmacist
        JPanel quickActionsPanel = createPharmacistQuickActionsPanel();
        
        dashboardPanel.add(welcomePanel, BorderLayout.NORTH);
        dashboardPanel.add(quickStatsPanel, BorderLayout.CENTER);
        dashboardPanel.add(quickActionsPanel, BorderLayout.SOUTH);
        
        return dashboardPanel;
    }
    
    private JPanel createAdminStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Overview"));
        
        // Total Medicines
        JPanel medicineStatsPanel = createStatCard("Total Medicines", 
                                                  String.valueOf(dbManager.getTotalMedicines()),
                                                  new Color(52, 152, 219));
        
        // Low Stock Items
        JPanel lowStockPanel = createStatCard("Low Stock Items", 
                                            String.valueOf(dbManager.getLowStockCount()),
                                            new Color(231, 76, 60));
        
        // Today's Sales
        JPanel salesPanel = createStatCard("Today's Sales", 
                                         String.format("$%.2f", dbManager.getTodaysSales()),
                                         new Color(46, 204, 113));
        
        // Active Suppliers
        JPanel suppliersPanel = createStatCard("Active Suppliers", 
                                             String.valueOf(dbManager.getActiveSuppliers()),
                                             new Color(155, 89, 182));
        
        statsPanel.add(medicineStatsPanel);
        statsPanel.add(lowStockPanel);
        statsPanel.add(salesPanel);
        statsPanel.add(suppliersPanel);
        
        return statsPanel;
    }
    
    private JPanel createPharmacistStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Quick Stats"));
        
        // Available Medicines
        JPanel medicineStatsPanel = createStatCard("Available Medicines", 
                                                  String.valueOf(dbManager.getTotalMedicines()),
                                                  new Color(52, 152, 219));
        
        // Today's Sales
        JPanel salesPanel = createStatCard("Today's Sales", 
                                         String.format("$%.2f", dbManager.getTodaysSales()),
                                         new Color(46, 204, 113));
        
        statsPanel.add(medicineStatsPanel);
        statsPanel.add(salesPanel);
        
        return statsPanel;
    }
    
    private JPanel createAdminQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        
        // Add Medicine Button
        JButton addMedicineBtn = createActionButton("Add Medicine", "Add new medicine to inventory");
        addMedicineBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        // New Sale Button
        JButton newSaleBtn = createActionButton("New Sale", "Create a new sale transaction");
        newSaleBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        
        // View Alerts Button
        JButton alertsBtn = createActionButton("View Alerts", "Check low stock alerts");
        alertsBtn.addActionListener(e -> tabbedPane.setSelectedIndex(5));
        
        // Add Supplier Button
        JButton addSupplierBtn = createActionButton("Add Supplier", "Add new supplier");
        addSupplierBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        
        // Add User Button
        JButton addUserBtn = createActionButton("Add User", "Add new user");
        addUserBtn.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        
        // Reports Button
        JButton reportsBtn = createActionButton("Reports", "Generate reports");
        reportsBtn.addActionListener(e -> tabbedPane.setSelectedIndex(6));
        
        actionsPanel.add(addMedicineBtn);
        actionsPanel.add(newSaleBtn);
        actionsPanel.add(alertsBtn);
        actionsPanel.add(addSupplierBtn);
        actionsPanel.add(addUserBtn);
        actionsPanel.add(reportsBtn);
        
        return actionsPanel;
    }
    
    private JPanel createPharmacistQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        
        // Search Medicine Button
        JButton searchMedicineBtn = createActionButton("Search Medicine", "Search for medicines");
        searchMedicineBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        // New Sale Button
        JButton newSaleBtn = createActionButton("New Sale", "Process a new sale");
        newSaleBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        
        actionsPanel.add(searchMedicineBtn);
        actionsPanel.add(newSaleBtn);
        
        return actionsPanel;
    }
    
    private JPanel createReportsPanel() {
        JPanel reportsPanel = new JPanel(new BorderLayout());
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Reports & Analytics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton salesReportBtn = new JButton("Sales Report");
        JButton inventoryReportBtn = new JButton("Inventory Report");
        JButton lowStockReportBtn = new JButton("Low Stock Report");
        JButton supplierReportBtn = new JButton("Supplier Report");
        JButton userActivityBtn = new JButton("User Activity");
        JButton financialReportBtn = new JButton("Financial Report");
        
        // Add action listeners for reports
        salesReportBtn.addActionListener(e -> showSalesReport());
        inventoryReportBtn.addActionListener(e -> showInventoryReport());
        lowStockReportBtn.addActionListener(e -> showLowStockReport());
        
        buttonPanel.add(salesReportBtn);
        buttonPanel.add(inventoryReportBtn);
        buttonPanel.add(lowStockReportBtn);
        buttonPanel.add(supplierReportBtn);
        buttonPanel.add(userActivityBtn);
        buttonPanel.add(financialReportBtn);
        
        reportsPanel.add(titleLabel, BorderLayout.NORTH);
        reportsPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return reportsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String title, String tooltip) {
        JButton button = new JButton("<html><center>" + title + "</center></html>");
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(120, 60));
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setBackground(new Color(236, 240, 241));
        button.setFocusPainted(false);
        return button;
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setBackground(Color.LIGHT_GRAY);
        
        statsLabel.setText("Ready");
        statsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel versionLabel = new JLabel("Version 1.0 - " + currentUser.getRole().getDisplayName() + " Mode");
        versionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        statusPanel.add(statsLabel, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
    
    private void setupEventHandlers() {
        // Tab change listener to update stats
        tabbedPane.addChangeListener(e -> updateStats());
    }
    
    private void updateUserInfo() {
        userInfoLabel.setText("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole().getDisplayName() + ")");
    }
    
    private void updateStats() {
        SwingUtilities.invokeLater(() -> {
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                statsLabel.setText("Medicines: " + dbManager.getTotalMedicines() + 
                                 " | Low Stock: " + dbManager.getLowStockCount() + 
                                 " | Today's Sales: $" + String.format("%.2f", dbManager.getTodaysSales()));
            } else {
                statsLabel.setText("Today's Sales: $" + String.format("%.2f", dbManager.getTodaysSales()) +
                                 " | Available Medicines: " + dbManager.getTotalMedicines());
            }
        });
    }
    
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to logout?", 
                                                  "Confirm Logout", 
                                                  JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    private void showSalesReport() {
        StringBuilder report = new StringBuilder();
        report.append("SALES REPORT\n");
        report.append("=============\n\n");
        report.append("Total Sales: ").append(dbManager.getAllSales().size()).append("\n");
        report.append("Today's Sales: $").append(String.format("%.2f", dbManager.getTodaysSales())).append("\n");
        report.append("Total Revenue: $").append(String.format("%.2f", 
            dbManager.getAllSales().stream()
                   .mapToDouble(sale -> sale.getFinalAmount())
                   .sum())).append("\n");
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Sales Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInventoryReport() {
        StringBuilder report = new StringBuilder();
        report.append("INVENTORY REPORT\n");
        report.append("================\n\n");
        report.append("Total Medicines: ").append(dbManager.getTotalMedicines()).append("\n");
        report.append("Low Stock Items: ").append(dbManager.getLowStockCount()).append("\n");
        report.append("Total Inventory Value: $").append(String.format("%.2f",
            dbManager.getAllMedicines().stream()
                   .mapToDouble(med -> med.getPrice() * med.getStock())
                   .sum())).append("\n\n");
        
        report.append("LOW STOCK MEDICINES:\n");
        report.append("--------------------\n");
        dbManager.getAllMedicines().stream()
               .filter(med -> med.isLowStock())
               .forEach(med -> report.append(med.getName())
                                   .append(" - Stock: ").append(med.getStock())
                                   .append(" (Min: ").append(med.getThreshold()).append(")\n"));
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Inventory Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showLowStockReport() {
        StringBuilder report = new StringBuilder();
        report.append("LOW STOCK ALERT REPORT\n");
        report.append("======================\n\n");
        
        long criticalCount = dbManager.getAllMedicines().stream()
                                   .filter(med -> med.getStock() == 0)
                                   .count();
        long lowStockCount = dbManager.getAllMedicines().stream()
                                   .filter(med -> med.isLowStock() && med.getStock() > 0)
                                   .count();
        
        report.append("Critical (Out of Stock): ").append(criticalCount).append("\n");
        report.append("Low Stock: ").append(lowStockCount).append("\n\n");
        
        report.append("CRITICAL ITEMS (OUT OF STOCK):\n");
        report.append("------------------------------\n");
        dbManager.getAllMedicines().stream()
               .filter(med -> med.getStock() == 0)
               .forEach(med -> report.append("• ").append(med.getName())
                                   .append(" - Supplier: ").append(med.getSupplier()).append("\n"));
        
        report.append("\nLOW STOCK ITEMS:\n");
        report.append("----------------\n");
        dbManager.getAllMedicines().stream()
               .filter(med -> med.isLowStock() && med.getStock() > 0)
               .forEach(med -> report.append("• ").append(med.getName())
                                   .append(" - Stock: ").append(med.getStock())
                                   .append(" (Min: ").append(med.getThreshold()).append(")\n"));
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Low Stock Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setFrameProperties() {
        setTitle("Pharmacy Management System - " + currentUser.getRole().getDisplayName() + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        
        // Set window icon
        try {
            // setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
}
