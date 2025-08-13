package views.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import controllers.MedicineController;
import controllers.SaleController;
import models.Medicine;
import models.Sale;
import models.User;

public class SalesManagementPanel extends JPanel {
    private SaleController saleController;
    private MedicineController medicineController;
    private User currentUser;
    
    // Components for new sale

    
    private JTextField customerNameField;
    private JComboBox<Medicine> medicineComboBox;
    private JSpinner quantitySpinner;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private JButton addToCartButton;
    private JButton removeFromCartButton;
    private JButton completeSaleButton;
    private JButton clearCartButton;
    
    // Components for sales history (Admin only)
    private JTable salesHistoryTable;
    private DefaultTableModel salesHistoryTableModel;
    
    // Current sale data
    private List<CartItem> cartItems;
    private double currentTotal;
    
    public SalesManagementPanel(User user) {
        this.currentUser = user;
        this.saleController = new SaleController();
        this.medicineController = new MedicineController();
        this.cartItems = new ArrayList<>();
        this.currentTotal = 0.0;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadMedicines();
        
        if (user.getRole() == User.UserRole.ADMIN) {
            loadSalesHistory();
        }
    }
    
    private void initializeComponents() {
        // New Sale Components
        customerNameField = new JTextField(20);
        medicineComboBox = new JComboBox<>();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        
        addToCartButton = new JButton("Add to Cart");
        removeFromCartButton = new JButton("Remove Selected");
        completeSaleButton = new JButton("Complete Sale");
        clearCartButton = new JButton("Clear Cart");
        
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(46, 204, 113));
        
