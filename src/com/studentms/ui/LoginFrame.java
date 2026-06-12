package com.studentms.ui;

import com.studentms.dao.UserDAO;
import com.studentms.model.User;
import com.studentms.util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Student Management System - Login");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(900, 550));

        // Left panel - Branding
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMARY, 0, getHeight(), Theme.PRIMARY_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        JLabel iconLabel = new JLabel("\uD83C\uDF93");
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 72));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Student");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel2 = new JLabel("Management System");
        titleLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        titleLabel2.setForeground(new Color(200, 215, 255));
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='text-align:center;color:#c8d7ff;font-size:13px;'>"
            + "A comprehensive solution for<br>managing student records,<br>departments, and academic data.</div></html>");
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(titleLabel);
        leftPanel.add(titleLabel2);
        leftPanel.add(descLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right panel - Login Form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 30, 8, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Welcome Back");
        formTitle.setFont(Theme.FONT_TITLE);
        formTitle.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(formTitle, gbc);

        JLabel formSubtitle = new JLabel("Sign in to your account");
        formSubtitle.setFont(Theme.FONT_SMALL);
        formSubtitle.setForeground(Theme.TEXT_SECONDARY);
        gbc.gridy = 1;
        rightPanel.add(formSubtitle, gbc);

        gbc.gridy = 2;
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.FONT_BUTTON);
        userLabel.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridy = 3;
        rightPanel.add(userLabel, gbc);

        usernameField = Theme.createTextField("Enter username");
        usernameField.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 4;
        rightPanel.add(usernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.FONT_BUTTON);
        passLabel.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridy = 5;
        rightPanel.add(passLabel, gbc);

        passwordField = Theme.createPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 42));
        gbc.gridy = 6;
        rightPanel.add(passwordField, gbc);

        // Status
        statusLabel = new JLabel(" ");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.DANGER);
        gbc.gridy = 7;
        rightPanel.add(statusLabel, gbc);

        // Login Button
        loginButton = Theme.createPrimaryButton("Sign In");
        loginButton.setPreferredSize(new Dimension(300, 44));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        gbc.gridy = 8;
        rightPanel.add(loginButton, gbc);

        // Hint
        JLabel hintLabel = new JLabel("<html><center><font color='#999' size='2'>Default: admin / admin123</font></center></html>");
        gbc.gridy = 9;
        rightPanel.add(hintLabel, gbc);

        loginButton.addActionListener(e -> doLogin());
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });
        usernameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) passwordField.requestFocus();
            }
        });

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");
        statusLabel.setText(" ");

        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() {
                return new UserDAO().authenticate(username, password);
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            MainFrame mainFrame = new MainFrame(user);
                            mainFrame.setVisible(true);
                        });
                    } else {
                        statusLabel.setText("Invalid username or password.");
                        loginButton.setEnabled(true);
                        loginButton.setText("Sign In");
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Connection error. Check database.");
                    loginButton.setEnabled(true);
                    loginButton.setText("Sign In");
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        Theme.applyLaf();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
