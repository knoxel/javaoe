package com.studentms.ui;

import com.studentms.dao.*;
import com.studentms.model.*;
import com.studentms.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddStudentPanel extends JPanel {
    private MainFrame mainFrame;
    private StudentDAO dao;
    private DepartmentDAO deptDAO;
    private JTextField rollField, firstNameField, lastNameField, emailField, phoneField, gpaField, addressField;
    private JComboBox<String> genderCombo, statusCombo;
    private JComboBox<Department> deptCombo;
    private JSpinner dobSpinner;

    public AddStudentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        dao = new StudentDAO();
        deptDAO = new DepartmentDAO();
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 8, 28));

        JLabel title = new JLabel("Add New Student");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        JLabel subtitle = new JLabel("Fill in the form below to register a new student");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form card
        JPanel card = Theme.createCard();
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(12, 28, 28, 28),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addField(card, gbc, row++, "Roll Number *", rollField = createField("e.g., CS2024010"));
        addField(card, gbc, row++, "First Name *", firstNameField = createField("Enter first name"));
        addField(card, gbc, row++, "Last Name *", lastNameField = createField("Enter last name"));
        addField(card, gbc, row++, "Email", emailField = createField("student@uni.edu"));
        addField(card, gbc, row++, "Phone", phoneField = createField("555-0100"));

        // DOB
        JLabel dobLabel = createLabel("Date of Birth");
        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));
        dobSpinner.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        card.add(dobLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        card.add(dobSpinner, gbc);
        row++;

        // Gender
        JLabel genderLabel = createLabel("Gender");
        genderCombo = Theme.createComboBox(new String[]{"Select...", "Male", "Female", "Other"});
        genderCombo.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        card.add(genderLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        card.add(genderCombo, gbc);
        row++;

        // Department
        JLabel deptLabel = createLabel("Department");
        List<Department> depts = deptDAO.getAllDepartments();
        Department[] deptArray = depts.toArray(new Department[0]);
        deptCombo = new JComboBox<>(deptArray);
        deptCombo.setFont(Theme.FONT_BODY);
        deptCombo.setPreferredSize(new Dimension(300, 40));
        deptCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object val, int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                if (val instanceof Department) setText(((Department) val).getDeptName());
                return this;
            }
        });
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        card.add(deptLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        card.add(deptCombo, gbc);
        row++;

        addField(card, gbc, row++, "GPA", gpaField = createField("0.00"));

        // Status
        JLabel statusLabel = createLabel("Status");
        statusCombo = Theme.createComboBox(new String[]{"Active", "Inactive", "Graduated", "Suspended"});
        statusCombo.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        card.add(statusLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        card.add(statusCombo, gbc);
        row++;

        addField(card, gbc, row++, "Address", addressField = createField("Full address"));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setOpaque(false);
        JButton resetBtn = Theme.createOutlineButton("Reset");
        JButton saveBtn = Theme.createSuccessButton("Save Student");
        saveBtn.setPreferredSize(new Dimension(150, 42));

        resetBtn.addActionListener(e -> clearForm());
        saveBtn.addActionListener(e -> saveStudent());

        btnPanel.add(resetBtn);
        btnPanel.add(saveBtn);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(btnPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(createLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BUTTON);
        label.setForeground(Theme.TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(120, 30));
        return label;
    }

    private JTextField createField(String placeholder) {
        JTextField field = Theme.createTextField(placeholder);
        field.setPreferredSize(new Dimension(300, 40));
        return field;
    }

    private void clearForm() {
        rollField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        gpaField.setText("");
        addressField.setText("");
        genderCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        if (deptCombo.getItemCount() > 0) deptCombo.setSelectedIndex(0);
    }

    private void saveStudent() {
        if (rollField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number, First Name and Last Name are required.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gender = (String) genderCombo.getSelectedItem();
        if ("Select...".equals(gender)) gender = null;

        Student s = new Student();
        s.setRollNumber(rollField.getText().trim());
        s.setFirstName(firstNameField.getText().trim());
        s.setLastName(lastNameField.getText().trim());
        s.setEmail(emailField.getText().trim());
        s.setPhone(phoneField.getText().trim());
        s.setGender(gender);
        s.setStatus((String) statusCombo.getSelectedItem());
        s.setAddress(addressField.getText().trim());

        try { s.setGpa(Double.parseDouble(gpaField.getText().trim())); }
        catch (NumberFormatException e) { s.setGpa(0.0); }

        Department dept = (Department) deptCombo.getSelectedItem();
        if (dept != null) s.setDeptId(dept.getDeptId());

        java.util.Date dob = (java.util.Date) dobSpinner.getValue();
        if (dob != null) s.setDateOfBirth(new java.sql.Date(dob.getTime()));

        if (dao.addStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            mainFrame.navigateToStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Check for duplicate roll number or email.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
