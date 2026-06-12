package com.studentms.ui;

import com.studentms.dao.*;
import com.studentms.model.*;
import com.studentms.util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class StudentDialog extends JDialog {
    private Student student;
    private boolean isEdit;
    private StudentPanel parentPanel;
    private StudentDAO dao;
    private DepartmentDAO deptDAO;

    private JTextField rollField, firstNameField, lastNameField, emailField, phoneField, gpaField, addressField;
    private JComboBox<String> genderCombo, statusCombo;
    private JComboBox<Department> deptCombo;
    private JSpinner dobSpinner;

    public StudentDialog(Window owner, Student student, StudentPanel parentPanel) {
        super(owner, student == null ? "Add Student" : "Edit Student", ModalityType.APPLICATION_MODAL);
        this.student = student;
        this.isEdit = student != null;
        this.parentPanel = parentPanel;
        dao = new StudentDAO();
        deptDAO = new DepartmentDAO();
        setSize(550, 650);
        setLocationRelativeTo(owner);
        setResizable(false);
        initComponents();
        if (isEdit) populateFields();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 56));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        JLabel title = new JLabel(isEdit ? "Edit Student Information" : "Register New Student");
        title.setFont(Theme.FONT_SUBTITLE);
        title.setForeground(Color.WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Roll Number
        addField(form, gbc, row++, "Roll Number:", rollField = Theme.createTextField("e.g., CS2024010"));

        // First Name
        addField(form, gbc, row++, "First Name:", firstNameField = Theme.createTextField("First name"));

        // Last Name
        addField(form, gbc, row++, "Last Name:", lastNameField = Theme.createTextField("Last name"));

        // Email
        addField(form, gbc, row++, "Email:", emailField = Theme.createTextField("email@uni.edu"));

        // Phone
        addField(form, gbc, row++, "Phone:", phoneField = Theme.createTextField("555-0100"));

        // Date of Birth
        JLabel dobLabel = createLabel("Date of Birth:");
        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));
        dobSpinner.setPreferredSize(new Dimension(250, 38));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(dobLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        form.add(dobSpinner, gbc);
        row++;

        // Gender
        addCombo(form, gbc, row++, "Gender:", genderCombo = Theme.createComboBox(
            new String[]{"", "Male", "Female", "Other"}));

        // Department
        JLabel deptLabel = createLabel("Department:");
        List<Department> depts = deptDAO.getAllDepartments();
        Department[] deptArray = depts.toArray(new Department[0]);
        deptCombo = new JComboBox<>(deptArray);
        deptCombo.setFont(Theme.FONT_BODY);
        deptCombo.setPreferredSize(new Dimension(250, 38));
        deptCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object val, int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                if (val instanceof Department) setText(((Department) val).getDeptName());
                return this;
            }
        });
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(deptLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        form.add(deptCombo, gbc);
        row++;

        // GPA
        addField(form, gbc, row++, "GPA:", gpaField = Theme.createTextField("0.00"));

        // Status
        addCombo(form, gbc, row++, "Status:", statusCombo = Theme.createComboBox(
            new String[]{"Active", "Inactive", "Graduated", "Suspended"}));

        // Address
        JLabel addrLabel = createLabel("Address:");
        addressField = Theme.createTextField("Full address");
        addressField.setPreferredSize(new Dimension(250, 38));
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(addrLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        form.add(addressField, gbc);

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setBorder(null);
        main.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JButton cancelBtn = Theme.createOutlineButton("Cancel");
        JButton saveBtn = Theme.createPrimaryButton(isEdit ? "Update" : "Save");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveStudent());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(createLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void addCombo(JPanel panel, GridBagConstraints gbc, int row, String label, JComboBox combo) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(createLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(combo, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BUTTON);
        label.setForeground(Theme.TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(110, 30));
        return label;
    }

    private void populateFields() {
        rollField.setText(student.getRollNumber());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        if (student.getDateOfBirth() != null) dobSpinner.setValue(student.getDateOfBirth());
        genderCombo.setSelectedItem(student.getGender());
        gpaField.setText(String.valueOf(student.getGpa()));
        statusCombo.setSelectedItem(student.getStatus());
        addressField.setText(student.getAddress());

        for (int i = 0; i < deptCombo.getItemCount(); i++) {
            Department d = deptCombo.getItemAt(i);
            if (d.getDeptId() == student.getDeptId()) {
                deptCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveStudent() {
        if (rollField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number, First Name and Last Name are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = isEdit ? student : new Student();
        s.setRollNumber(rollField.getText().trim());
        s.setFirstName(firstNameField.getText().trim());
        s.setLastName(lastNameField.getText().trim());
        s.setEmail(emailField.getText().trim());
        s.setPhone(phoneField.getText().trim());
        s.setGender((String) genderCombo.getSelectedItem());
        s.setStatus((String) statusCombo.getSelectedItem());
        s.setAddress(addressField.getText().trim());

        try {
            s.setGpa(Double.parseDouble(gpaField.getText().trim()));
        } catch (NumberFormatException e) {
            s.setGpa(0.0);
        }

        Department dept = (Department) deptCombo.getSelectedItem();
        if (dept != null) s.setDeptId(dept.getDeptId());

        java.util.Date dob = (java.util.Date) dobSpinner.getValue();
        if (dob != null) s.setDateOfBirth(new java.sql.Date(dob.getTime()));

        boolean success;
        if (isEdit) {
            success = dao.updateStudent(s);
        } else {
            success = dao.addStudent(s);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, isEdit ? "Student updated successfully!" : "Student added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            parentPanel.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save student. Check for duplicate roll number or email.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
