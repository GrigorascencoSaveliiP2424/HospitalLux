package com.example.practica.repository;
import com.example.practica.db.DatabaseConnection;
import com.example.practica.model.Ward;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WardRepository  {

    public boolean hasFreePlacesByType(String departmentName, String wardType) {
        String sql = """
        SELECT Ward.ward_id
        FROM Ward
        INNER JOIN Department
            ON Ward.department_id = Department.department_id
        LEFT JOIN Patient
            ON Ward.ward_id = Patient.ward_id
        WHERE Department.department_name = ?
          AND Ward.type_ward = ?
        GROUP BY Ward.ward_id, Ward.kol_seats
        HAVING Ward.kol_seats - COUNT(Patient.patient_id) > 0
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, departmentName);
            stmt.setString(2, wardType);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // -> true, если есть свободная палата такого типа

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getSelectQuery() {
        return """
        SELECT 
            number_of_ward AS [Номер палаты],
            type_ward AS [Тип палаты],
            kol_seats AS [Кол-во мест],
            department_name AS [Отделение]
        FROM Ward
        INNER JOIN Department ON Ward.department_id = Department.department_id
        """;
    }

    public List<Ward> getAllWards() {
        List<Ward> wards = new ArrayList<>(); // -> создаёт список палат

        String sql = """
        SELECT ward_id, number_of_ward, type_ward, kol_seats, department_id
        FROM Ward
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ward ward = new Ward(
                        rs.getInt("ward_id"),
                        rs.getInt("number_of_ward"),
                        rs.getString("type_ward"),
                        rs.getInt("kol_seats"),
                        rs.getInt("department_id")
                ); // -> создаёт объект палаты

                wards.add(ward); // -> добавляет палату в список
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wards; // -> возвращает список палат
    }
}