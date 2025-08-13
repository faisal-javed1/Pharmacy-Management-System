package views.medicines;

import controllers.MedicineController;
import models.Medicine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MedicineSearchPanel extends JPanel {
    private MedicineController medicineController;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JLabel resultCountLabel;
    
    public MedicineSearchPanel() {
        this.medicineController = new MedicineController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadMedicines();
    }
    
    private void initializeComponents() {
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Category filter
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        List<String> categories = medicineController.getCategories();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
        
        // Result count label
        resultCountLabel = new JLabel("0 medicines found");
        resultCountLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        // Medicine table
        String[] columnNames = {"ID", "Name", "Category", "Stock", "Price", "Expiry Date", "Supplier", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table for pharmacists
            }
        };
        
        medicineTable = new JTable(tableModel);
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicineTable.setRowHeight(25);
        medicineTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set column widths
        medicineTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        medicineTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        medicineTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Category
        medicineTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Stock
        medicineTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Price
        medicineTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Expiry
        medicineTable.getColumnModel().getColumn(6).setPreferredWidth(120); // Supplier
        medicineTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Status
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Medicine Search");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Medicines"));
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryComboBox);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(resultCountLabel);
        
        // Table Panel
        JScrollPane tableScrollPane = new JScrollPane(medicineTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        // Info Panel
        JPanel infoPanel = createInfoPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);
        
        // Adjust layout weights
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Medicine Details"));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel infoLabel = new JLabel("<html><center>Select a medicine from the table above to view details<br/>" +
                                    "Use this information to help customers and process sales</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createVerticalGlue());
        
        return infoPanel;
    }
    
    private void setupEventHandlers() {
        // Real-time search as user types
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        
        // Category filter
        categoryComboBox.addActionListener(e -> performSearch());
        
        // Table selection listener
        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showMedicineDetails();
            }
        });
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        
        List<Medicine> allMedicines = medicineController.getAllMedicines();
        List<Medicine> filteredMedicines = allMedicines.stream()
            .filter(medicine -> {
                boolean matchesSearch = searchTerm.isEmpty() || 
                    medicine.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    medicine.getId().toLowerCase().contains(searchTerm.toLowerCase());
                
                boolean matchesCategory = "All Categories".equals(selectedCategory) ||
                    medicine.getCategory().equals(selectedCategory);
                
                return matchesSearch && matchesCategory;
            })
            .collect(java.util.stream.Collectors.toList());
        
        updateTable(filteredMedicines);
        resultCountLabel.setText(filteredMedicines.size() + " medicines found");
    }
    
    private void updateTable(List<Medicine> medicines) {
        tableModel.setRowCount(0);
        
        for (Medicine medicine : medicines) {
            String status = getStockStatus(medicine);
            Object[] row = {
                medicine.getId(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getStock(),
                String.format("$%.2f", medicine.getPrice()),
                medicine.getExpiryDate().toString(),
                medicine.getSupplier(),
                status
            };
            tableModel.addRow(row);
        }
    }
    
    private String getStockStatus(Medicine medicine) {
        if (medicine.getStock() == 0) {
            return "Out of Stock";
        } else if (medicine.isLowStock()) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
    
    private void showMedicineDetails() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow >= 0) {
            String medicineId = (String) tableModel.getValueAt(selectedRow, 0);
            Medicine medicine = medicineController.getMedicineById(medicineId);
            
            if (medicine != null) {
                StringBuilder details = new StringBuilder();
                details.append("MEDICINE DETAILS\n");
                details.append("================\n\n");
                details.append("ID: ").append(medicine.getId()).append("\n");
                details.append("Name: ").append(medicine.getName()).append("\n");
                details.append("Category: ").append(medicine.getCategory()).append("\n");
                details.append("Price: $").append(String.format("%.2f", medicine.getPrice())).append("\n");
                details.append("Stock: ").append(medicine.getStock()).append(" units\n");
                details.append("Minimum Stock: ").append(medicine.getThreshold()).append(" units\n");
                details.append("Expiry Date: ").append(medicine.getExpiryDate()).append("\n");
                details.append("Supplier: ").append(medicine.getSupplier()).append("\n");
                details.append("Status: ").append(getStockStatus(medicine)).append("\n");
                
                if (medicine.getDescription() != null && !medicine.getDescription().isEmpty()) {
                    details.append("Description: ").append(medicine.getDescription()).append("\n");
                }
                
                // Warnings
                if (medicine.isOutOfStock()) {
                    details.append("\n⚠️ WARNING: This medicine is out of stock!");
                } else if (medicine.isLowStock()) {
                    details.append("\n⚠️ NOTICE: This medicine is running low on stock.");
                }
                
                if (medicine.isExpired()) {
                    details.append("\n❌ EXPIRED: This medicine has expired!");
                } else if (medicine.isExpiringSoon(30)) {
                    details.append("\n⚠️ EXPIRING SOON: This medicine expires within 30 days.");
                }
                
                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                
                JOptionPane.showMessageDialog(this, scrollPane, "Medicine Details", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void loadMedicines() {
        List<Medicine> allMedicines = medicineController.getAllMedicines();
        updateTable(allMedicines);
        resultCountLabel.setText(allMedicines.size() + " medicines found");
    }
    
    // Public method to get selected medicine (for use in sales)
    public Medicine getSelectedMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow >= 0) {
            String medicineId = (String) tableModel.getValueAt(selectedRow, 0);
            return medicineController.getMedicineById(medicineId);
        }
        return null;
    }
    
    // Public method to refresh the medicine list
    public void refreshMedicines() {
        loadMedicines();
        performSearch(); // Reapply current search/filter
    }
}
