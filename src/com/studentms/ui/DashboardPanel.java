package com.studentms.ui;

import com.studentms.dao.StudentDAO;
import com.studentms.util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel totalValueLabel, activeValueLabel, gpaValueLabel, deptValueLabel;
    private JLabel graduatedLabel, suspendedLabel, inactiveLabel, maleLabel;
    private StudentDAO dao;

    public DashboardPanel() {
        dao = new StudentDAO();
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 8, 28));

        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Overview of student management system");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setBackground(Theme.BG_MAIN);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 28, 28, 28));

        // Top stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.add(createStatCard("Total Students", "0", Theme.PRIMARY, "\uD83D\uDCDA", "total"));
        statsRow.add(createStatCard("Active Students", "0", Theme.ACCENT, "\u2705", "active"));
        statsRow.add(createStatCard("Avg GPA", "0.00", Theme.WARNING, "\u2B50", "gpa"));
        statsRow.add(createStatCard("Departments", "0", new Color(156, 39, 176), "\uD83C\uDFE2", "dept"));
        content.add(statsRow);
        content.add(Box.createRigidArea(new Dimension(0, 16)));

        // Second stats row
        JPanel statsRow2 = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow2.setOpaque(false);
        statsRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        graduatedLabel = new JLabel("0");
        suspendedLabel = new JLabel("0");
        inactiveLabel = new JLabel("0");
        maleLabel = new JLabel("0");
        statsRow2.add(createStatCardDetail("Graduated", graduatedLabel, Theme.ACCENT));
        statsRow2.add(createStatCardDetail("Suspended", suspendedLabel, Theme.DANGER));
        statsRow2.add(createStatCardDetail("Inactive", inactiveLabel, Theme.WARNING));
        statsRow2.add(createStatCardDetail("Male Students", maleLabel, Theme.PRIMARY));
        content.add(statsRow2);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Info card
        JPanel infoCard = Theme.createCard();
        infoCard.setLayout(new BorderLayout());
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel infoTitle = new JLabel("  System Information");
        infoTitle.setFont(Theme.FONT_SUBTITLE);
        infoTitle.setForeground(Theme.TEXT_PRIMARY);
        infoTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        infoCard.add(infoTitle, BorderLayout.NORTH);

        JTextArea infoArea = new JTextArea(
            "\u2022  Database: SQLite (embedded, no server required)\n" +
            "\u2022  Driver: SQLite JDBC 3.42.0.0\n" +
            "\u2022  UI Framework: Java Swing with Custom Theme\n" +
            "\u2022  Architecture: MVC (Model-View-Controller)\n" +
            "\u2022  Features: CRUD Operations, Search, Statistics\n" +
            "\u2022  Authentication: Role-based (Admin/Staff)"
        );
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        infoArea.setForeground(Theme.TEXT_SECONDARY);
        infoArea.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        infoCard.add(infoArea, BorderLayout.CENTER);

        content.add(infoCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color color, String icon, String key) {
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(0, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(Theme.FONT_STAT);
        valueLabel.setForeground(color);

        switch (key) {
            case "total": totalValueLabel = valueLabel; break;
            case "active": activeValueLabel = valueLabel; break;
            case "gpa": gpaValueLabel = valueLabel; break;
            case "dept": deptValueLabel = valueLabel; break;
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 32));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconLabel, BorderLayout.EAST);

        return card;
    }

    private JPanel createStatCardDetail(String title, JLabel valueLabel, Color color) {
        JPanel card = Theme.createCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(0, 100));

        valueLabel.setFont(Theme.FONT_STAT);
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public void refreshData() {
        totalValueLabel.setText(String.valueOf(dao.getTotalStudents()));
        activeValueLabel.setText(String.valueOf(dao.getActiveStudents()));
        gpaValueLabel.setText(String.format("%.2f", dao.getAverageGpa()));
        deptValueLabel.setText(String.valueOf(dao.getDepartmentCount()));
        graduatedLabel.setText(String.valueOf(dao.getCountByStatus("Graduated")));
        suspendedLabel.setText(String.valueOf(dao.getCountByStatus("Suspended")));
        inactiveLabel.setText(String.valueOf(dao.getCountByStatus("Inactive")));
        maleLabel.setText(String.valueOf(getGenderCount("Male")));
    }

    private int getGenderCount(String gender) {
        java.util.List<com.studentms.model.Student> students = dao.getAllStudents();
        return (int) students.stream().filter(s -> gender.equals(s.getGender())).count();
    }
}
