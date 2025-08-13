package views;

import controllers.UserController;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class SignUpFrame extends JFrame {
    private UserController userController;
    private LoginFrame parentFrame;
    
    // Form components
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<User.UserRole> roleComboBox;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JButton signUpButton;
    private JButton backToLoginButton;
    private JLabel statusLabel;
    private JCheckBox termsCheckBox;
    
    // Validation components
    private JLabel usernameValidationLabel;
    private JLabel emailValidationLabel;
    private JLabel passwordValidationLabel;
    private JLabel confirmPasswordValidationLabel;
    
    public SignUpFrame(LoginFrame parent) {
        this.parentFrame = parent;
        this.userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        // Form fields
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        phoneField = new JTextField(20);
        
        // Role selection - Only allow Admin and Pharmacist
        roleComboBox = new JComboBox<>(User.UserRole.values());
        roleComboBox.setSelectedItem(User.UserRole.PHARMACIST); // Default to Pharmacist
        
        // Buttons
        signUpButton = new JButton("Create Account");
        backToLoginButton = new JButton("Back to Login");
        
        // Status and validation labels
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        
        usernameValidationLabel = new JLabel(" ");
        emailValidationLabel = new JLabel(" ");
        passwordValidationLabel = new JLabel(" ");
        confirmPasswordValidationLabel = new JLabel(" ");
        
        // Set validation label properties
        Font validationFont = new Font("Arial", Font.PLAIN, 10);
        usernameValidationLabel.setFont(validationFont);
        emailValidationLabel.setFont(validationFont);
        passwordValidationLabel.setFont(validationFont);
        confirmPasswordValidationLabel.setFont(validationFont);
        
        // Terms and conditions checkbox
        termsCheckBox = new JCheckBox("<html>I agree to the <u>Terms and Conditions</u> and <u>Privacy Policy</u></html>");
        termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 11));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main Form Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 2, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(fullNameField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);
        
        // Username validation
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 8, 10);
        mainPanel.add(usernameValidationLabel, gbc);
        
        // Email
        gbc.insets = new Insets(8, 10, 2, 10);
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Email validation
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(0, 10, 8, 10);
        mainPanel.add(emailValidationLabel, gbc);
        
        // Phone
        gbc.insets = new Insets(8, 10, 2, 10);
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
        
        // Password validation
        gbc.gridx = 1; gbc.gridy = 8;
        gbc.insets = new Insets(0, 10, 8, 10);
        mainPanel.add(passwordValidationLabel, gbc);
        
        // Confirm Password
        gbc.insets = new Insets(8, 10, 2, 10);
        gbc.gridx = 0; gbc.gridy = 9;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);
        
        // Confirm Password validation
        gbc.gridx = 1; gbc.gridy = 10;
        gbc.insets = new Insets(0, 10, 8, 10);
        mainPanel.add(confirmPasswordValidationLabel, gbc);
        
        // Terms checkbox
        gbc.insets = new Insets(15, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(termsCheckBox, gbc);
        
        // Sign Up Button
        gbc.gridy = 12;
        gbc.insets = new Insets(20, 10, 10, 10);
        signUpButton.setBackground(new Color(46, 204, 113));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(200, 35));
        mainPanel.add(signUpButton, gbc);
        
        // Back to Login Button
        gbc.gridy = 13;
        gbc.insets = new Insets(10, 10, 10, 10);
        backToLoginButton.setBackground(new Color(149, 165, 166));
        backToLoginButton.setForeground(Color.WHITE);
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backToLoginButton.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(backToLoginButton, gbc);
        
        // Status Label
        gbc.gridy = 14;
        gbc.insets = new Insets(15, 10, 10, 10);
        mainPanel.add(statusLabel, gbc);
        
        // Role Information Panel
        JPanel roleInfoPanel = createRoleInfoPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(roleInfoPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createRoleInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Role Information"));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));
        
        JLabel adminInfo = new JLabel("• Administrator: Full system access - manage inventory, users, suppliers, and reports");
        JLabel pharmacistInfo = new JLabel("• Pharmacist: Limited access - search medicines and process customer sales");
        
        adminInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        pharmacistInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        
        adminInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
        pharmacistInfo.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));
        
        infoPanel.add(adminInfo);
        infoPanel.add(pharmacistInfo);
        
        return infoPanel;
    }
    
    private void setupEventHandlers() {
        // Real-time validation
        usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validateUsername();
            }
        });
        
        emailField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validateEmail();
            }
        });
        
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validatePassword();
            }
        });
        
        confirmPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                validateConfirmPassword();
            }
        });
        
        // Button actions
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSignUp();
            }
        });
        
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToLogin();
            }
        });
        
        // Terms checkbox action
        termsCheckBox.addActionListener(e -> updateSignUpButtonState());
        
        // Role selection info
        roleComboBox.addActionListener(e -> showRoleInfo());
    }
    
    private void validateUsername() {
        String username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            setValidationMessage(usernameValidationLabel, "", Color.BLACK);
            return;
        }
        
        if (username.length() < 3) {
            setValidationMessage(usernameValidationLabel, "Username must be at least 3 characters", Color.RED);
            return;
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            setValidationMessage(usernameValidationLabel, "Username can only contain letters, numbers, and underscores", Color.RED);
            return;
        }
        
        if (!userController.isUsernameAvailable(username)) {
            setValidationMessage(usernameValidationLabel, "Username is already taken", Color.RED);
            return;
        }
        
        setValidationMessage(usernameValidationLabel, "✓ Username is available", new Color(46, 204, 113));
    }
    
    private void validateEmail() {
        String email = emailField.getText().trim();
        
        if (email.isEmpty()) {
            setValidationMessage(emailValidationLabel, "", Color.BLACK);
            return;
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailRegex, email)) {
            setValidationMessage(emailValidationLabel, "Please enter a valid email address", Color.RED);
            return;
        }
        
        setValidationMessage(emailValidationLabel, "✓ Valid email format", new Color(46, 204, 113));
    }
    
    private void validatePassword() {
        String password = new String(passwordField.getPassword());
        
        if (password.isEmpty()) {
            setValidationMessage(passwordValidationLabel, "", Color.BLACK);
            return;
        }
        
        if (password.length() < 6) {
            setValidationMessage(passwordValidationLabel, "Password must be at least 6 characters", Color.RED);
            return;
        }
        
        // Check for at least one number and one letter
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        
        if (!hasLetter || !hasNumber) {
            setValidationMessage(passwordValidationLabel, "Password must contain at least one letter and one number", Color.RED);
            return;
        }
        
        setValidationMessage(passwordValidationLabel, "✓ Strong password", new Color(46, 204, 113));
    }
    
    private void validateConfirmPassword() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (confirmPassword.isEmpty()) {
            setValidationMessage(confirmPasswordValidationLabel, "", Color.BLACK);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            setValidationMessage(confirmPasswordValidationLabel, "Passwords do not match", Color.RED);
            return;
        }
        
        setValidationMessage(confirmPasswordValidationLabel, "✓ Passwords match", new Color(46, 204, 113));
    }
    
    private void setValidationMessage(JLabel label, String message, Color color) {
        label.setText(message);
        label.setForeground(color);
    }
    
    private void updateSignUpButtonState() {
        boolean allValid = isFormValid();
        signUpButton.setEnabled(allValid && termsCheckBox.isSelected());
    }
    
    private boolean isFormValid() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        return !username.isEmpty() && username.length() >= 3 && 
               userController.isUsernameAvailable(username) &&
               !email.isEmpty() && isValidEmail(email) &&
               !fullName.isEmpty() &&
               !password.isEmpty() && password.length() >= 6 &&
               password.equals(confirmPassword) &&
               password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*");
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    
    private void showRoleInfo() {
        User.UserRole selectedRole = (User.UserRole) roleComboBox.getSelectedItem();
        String roleDescription = "";
        
        if (selectedRole == User.UserRole.ADMIN) {
            roleDescription = "Administrator Role:\n" +
                            "• Full system access\n" +
                            "• Manage medicines and inventory\n" +
                            "• Manage suppliers and users\n" +
                            "• Process sales and view reports\n" +
                            "• Monitor alerts and analytics";
        } else if (selectedRole == User.UserRole.PHARMACIST) {
            roleDescription = "Pharmacist Role:\n" +
                            "• Search medicine database\n" +
                            "• Process customer sales\n" +
                            "• Generate customer receipts\n" +
                            "• View medicine information\n" +
                            "• Limited system access";
        }
        
        // Update status label temporarily to show role info
        statusLabel.setText("<html>" + roleDescription.replace("\n", "<br>") + "</html>");
        statusLabel.setForeground(new Color(52, 73, 94));
        
        // Clear the message after 5 seconds
        Timer timer = new Timer(5000, e -> {
            statusLabel.setText(" ");
            statusLabel.setForeground(Color.RED);
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void performSignUp() {
        // Final validation
        if (!isFormValid()) {
            statusLabel.setText("Please fix all validation errors before proceeding.");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        if (!termsCheckBox.isSelected()) {
            statusLabel.setText("Please accept the Terms and Conditions.");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Get form data
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        User.UserRole role = (User.UserRole) roleComboBox.getSelectedItem();
        
        // Create user account
        boolean success = userController.createUser(username, email, password, role);
        
        if (success) {
            // Show success message
            statusLabel.setText("Account created successfully! You can now login.");
            statusLabel.setForeground(new Color(46, 204, 113));
            
            // Show success dialog
            String successMessage = String.format(
                "Account Created Successfully!\n\n" +
                "Username: %s\n" +
                "Email: %s\n" +
                "Role: %s\n\n" +
                "You can now login with your credentials.",
                username, email, role.getDisplayName()
            );
            
            JOptionPane.showMessageDialog(this, successMessage, "Account Created", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            clearForm();
            
            // Go back to login after 2 seconds
            Timer timer = new Timer(2000, e -> backToLogin());
            timer.setRepeats(false);
            timer.start();
            
        } else {
            statusLabel.setText("Failed to create account. Username may already exist.");
            statusLabel.setForeground(Color.RED);
        }
    }
    
    private void clearForm() {
        usernameField.setText("");
        emailField.setText("");
        fullNameField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedItem(User.UserRole.PHARMACIST);
        termsCheckBox.setSelected(false);
        
        // Clear validation messages
        usernameValidationLabel.setText(" ");
        emailValidationLabel.setText(" ");
        passwordValidationLabel.setText(" ");
        confirmPasswordValidationLabel.setText(" ");
    }
    
    private void backToLogin() {
        parentFrame.setVisible(true);
        dispose();
    }
    
    private void setFrameProperties() {
        setTitle("Pharmacy Management System - Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set icon (you can add an icon file)
        try {
            // setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        // Update sign up button state initially
        updateSignUpButtonState();
    }
}
