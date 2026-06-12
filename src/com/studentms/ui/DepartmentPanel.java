package com.studentms.ui;

import com.studentms.dao.DepartmentDAO;
import com.studentms.model.Department;
import com.studentms.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DepartmentPanel extends JPanel {
    private JTable table;
    private DepartmentDAO dao;
    private DefaultTableModel tableModel;

    public DepartmentPanel() {
        dao = new DepartmentDAO();
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

        JLabel title = new JLabel("Departments");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("All academic departments");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Department Name", "Department Head"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 28, 28, 28));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Department> depts = dao.getAllDepartments();
        for (Department d : depts) {
            tableModel.addRow(new Object[]{d.getDeptId(), d.getDeptName(), d.getDeptHead()});
        }
    }
}
