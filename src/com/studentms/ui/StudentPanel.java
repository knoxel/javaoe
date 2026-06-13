package com.studentms.ui;

import com.studentms.dao.StudentDAO;
import com.studentms.model.Student;
import com.studentms.model.User;
import com.studentms.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private JTable table;
    private StudentTableModel tableModel;
    private JTextField searchField;
    private StudentDAO dao;
    private MainFrame mainFrame;
    private User currentUser;

    public StudentPanel(MainFrame mainFrame, User currentUser) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        dao = new StudentDAO();
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        initComponents();
        refreshData();
    }

    private void initComponents() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 8, 28));

        JLabel title = new JLabel("Student Management");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("View, search, and manage all student records");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // Search + Actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsPanel.setOpaque(false);

        searchField = Theme.createTextField("Search by name, roll no, email...");
        searchField.setPreferredSize(new Dimension(260, 38));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    refreshData();
                } else {
                    loadData(dao.searchStudents(query));
                }
            }
        });

        JButton refreshBtn = Theme.createOutlineButton("Refresh");
        JButton addBtn = Theme.createPrimaryButton("+ Add Student");

        addBtn.addActionListener(e -> mainFrame.navigateToAddStudent());
        refreshBtn.addActionListener(e -> refreshData());

        actionsPanel.add(searchField);
        actionsPanel.add(refreshBtn);
        if ("ADMIN".equals(currentUser.getRole())) {
            actionsPanel.add(addBtn);
        }
        header.add(actionsPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Table
        tableModel = new StudentTableModel();
        table = new JTable(tableModel);
        Theme.styleTable(table);

        // Action column
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());
        table.getColumnModel().getColumn(7).setPreferredWidth(180);
        table.getColumnModel().getColumn(7).setMaxWidth(200);
        table.getColumnModel().getColumn(7).setMinWidth(180);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setMaxWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 28, 28, 28));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        List<Student> students = dao.getAllStudents();
        loadData(students);
    }

    private void loadData(List<Student> students) {
        tableModel.setStudents(students);
    }

    private class StudentTableModel extends AbstractTableModel {
        private String[] columns = {"ID", "Roll No", "Name", "Email", "Phone", "Department", "GPA", "Actions"};
        private List<Student> students;

        public void setStudents(List<Student> students) {
            this.students = students;
            fireTableDataChanged();
        }

        public Student getStudentAt(int row) { return students.get(row); }
        public int getRowCount() { return students != null ? students.size() : 0; }
        public int getColumnCount() { return columns.length; }
        public String getColumnName(int col) { return columns[col]; }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 7;
        }

        public Object getValueAt(int row, int col) {
            Student s = students.get(row);
            switch (col) {
                case 0: return s.getStudentId();
                case 1: return s.getRollNumber();
                case 2: return s.getFullName();
                case 3: return s.getEmail();
                case 4: return s.getPhone();
                case 5: return s.getDepartmentName();
                case 6: return String.format("%.2f", s.getGpa());
                case 7: return "actions";
                default: return "";
            }
        }
    }

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton viewBtn = new JButton("View");
        private JButton editBtn = new JButton("Edit");
        private JButton deleteBtn = new JButton("Delete");
        private boolean isAdmin;

        public ButtonRenderer() {
            isAdmin = "ADMIN".equals(currentUser.getRole());
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
            setOpaque(true);

            styleBtn(viewBtn, Theme.PRIMARY);
            styleBtn(editBtn, Theme.WARNING);
            styleBtn(deleteBtn, Theme.DANGER);

            add(viewBtn);
            if (isAdmin) {
                add(editBtn);
                add(deleteBtn);
            }
        }

        private void styleBtn(JButton btn, Color bg) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setPreferredSize(new Dimension(52, 26));
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
            setBackground(sel ? Theme.TABLE_SELECTION : (row % 2 == 0 ? Theme.BG_CARD : Theme.TABLE_ROW_ALT));
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton viewBtn, editBtn, deleteBtn;
        private int currentRow;
        private boolean isAdmin;

        public ButtonEditor() {
            isAdmin = "ADMIN".equals(currentUser.getRole());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
            viewBtn = createBtn("View", Theme.PRIMARY);
            editBtn = createBtn("Edit", Theme.WARNING);
            deleteBtn = createBtn("Delete", Theme.DANGER);

            viewBtn.addActionListener(e -> {
                fireEditingStopped();
                viewStudent();
            });
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                editStudent();
            });
            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                deleteStudent();
            });

            panel.add(viewBtn);
            if (isAdmin) {
                panel.add(editBtn);
                panel.add(deleteBtn);
            }
        }

        private JButton createBtn(String text, Color bg) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setPreferredSize(new Dimension(52, 26));
            btn.setFocusable(false);
            return btn;
        }

        private void viewStudent() {
            Student s = tableModel.getStudentAt(currentRow);
            JOptionPane.showMessageDialog(panel,
                String.format("Student Details:\n\nRoll No: %s\nName: %s\nEmail: %s\nPhone: %s\nDept: %s\nGPA: %.2f\nStatus: %s",
                    s.getRollNumber(), s.getFullName(), s.getEmail(), s.getPhone(),
                    s.getDepartmentName(), s.getGpa(), s.getStatus()),
                "Student Details", JOptionPane.INFORMATION_MESSAGE);
        }

        private void editStudent() {
            Student s = tableModel.getStudentAt(currentRow);
            new StudentDialog(SwingUtilities.getWindowAncestor(panel), s, StudentPanel.this).setVisible(true);
        }

        private void deleteStudent() {
            Student s = tableModel.getStudentAt(currentRow);
            int confirm = JOptionPane.showConfirmDialog(panel,
                "Delete student: " + s.getFullName() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteStudent(s.getStudentId())) {
                    refreshData();
                    JOptionPane.showMessageDialog(panel, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object val, boolean sel, int row, int col) {
            currentRow = row;
            panel.setBackground(sel ? Theme.TABLE_SELECTION : (row % 2 == 0 ? Theme.BG_CARD : Theme.TABLE_ROW_ALT));
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return "actions"; }
        @Override
        public boolean stopCellEditing() { return super.stopCellEditing(); }
    }
}