        // Cart Table
        String[] cartColumns = {"Medicine", "Price", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sales History Table (Admin only)
        if (currentUser.getRole() == User.UserRole.ADMIN) {
            String[] historyColumns = {"Invoice ID", "Date", "Customer", "Items", "Total", "Status", "Cashier"};
            salesHistoryTableModel = new DefaultTableModel(historyColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            salesHistoryTable = new JTable(salesHistoryTableModel);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        if (currentUser.getRole() == User.UserRole.PHARMACIST) {
            setupPharmacistLayout();
        } else {
            setupAdminLayout();
        }
    }
    
    private void setupPharmacistLayout() {
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Sales Management - Process Customer Sales");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // New Sale Panel
        JPanel newSalePanel = createNewSalePanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(newSalePanel, BorderLayout.CENTER);
    }
    
    private void setupAdminLayout() {
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Sales Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // Tabbed Pane for Admin
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("New Sale", createNewSalePanel());
        tabbedPane.addTab("Sales History", createSalesHistoryPanel());
        
        add(titlePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createNewSalePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Customer Info Panel
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        customerPanel.add(new JLabel("Customer Name:"));
        customerPanel.add(customerNameField);
        
        // Add Medicine Panel
        JPanel addMedicinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addMedicinePanel.setBorder(BorderFactory.createTitledBorder("Add Medicine to Cart"));
        addMedicinePanel.add(new JLabel("Medicine:"));
        addMedicinePanel.add(medicineComboBox);
        addMedicinePanel.add(new JLabel("Quantity:"));
        addMedicinePanel.add(quantitySpinner);
        addMedicinePanel.add(addToCartButton);
        
        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(customerPanel, BorderLayout.NORTH);
        topPanel.add(addMedicinePanel, BorderLayout.SOUTH);
        
        // Cart Panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setPreferredSize(new Dimension(0, 200));
        
        JPanel cartButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartButtonPanel.add(removeFromCartButton);
        cartButtonPanel.add(clearCartButton);
        
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);
        
        // Total and Complete Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);
        
        JPanel completePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        completePanel.add(completeSaleButton);
        
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(completePanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(cartPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSalesHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Sales History"));
        
        JScrollPane scrollPane = new JScrollPane(salesHistoryTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        JButton viewDetailsButton = new JButton("View Details");
        JButton printReceiptButton = new JButton("Print Receipt");
        
        refreshButton.addActionListener(e -> loadSalesHistory());
        viewDetailsButton.addActionListener(e -> viewSaleDetails());
        printReceiptButton.addActionListener(e -> printReceipt());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(printReceiptButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        clearCartButton.addActionListener(e -> clearCart());
        completeSaleButton.addActionListener(e -> completeSale());
        
        // Update buttons state
        updateButtonStates();
    }
    
    private void loadMedicines() {
        medicineComboBox.removeAllItems();
        List<Medicine> medicines = medicineController.getAllMedicines();
        
        for (Medicine medicine : medicines) {
            if (medicine.getStock() > 0) { // Only show medicines with stock
                medicineComboBox.addItem(medicine);
            }
        }
        
        // Custom renderer to show medicine details
        medicineComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Medicine) {
                    Medicine medicine = (Medicine) value;
                    setText(medicine.getName() + " - $" + String.format("%.2f", medicine.getPrice()) + 
                           " (Stock: " + medicine.getStock() + ")");
                }
                return this;
            }
        });
    }
    
    private void addToCart() {
        Medicine selectedMedicine = (Medicine) medicineComboBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();
        
        if (selectedMedicine == null) {
            JOptionPane.showMessageDialog(this, "Please select a medicine.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (quantity > selectedMedicine.getStock()) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock. Available: " + selectedMedicine.getStock(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if medicine already in cart
        CartItem existingItem = cartItems.stream()
            .filter(item -> item.getMedicine().getId().equals(selectedMedicine.getId()))
            .findFirst()
            .orElse(null);
        
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > selectedMedicine.getStock()) {
                JOptionPane.showMessageDialog(this, 
                    "Total quantity would exceed available stock. Available: " + selectedMedicine.getStock(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            existingItem.setQuantity(newQuantity);
        } else {
            cartItems.add(new CartItem(selectedMedicine, quantity));
        }
        
        updateCartTable();
        quantitySpinner.setValue(1);
        updateButtonStates();
    }
    
    private void removeFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            cartItems.remove(selectedRow);
            updateCartTable();
            updateButtonStates();
        }
    }
    
    private void clearCart() {
        cartItems.clear();
        updateCartTable();
        updateButtonStates();
    }
    
    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        currentTotal = 0.0;
        
        for (CartItem item : cartItems) {
            double subtotal = item.getMedicine().getPrice() * item.getQuantity();
            currentTotal += subtotal;
            
            Object[] row = {
                item.getMedicine().getName(),
                String.format("$%.2f", item.getMedicine().getPrice()),
                item.getQuantity(),
                String.format("$%.2f", subtotal)
            };
            cartTableModel.addRow(row);
        }
        
        totalLabel.setText("Total: $" + String.format("%.2f", currentTotal));
    }
    
    private void completeSale() {
        String customerName = customerNameField.getText().trim();
        
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create new sale
        Sale sale = saleController.createNewSale(customerName, currentUser.getId());
        
        // Add items to sale
        boolean success = true;
        for (CartItem item : cartItems) {
            if (!saleController.addItemToSale(sale.getId(), item.getMedicine().getId(), item.getQuantity())) {
                success = false;
                break;
            }
        }
        
        if (success && saleController.completeSale(sale.getId())) {
            // Show receipt
            showReceipt(sale);
            
            // Clear form
            customerNameField.setText("");
            clearCart();
            loadMedicines(); // Refresh medicine list with updated stock
            
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                loadSalesHistory(); // Refresh sales history
            }
            
            JOptionPane.showMessageDialog(this, "Sale completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to complete sale. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showReceipt(Sale sale) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("PHARMACY RECEIPT\n");
        receipt.append("================\n\n");
        receipt.append("Invoice ID: ").append(sale.getId()).append("\n");
        receipt.append("Date: ").append(sale.getSaleDate().toLocalDate()).append("\n");
        receipt.append("Time: ").append(sale.getSaleDate().toLocalTime().toString().substring(0, 8)).append("\n");
        receipt.append("Customer: ").append(sale.getCustomerName()).append("\n");
        receipt.append("Cashier: ").append(currentUser.getUsername()).append("\n\n");
        
        receipt.append("ITEMS:\n");
        receipt.append("------\n");
        for (Sale.SaleItem item : sale.getItems()) {
            receipt.append(String.format("%-20s %2d x $%6.2f = $%7.2f\n", 
                item.getMedicineName(), 
                item.getQuantity(), 
                item.getPrice(), 
                item.getSubtotal()));
        }
        
        receipt.append("\n");
        receipt.append(String.format("TOTAL: $%.2f\n", sale.getFinalAmount()));
        receipt.append("\nThank you for your business!\n");
        
        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadSalesHistory() {
        if (salesHistoryTableModel == null) return;
        
        salesHistoryTableModel.setRowCount(0);
        List<Sale> sales = saleController.getAllSales();
        
        for (Sale sale : sales) {
            Object[] row = {
                sale.getId(),
                sale.getSaleDate().toLocalDate().toString(),
                sale.getCustomerName(),
                sale.getItems().size() + " items",
                String.format("$%.2f", sale.getFinalAmount()),
                sale.getStatus().toString(),
                sale.getCashierId()
            };
            salesHistoryTableModel.addRow(row);
        }
    }
    
    private void viewSaleDetails() {
        int selectedRow = salesHistoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            String saleId = (String) salesHistoryTableModel.getValueAt(selectedRow, 0);
            Sale sale = saleController.getSaleById(saleId);
            
            if (sale != null) {
                showReceipt(sale);
            }
        }
    }
    
    private void printReceipt() {
        int selectedRow = salesHistoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(this, "Print functionality would be implemented here.", 
                                        "Print Receipt", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateButtonStates() {
        boolean hasItems = !cartItems.isEmpty();
        boolean hasCustomer = !customerNameField.getText().trim().isEmpty();
        
        removeFromCartButton.setEnabled(hasItems);
        clearCartButton.setEnabled(hasItems);
        completeSaleButton.setEnabled(hasItems && hasCustomer);
    }
    
    // Inner class for cart items
    private static class CartItem {
        private Medicine medicine;
        private int quantity;
        
        public CartItem(Medicine medicine, int quantity) {
            this.medicine = medicine;
            this.quantity = quantity;
        }
        
        public Medicine getMedicine() { return medicine; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
