package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controllers.UserController;
import models.User;

public class LoginFrame extends JFrame {
    private final UserController userController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    
    public LoginFrame() {
        this.userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        // Custom field styling
        usernameField = new JTextField(20);
        customizeTextField(usernameField);
        
        passwordField = new JPasswordField(20);
        customizeTextField(passwordField);
        
        loginButton = new JButton("LOGIN");
        customizeButton(loginButton, new Color(52, 152, 219));
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void customizeTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(Color.WHITE);
    }
    
    private void customizeButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("PHARMACY MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Login Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Title
        JLabel formTitle = new JLabel("Sign In to Your Account");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);
        
        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(usernameField, gbc);
        
        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(loginButton, gbc);
        
        // Status Label
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(statusLabel, gbc);
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        
        // Sign Up Button
        JButton signUpButton = new JButton("CREATE NEW ACCOUNT");
        customizeButton(signUpButton, new Color(46, 204, 113));
        signUpButton.addActionListener(e -> openSignUpForm());
        
        // Demo Panel
        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(new Color(245, 245, 245));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel demoTitle = new JLabel("Demo Credentials");
        demoTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        demoTitle.setForeground(new Color(100, 100, 100));
        
        JLabel demoText = new JLabel("<html><div style='text-align:center;'>"
            + "<b>Admin:</b> admin/admin123<br/>"
            + "<b>Pharmacist:</b> pharmacist/pharma123</div></html>");
        demoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        demoText.setForeground(new Color(120, 120, 120));
        
        demoPanel.add(demoTitle);
        demoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        demoPanel.add(demoText);
        
        // Add components to content panel
        contentPanel.add(formPanel);
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(signUpButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(demoPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> performLogin());
        
        // Enter key support
        ActionListener loginAction = e -> performLogin();
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }

    private void openSignUpForm() {
        new SignUpFrame(this).setVisible(true);
        this.setVisible(false);
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatusMessage("Please enter both username and password", false);
            return;
        }
        
        User user = userController.authenticateUser(username, password);
        if (user != null) {
            showStatusMessage("Login successful! Redirecting...", true);
            
            // Open main dashboard
            SwingUtilities.invokeLater(() -> {
                new MainDashboardFrame(user).setVisible(true);
                dispose();
            });
        } else {
            showStatusMessage("Invalid username or password", false);
            passwordField.setText("");
        }
    }
    
    private void showStatusMessage(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setForeground(isSuccess ? new Color(46, 204, 113) : new Color(231, 76, 60));
    }
    
    private void setFrameProperties() {
        setTitle("Pharmacy Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 650);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        
        // Set application icon
        try {
            // Load from resources folder
            // ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icon.png"));
            // setIconImage(icon.getImage());
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Custom UI improvements
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 5);
            UIManager.put("ProgressBar.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
            
            // Center on screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(
                (screenSize.width - frame.getWidth()) / 2,
                (screenSize.height - frame.getHeight()) / 2
            );
        });
    }
}