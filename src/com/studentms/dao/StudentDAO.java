package com.studentms.dao;

import com.studentms.model.*;
import com.studentms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name FROM students s LEFT JOIN departments d ON s.dept_id = d.dept_id ORDER BY s.student_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, d.dept_name FROM students s LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE s.first_name LIKE ? OR s.last_name LIKE ? OR s.roll_number LIKE ? OR s.email LIKE ? "
                   + "ORDER BY s.student_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT s.*, d.dept_name FROM students s LEFT JOIN departments d ON s.dept_id = d.dept_id WHERE s.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (roll_number, first_name, last_name, email, phone, date_of_birth, gender, address, dept_id, gpa, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getRollNumber());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setDate(6, s.getDateOfBirth());
            ps.setString(7, s.getGender());
            ps.setString(8, s.getAddress());
            ps.setInt(9, s.getDeptId());
            ps.setDouble(10, s.getGpa());
            ps.setString(11, s.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET roll_number=?, first_name=?, last_name=?, email=?, phone=?, date_of_birth=?, "
                   + "gender=?, address=?, dept_id=?, gpa=?, status=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getRollNumber());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setDate(6, s.getDateOfBirth());
            ps.setString(7, s.getGender());
            ps.setString(8, s.getAddress());
            ps.setInt(9, s.getDeptId());
            ps.setDouble(10, s.getGpa());
            ps.setString(11, s.getStatus());
            ps.setInt(12, s.getStudentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalStudents() {
        return getCount("SELECT COUNT(*) FROM students");
    }

    public int getActiveStudents() {
        return getCount("SELECT COUNT(*) FROM students WHERE status = 'Active'");
    }

    public double getAverageGpa() {
        String sql = "SELECT AVG(gpa) FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getDepartmentCount() {
        return getCount("SELECT COUNT(*) FROM departments");
    }

    public int getCountByStatus(String status) {
        return getCount("SELECT COUNT(*) FROM students WHERE status = '" + status + "'");
    }

    private int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setDateOfBirth(parseDate(rs.getString("date_of_birth")));
        s.setGender(rs.getString("gender"));
        s.setAddress(rs.getString("address"));
        s.setDeptId(rs.getInt("dept_id"));
        try { s.setDepartmentName(rs.getString("dept_name")); } catch (SQLException ignored) {}
        s.setEnrollmentDate(parseDate(rs.getString("enrollment_date")));
        s.setGpa(rs.getDouble("gpa"));
        s.setStatus(rs.getString("status"));
        try { s.setPhotoPath(rs.getString("photo_path")); } catch (SQLException ignored) {}
        return s;
    }

    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return java.sql.Date.valueOf(dateStr.substring(0, 10));
        } catch (Exception e) {
            return null;
        }
    }
}
