package com.studentms.ui;

import com.studentms.model.User;
import com.studentms.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton selectedNavBtn;
    private JButton dashboardBtn, studentsBtn, addStudentBtn, departmentsBtn, settingsBtn;
    private JLabel navLabel;

    private DashboardPanel dashboardPanel;
    private StudentPanel studentPanel;
    private AddStudentPanel addStudentPanel;
    private DepartmentPanel departmentPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Student Management System");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        showPanel("dashboard");
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_MAIN);

        dashboardPanel = new DashboardPanel();
        studentPanel = new StudentPanel(this, currentUser);
        addStudentPanel = new AddStudentPanel(this);
        departmentPanel = new DepartmentPanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(studentPanel, "students");
        contentPanel.add(addStudentPanel, "addStudent");
        contentPanel.add(departmentPanel, "departments");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        logoPanel.setMaximumSize(new Dimension(240, 64));
        logoPanel.setPreferredSize(new Dimension(240, 64));
        logoPanel.setBackground(Theme.BG_DARK);
        JLabel logo = new JLabel("\uD83C\uDF93  SMS Portal");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        logo.setForeground(Color.WHITE);
        logoPanel.add(logo);
        sidebar.add(logoPanel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 16)));

        // User info
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setMaximumSize(new Dimension(240, 60));
        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel userName = new JLabel(currentUser.getFullName());
        userName.setFont(Theme.FONT_BUTTON);
        userName.setForeground(Color.WHITE);
        userName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userRole = new JLabel(currentUser.getRole());
        userRole.setFont(Theme.FONT_SMALL);
        userRole.setForeground(new Color(150, 160, 180));
        userRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        userPanel.add(userName);
        userPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        userPanel.add(userRole);
        sidebar.add(userPanel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Menu label
        JLabel menuLabel = new JLabel("  MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(new Color(120, 130, 150));
        menuLabel.setMaximumSize(new Dimension(240, 20));
        sidebar.add(menuLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        // Nav buttons
        dashboardBtn = createNavButton("\uD83D\uDCCA  Dashboard", "dashboard");
        studentsBtn = createNavButton("\uD83D\uDCDA  Students", "students");
        addStudentBtn = createNavButton("\u2795  Add Student", "addStudent");
        departmentsBtn = createNavButton("\uD83C\uDFE2  Departments", "departments");

        sidebar.add(dashboardBtn);
        sidebar.add(studentsBtn);
        if ("ADMIN".equals(currentUser.getRole())) {
            sidebar.add(addStudentBtn);
        }
        sidebar.add(departmentsBtn);

        sidebar.add(Box.createVerticalGlue());

        // Logout
        JButton logoutBtn = createNavButton("\uD83D\uDEAA  Logout", "logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });
        sidebar.add(logoutBtn);

        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        return sidebar;
    }

    private JButton createNavButton(String text, String action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_SIDEBAR);
        btn.setForeground(new Color(180, 190, 210));
        btn.setBackground(Theme.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(240, 44));
        btn.setPreferredSize(new Dimension(240, 44));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != selectedNavBtn) {
                    btn.setBackground(new Color(45, 55, 75));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != selectedNavBtn) {
                    btn.setBackground(Theme.BG_SIDEBAR);
                }
            }
        });

        if (!action.equals("logout")) {
            btn.addActionListener(e -> showPanel(action));
        }

        return btn;
    }

    public void showPanel(String name) {
        // Reset old selection
        if (selectedNavBtn != null) {
            selectedNavBtn.setBackground(Theme.BG_SIDEBAR);
            selectedNavBtn.setForeground(new Color(180, 190, 210));
        }

        cardLayout.show(contentPanel, name);

        // Set new selection
        switch (name) {
            case "dashboard": selectedNavBtn = dashboardBtn; break;
            case "students": selectedNavBtn = studentsBtn; break;
            case "addStudent": selectedNavBtn = addStudentBtn; break;
            case "departments": selectedNavBtn = departmentsBtn; break;
        }

        if (selectedNavBtn != null) {
            selectedNavBtn.setBackground(Theme.PRIMARY);
            selectedNavBtn.setForeground(Color.WHITE);
        }

        // Refresh data on panels
        switch (name) {
            case "dashboard": dashboardPanel.refreshData(); break;
            case "students": studentPanel.refreshData(); break;
            case "departments": departmentPanel.refreshData(); break;
        }
    }

    public void navigateToStudents() {
        showPanel("students");
    }

    public void navigateToAddStudent() {
        showPanel("addStudent");
    }
}
