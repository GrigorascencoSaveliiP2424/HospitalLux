package com.example.practica.repository;

import com.example.practica.db.DatabaseConnection;
import com.example.practica.model.Patient;
import com.example.practica.repository.interfaces.Repository;

import java.sql.*;
import java.util.*;

public class PatientRepository implements Repository<Patient> {

    public String getSelectQuery() {
        return """
        SELECT patient_id, patient_name, patient_number, patient_adress,
               patient_diagnosis, date_of_admission, number_of_ward,
               type_ward, department_name, specialization
        FROM patient_department
        """;
    }

    public Map<String, String> getPatientForEdit(int patientId) {
        Map<String, String> data = new HashMap<>();

        String sql = """
            SELECT patient_id, patient_name, patient_adress, patient_number,
                   patient_diagnosis, date_of_admission,
                   department_name, specialization, type_ward
            FROM patient_department
            WHERE patient_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId); // -> передаёт ID пациента

            ResultSet rs = stmt.executeQuery(); // -> получает данные пациента

            if (rs.next()) {
                data.put("patient_name", rs.getString("patient_name")); // -> сохраняет ФИО
                data.put("patient_adress", rs.getString("patient_adress")); // -> сохраняет адрес
                data.put("patient_number", rs.getString("patient_number")); // -> сохраняет телефон
                data.put("patient_diagnosis", rs.getString("patient_diagnosis")); // -> сохраняет диагноз
                data.put("date_of_admission", rs.getString("date_of_admission")); // -> сохраняет дату поступления
                data.put("department_name", rs.getString("department_name")); // -> сохраняет отделение
                data.put("specialization", rs.getString("specialization")); // -> сохраняет специализацию
                data.put("type_ward", rs.getString("type_ward")); // -> сохраняет тип палаты
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public void add(Patient patient) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection(); // -> подключается к базе данных
            conn.setAutoCommit(false); // -> начинает транзакцию

            int patientId = generatePatientId(conn); // -> создаёт новый ID пациента
            int wardId = getFreeWardId(
                    conn,
                    patient.getDepartmentName(),
                    patient.getWardType(),
                    null
            ); // -> ищет свободную палату

            if (wardId == -1) {
                throw new SQLException("В отделении " + patient.getDepartmentName()
                        + " закончились свободные места в палатах типа "
                        + patient.getWardType());
            }

            String sql = """
                INSERT INTO Patient
                (patient_id, patient_name, patient_adress, patient_number,
                 patient_diagnosis, date_of_admission, ward_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, patientId); // -> передаёт ID
                stmt.setString(2, patient.getPatientName()); // -> передаёт ФИО
                stmt.setString(3, patient.getPatientAdress()); // -> передаёт адрес
                stmt.setString(4, patient.getPatientNumber()); // -> передаёт телефон
                stmt.setString(5, patient.getPatientDiagnosis()); // -> передаёт диагноз
                stmt.setDate(6, patient.getDateOfAdmission()); // -> передаёт дату
                stmt.setInt(7, wardId); // -> передаёт ID свободной палаты
                stmt.executeUpdate(); // -> добавляет пациента
            }

            conn.commit(); // -> сохраняет изменения

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // -> отменяет изменения при ошибке
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // -> возвращает стандартный режим
                    conn.close(); // -> закрывает подключение
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Patient patient) {
        if (!patient.hasValidId()) {
            throw new RuntimeException("Некорректный ID пациента");
        }
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection(); // -> подключается к базе данных
            conn.setAutoCommit(false); // -> начинает транзакцию

            int wardId = getFreeWardId(
                    conn,
                    patient.getDepartmentName(),
                    patient.getWardType(),
                    patient.getId()
            ); // -> ищет свободную палату с учётом текущего пациента

            if (wardId == -1) {
                throw new SQLException("В отделении " + patient.getDepartmentName()
                        + " закончились свободные места в палатах типа "
                        + patient.getWardType());
            }

            String sql = """
                UPDATE Patient
                SET patient_name = ?,
                    patient_adress = ?,
                    patient_number = ?,
                    patient_diagnosis = ?,
                    date_of_admission = ?,
                    ward_id = ?
                WHERE patient_id = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, patient.getPatientName()); // -> обновляет ФИО
                stmt.setString(2, patient.getPatientAdress()); // -> обновляет адрес
                stmt.setString(3, patient.getPatientNumber()); // -> обновляет телефон
                stmt.setString(4, patient.getPatientDiagnosis()); // -> обновляет диагноз
                stmt.setDate(5, patient.getDateOfAdmission()); // -> обновляет дату поступления
                stmt.setInt(6, wardId); // -> обновляет палату
                stmt.setInt(7, patient.getId()); // -> указывает ID пациента
                stmt.executeUpdate(); // -> выполняет обновление
            }

            conn.commit(); // -> сохраняет изменения

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // -> отменяет изменения при ошибке
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException(e.getMessage());

        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // -> возвращает стандартный режим
                    conn.close(); // -> закрывает подключение
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Patient WHERE patient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id); // -> передаёт ID пациента
            statement.executeUpdate(); // -> удаляет пациента

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getFreeWardId(Connection conn, String departmentName, String wardType, Integer currentPatientId) throws SQLException {
        String sql = """
            SELECT TOP 1 Ward.ward_id
            FROM Ward
            INNER JOIN Department ON Ward.department_id = Department.department_id
            LEFT JOIN Patient ON Ward.ward_id = Patient.ward_id
            WHERE Department.department_name = ?
              AND Ward.type_ward = ?
            GROUP BY Ward.ward_id, Ward.kol_seats
            HAVING Ward.kol_seats - COUNT(
                CASE
                    WHEN ? IS NULL OR Patient.patient_id <> ?
                    THEN Patient.patient_id
                END
            ) > 0
            ORDER BY Ward.ward_id
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departmentName); // -> передаёт отделение
            stmt.setString(2, wardType); // -> передаёт тип палаты

            if (currentPatientId == null) {
                stmt.setNull(3, Types.INTEGER); // -> при добавлении пациента
                stmt.setNull(4, Types.INTEGER); // -> при добавлении пациента
            } else {
                stmt.setInt(3, currentPatientId); // -> при редактировании пациента
                stmt.setInt(4, currentPatientId); // -> при редактировании пациента
            }

            ResultSet rs = stmt.executeQuery(); // -> ищет свободную палату

            if (rs.next()) {
                return rs.getInt("ward_id"); // -> возвращает ID свободной палаты
            }
        }

        return -1; // -> свободная палата не найдена
    }

    private int generatePatientId(Connection conn) throws SQLException {
        String sql = "SELECT ISNULL(MAX(patient_id), 0) + 1 AS new_id FROM Patient";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("new_id"); // -> возвращает новый ID
            }
        }

        return 1; // -> если пациентов ещё нет
    }
}