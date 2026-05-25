package com.example.practica.controller;

import com.example.practica.model.Patient;
import com.example.practica.model.enums.WardType;
import com.example.practica.repository.DepartmentRepository;
import com.example.practica.repository.PatientRepository;
import com.example.practica.repository.WardRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

public class PatientFormController {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField diagnosisField;
    @FXML private DatePicker datePicker;

    @FXML private ComboBox<String> departmentBox;
    @FXML private ComboBox<String> specializationBox;
    @FXML private ComboBox<String> wardTypeBox;

    private final PatientRepository patientRepository = new PatientRepository();
    private final DepartmentRepository departmentRepository = new DepartmentRepository();
    private final WardRepository wardRepository = new WardRepository();

    private boolean editMode = false;
    private int editingPatientId = -1;

    @FXML
    public void initialize() {
        loadDepartments();
        setupDatePicker();
        setupPhoneField();

        departmentBox.setOnAction(event -> {
            loadSpecialization();
            loadWardTypes();
        });
    }

    private void loadDepartments() {
        departmentBox.getItems().clear();
        departmentBox.getItems().addAll(departmentRepository.getAvailableDepartments());
    }

    private void loadSpecialization() {
        specializationBox.getItems().clear();

        if (departmentBox.getValue() == null) return;

        String specialization =
                departmentRepository.getSpecializationByDepartment(departmentBox.getValue());

        if (specialization != null) {
            specializationBox.getItems().add(specialization);
            specializationBox.setValue(specialization);
        }
    }

    private void loadWardTypes() {
        wardTypeBox.getItems().clear();

        if (departmentBox.getValue() == null) return;

        for (WardType type : WardType.values()) {
            if (wardRepository.hasFreePlacesByType(
                    departmentBox.getValue(),
                    type.getDisplayName()
            )) {
                wardTypeBox.getItems().add(type.getDisplayName());
            }
        }
    }

    public void setPatientForEdit(int patientId) {
        editMode = true;
        editingPatientId = patientId;

        Map<String, String> data = patientRepository.getPatientForEdit(patientId);

        if (data.isEmpty()) {
            showError("Пациент не найден");
            return;
        }

        nameField.setText(data.get("patient_name"));
        addressField.setText(data.get("patient_adress"));
        phoneField.setText(data.get("patient_number"));
        diagnosisField.setText(data.get("patient_diagnosis"));
        datePicker.setValue(Date.valueOf(data.get("date_of_admission")).toLocalDate());

        departmentBox.setValue(data.get("department_name"));
        loadSpecialization();
        loadWardTypes();

        specializationBox.setValue(data.get("specialization"));
        wardTypeBox.setValue(data.get("type_ward"));
    }

    @FXML
    private void handleSave() {
        try {
            if (nameField.getText().isEmpty() ||
                    addressField.getText().isEmpty() ||
                    phoneField.getText().isEmpty() ||
                    diagnosisField.getText().isEmpty() ||
                    datePicker.getValue() == null ||
                    departmentBox.getValue() == null ||
                    wardTypeBox.getValue() == null) {

                showError("Заполните все поля");
                return;
            }

            if (!validateFields()) return;

            Patient patient = new Patient(
                    editMode ? editingPatientId : 0,
                    nameField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    diagnosisField.getText(),
                    Date.valueOf(datePicker.getValue()),
                    0
            );

            patient.setDepartmentName(departmentBox.getValue()); // -> сохраняет выбранное отделение в объект пациента
            patient.setWardType(wardTypeBox.getValue()); // -> сохраняет выбранный тип палаты в объект пациента

            if (editMode) {
                patientRepository.update(patient); // -> вызывает переопределённый метод update из интерфейса Repository
            } else {
                patientRepository.add(patient); // -> вызывает переопределённый метод add из интерфейса Repository
            }

            ((Stage) nameField.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Проверьте правильность данных");
        }
    }

    private boolean validateFields() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String diagnosis = diagnosisField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.matches(".*\\d.*")) {
            showError("ФИО не должно содержать цифры");
            return false;
        }

        if (address.matches("^\\d+$")) {
            showError("Адрес не должен состоять только из цифр");
            return false;
        }

        if (diagnosis.matches(".*\\d.*")) {
            showError("Диагноз не должен содержать цифры");
            return false;
        }

        if (!phone.startsWith("+373")) {
            showError("Телефон должен начинаться с +373");
            return false;
        }

        String digits = phone.substring(4);

        if (!digits.matches("\\d{8}")) {
            showError("После +373 должно быть 8 цифр");
            return false;
        }

        if (datePicker.getValue().isBefore(LocalDate.now())) {
            showError("Дата поступления не может быть раньше сегодняшнего дня");
            return false;
        }

        return true;
    }

    private void setupDatePicker() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #999999;");
                }
            }
        });
    }

    private void setupPhoneField() {
        phoneField.setText("+373");

        phoneField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();

            if (!newText.startsWith("+373")) {
                return null;
            }

            String digits = newText.substring(4);

            if (!digits.matches("\\d*")) {
                return null;
            }

            if (digits.length() > 8) {
                return null;
            }

            return change;
        }));

        phoneField.positionCaret(phoneField.getText().length());

        phoneField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue && phoneField.getCaretPosition() < 4) {
                phoneField.positionCaret(phoneField.getText().length());
            }
        });

        phoneField.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            if (newPos.intValue() < 4) {
                phoneField.positionCaret(phoneField.getText().length());
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        alert.showAndWait();
    }

    private void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/practica/style.css").toExternalForm()
        );

        dialogPane.getStyleClass().add("custom-alert");
    }

    @FXML
    private void handleCancel() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}