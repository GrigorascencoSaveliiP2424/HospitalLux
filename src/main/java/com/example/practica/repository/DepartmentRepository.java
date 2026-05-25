package com.example.practica.repository;
import com.example.practica.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    public String getSelectQuery() {
        return """
        SELECT 
            department_name AS [Отделение],
            specialization AS [Специализация],
            kol_ward AS [Кол-во палат]
        FROM Department
        """;
    }

    public List<String> getAvailableDepartments() {
        List<String> departments = new ArrayList<>();

        String sql = """
        SELECT DISTINCT Department.department_name
        FROM Department
        INNER JOIN Ward ON Department.department_id = Ward.department_id
        LEFT JOIN Patient ON Ward.ward_id = Patient.ward_id
        GROUP BY Department.department_id, Department.department_name, Ward.ward_id, Ward.kol_seats
        HAVING Ward.kol_seats - COUNT(Patient.patient_id) > 0
        ORDER BY Department.department_name
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(rs.getString("department_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return departments;
    }

    public String getSpecializationByDepartment(String departmentName) {
        String sql = """
        SELECT specialization
        FROM Department
        WHERE department_name = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("specialization");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}