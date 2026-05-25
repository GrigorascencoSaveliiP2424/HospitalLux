package com.example.practica.controller;
import com.example.practica.repository.PatientRepository;
import com.example.practica.repository.WardRepository;
import com.example.practica.repository.DepartmentRepository;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.control.ProgressBar;
import com.example.practica.db.DatabaseConnection;
import com.example.practica.service.ExportService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import java.sql.*;

public class MainController {

    private final ObservableList<TableView<ObservableList<String>>> reportTables =
            FXCollections.observableArrayList();

    private VBox reportExportBox;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    private final ExportService exportService = new ExportService();
    private String currentQuery = "";
    private final PatientRepository patientRepository = new PatientRepository();
    private final WardRepository wardRepository = new WardRepository();
    private final DepartmentRepository departmentRepository = new DepartmentRepository();
    @FXML private ScrollPane reportScrollPane;
    @FXML private VBox reportBox;
    @FXML private Button reportBtn;
    @FXML private Button exportBtn;
    @FXML private Label pageTitle;
    @FXML private TextField searchField;
    @FXML private TableView<ObservableList<String>> mainTable;
    @FXML private Button statsBtn;
    @FXML private Button patientsBtn;
    @FXML private Button wardsBtn;
    @FXML private Button departmentsBtn;
    @FXML private Button homeBtn;
    @FXML private VBox homeContent;
    @FXML private VBox dataContent;
    @FXML private HBox brandStrip;

    @FXML
    public void initialize() {
        showHome();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
    }

    @FXML
    private void showHome() {
        pageTitle.setText("Главная");
        currentQuery = "";

        setActiveButton(homeBtn);
        setPatientControlsVisible(false);

        homeContent.setVisible(true);
        homeContent.setManaged(true);

        dataContent.setVisible(false);
        dataContent.setManaged(false);

        mainTable.setVisible(false);
        mainTable.setManaged(false);
        reportScrollPane.setVisible(false);
        reportScrollPane.setManaged(false);

        playPageAnimation(homeContent);
    }

    @FXML
    private void showPatients() {
        exportBtn.setVisible(true);
        exportBtn.setManaged(true);

        showDataContent();
        setPatientControlsVisible(true);

        pageTitle.setText("Пациенты");
        setActiveButton(patientsBtn);
        showTable();

        currentQuery = patientRepository.getSelectQuery();
        loadTable(currentQuery);
    }

    @FXML
    private void showWards() {
        exportBtn.setVisible(true);
        exportBtn.setManaged(true);

        showDataContent();
        setPatientControlsVisible(false);

        pageTitle.setText("Палаты");
        setActiveButton(wardsBtn);
        showTable();

        currentQuery = wardRepository.getSelectQuery();
        loadTable(currentQuery);
    }

    @FXML
    private void showDepartments() {
        exportBtn.setVisible(true);
        exportBtn.setManaged(true);

        showDataContent();
        setPatientControlsVisible(false);

        pageTitle.setText("Отделения");
        setActiveButton(departmentsBtn);
        showTable();

        currentQuery = departmentRepository.getSelectQuery();
        loadTable(currentQuery);
    }

    @FXML
    private void showStats() {
        showDataContent();
        reportBox.getChildren().clear();
        setPatientControlsVisible(false);
        exportBtn.setVisible(false);
        exportBtn.setManaged(false);
        pageTitle.setText("Статистика");
        setActiveButton(statsBtn);
        showReportView();



        addSection("ПАЛАТЫ ПО ОТДЕЛЕНИЯМ");
        addDepartmentBars();

        addSection("ГРАФИК ТИПОВ ПАЛАТ");
        addWardTypePieChart();

        HBox mainReportRow = new HBox(45);
        mainReportRow.setFillHeight(true);

        VBox cardsBox = new VBox(25);
        cardsBox.setPrefWidth(700);
        cardsBox.setMinWidth(700);

        addSectionToBox(cardsBox, "ПАЦИЕНТЫ");

        HBox patientRow = new HBox(20);
        patientRow.getChildren().addAll(
                createReportCard("Всего пациентов",
                        getSingleValue("SELECT COUNT(*) FROM Patient"),
                        null),

                createReportCard("Из Chisinau",
                        getSingleValue("SELECT COUNT(*) FROM Patient WHERE patient_adress = 'Chisinau'"),
                        "report-card-green"),

                createReportCard("Самый частый диагноз",
                        getSingleValue("""
                        SELECT TOP 1 patient_diagnosis
                        FROM Patient
                        GROUP BY patient_diagnosis
                        ORDER BY COUNT(*) DESC
                        """),
                        "report-card-purple")
        );
        cardsBox.getChildren().add(patientRow);

        addSectionToBox(cardsBox, "ПАЛАТЫ");

        HBox wardRow = new HBox(20);
        wardRow.getChildren().addAll(
                createReportCard("Всего палат",
                        getSingleValue("SELECT COUNT(*) FROM Ward"),
                        null),

                createReportCard("Всего мест",
                        getSingleValue("SELECT SUM(kol_seats) FROM Ward"),
                        "report-card-green"),

                createReportCard("Свободных мест",
                        getSingleValue("""
                        SELECT SUM(free_places)
                        FROM (
                            SELECT Ward.kol_seats - COUNT(Patient.patient_id) AS free_places
                            FROM Ward
                            LEFT JOIN Patient ON Ward.ward_id = Patient.ward_id
                            GROUP BY Ward.ward_id, Ward.kol_seats
                        ) AS temp
                        """),
                        "report-card-orange")
        );
        cardsBox.getChildren().add(wardRow);

        addSectionToBox(cardsBox, "ОТДЕЛЕНИЯ");

        HBox departmentRow = new HBox(20);

        departmentRow.getChildren().addAll(
                createReportCard("Всего отделений",
                        getSingleValue("SELECT COUNT(*) FROM Department"),
                        null),

                createReportCard("Всего палат в отделениях",
                        getSingleValue("""
                SELECT COUNT(Ward.ward_id)
                FROM Department
                INNER JOIN Ward ON Department.department_id = Ward.department_id
                """),
                        "report-card-green"),

                createReportCard("Кол-во специализаций",
                        getSingleValue("SELECT COUNT(DISTINCT specialization) FROM Department"),
                        "report-card-green")
        );

        cardsBox.getChildren().add(departmentRow);

        VBox diagnosisChart = createDiagnosisBarChart();

        mainReportRow.getChildren().addAll(cardsBox, diagnosisChart);

        reportBox.getChildren().add(mainReportRow);
        playPageAnimation(dataContent);
        playPageAnimation(reportScrollPane);
    }

    @FXML
    private void showReport() {
        showDataContent();
        reportBox.getChildren().clear();
        reportTables.clear();

        setPatientControlsVisible(false);
        exportBtn.setVisible(false);
        exportBtn.setManaged(false);

        pageTitle.setText("Отчёт");
        setActiveButton(reportBtn);
        showReportView();

        addSection("ОТЧЁТ ПО ПОСТУПЛЕНИЯМ");

        HBox buttonsBox = new HBox(18);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        Button dayBtn = new Button("За день");
        Button weekBtn = new Button("За неделю");
        Button monthBtn = new Button("За месяц");
        Button exportReportBtn = new Button("Экспорт отчёта CSV");

        dayBtn.getStyleClass().add("report-filter-button");
        weekBtn.getStyleClass().add("report-filter-button");
        monthBtn.getStyleClass().add("report-filter-button");
        exportReportBtn.getStyleClass().add("outline-button");

        exportReportBtn.setOnAction(e -> handleExportReport());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonsBox.getChildren().addAll(dayBtn, weekBtn, monthBtn, spacer, exportReportBtn);
        reportBox.getChildren().add(buttonsBox);

        VBox resultBox = new VBox(20);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        reportBox.getChildren().add(resultBox);

        VBox departmentReportBox = new VBox(20);
        reportBox.getChildren().add(departmentReportBox);

        VBox criticalWardBox = new VBox(20);
        reportBox.getChildren().add(criticalWardBox);

        dayBtn.setOnAction(e -> {
            reportTables.clear();
            setActiveReportFilterButton(dayBtn, weekBtn, monthBtn);

            showAdmissionPatients(resultBox, "За день", """
            SELECT COUNT(*)
            FROM patient_department
            WHERE CAST(date_of_admission AS DATE) = CAST(GETDATE() AS DATE)
            """, """
            SELECT patient_id AS [Номер пациента], patient_name AS [ФИО], patient_number AS [Телефон], patient_adress AS [Адресс],
                   patient_diagnosis AS [Диагноз], date_of_admission AS [Дата поступления],
                   number_of_ward AS [Номер палаты], type_ward AS [Тип палаты], department_name AS [Отделение], specialization AS [Специализация]
            FROM patient_department
            WHERE CAST(date_of_admission AS DATE) = CAST(GETDATE() AS DATE)
            """);

            showDepartmentOccupancyTable(departmentReportBox);
            showCriticalWardsTable(criticalWardBox);
        });

        weekBtn.setOnAction(e -> {
            reportTables.clear();
            setActiveReportFilterButton(weekBtn, dayBtn, monthBtn);

            showAdmissionPatients(resultBox, "За неделю", """
            SELECT COUNT(*)
            FROM patient_department
            WHERE date_of_admission >= DATEADD(DAY, -7, CAST(GETDATE() AS DATE))
            """, """
            SELECT patient_id AS [Номер пациента], patient_name AS [ФИО], patient_number AS [Телефон], patient_adress AS [Адресс],
                   patient_diagnosis AS [Диагноз], date_of_admission AS [Дата поступления],
                   number_of_ward AS [Номер палаты], type_ward AS [Тип палаты], department_name AS [Отделение], specialization AS [Специализация]
            FROM patient_department
            WHERE date_of_admission >= DATEADD(DAY, -7, CAST(GETDATE() AS DATE))
            """);

            showDepartmentOccupancyTable(departmentReportBox);
            showCriticalWardsTable(criticalWardBox);
        });

        monthBtn.setOnAction(e -> {
            reportTables.clear();
            setActiveReportFilterButton(monthBtn, dayBtn, weekBtn);

            showAdmissionPatients(resultBox, "За месяц", """
            SELECT COUNT(*)
            FROM patient_department
            WHERE date_of_admission >= DATEADD(MONTH, -1, CAST(GETDATE() AS DATE))
            """, """
            SELECT patient_id AS [Номер пациента], patient_name AS [ФИО], patient_number AS [Телефон], patient_adress AS [Адресс],
                   patient_diagnosis AS [Диагноз], date_of_admission AS [Дата поступления],
                   number_of_ward AS [Номер палаты], type_ward AS [Тип палаты], department_name AS [Отделение], specialization AS [Специализация]
            FROM patient_department
            WHERE date_of_admission >= DATEADD(MONTH, -1, CAST(GETDATE() AS DATE))
            """);

            showDepartmentOccupancyTable(departmentReportBox);
            showCriticalWardsTable(criticalWardBox);
        });

        playPageAnimation(dataContent);
        playPageAnimation(reportScrollPane);
    }

    @FXML
    private void handleExport() {

        boolean saved = exportService.exportToCsv(mainTable);

        if (!saved) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Экспорт");
        alert.setHeaderText(null);
        alert.setContentText("Файл успешно сохранён");

        alert.showAndWait();
    }
    private void loadTable(String sql) {
        mainTable.getColumns().clear();
        mainTable.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i - 1;
                String columnName = metaData.getColumnName(i);

                columnName = switch (columnName) {
                    case "patient_id" -> "Номер пациента";
                    case "patient_name" -> "ФИО";
                    case "patient_number" -> "Телефон";
                    case "patient_adress" -> "Адрес";
                    case "patient_diagnosis" -> "Диагноз";
                    case "date_of_admission" -> "Дата поступления";

                    case "number_of_ward" -> "Номер палаты";
                    case "type_ward" -> "Тип палаты";
                    case "kol_seats" -> "Кол-во мест";

                    case "department_name" -> "Отделение";
                    case "specialization" -> "Специализация";
                    case "kol_ward" -> "Кол-во палат";

                    default -> columnName;
                };

                TableColumn<ObservableList<String>, String> column =
                        new TableColumn<>(columnName);

                if (pageTitle.getText().equals("Пациенты")) {
                    column.setPrefWidth(220);
                    column.setMinWidth(180);
                } else {
                    column.setMinWidth(100);
                }

                column.setCellValueFactory(data ->
                        new SimpleStringProperty(data.getValue().get(columnIndex))
                );

                mainTable.getColumns().add(column);
            }

            ObservableList<ObservableList<String>> data =
                    FXCollections.observableArrayList();

            while (rs.next()) {
                ObservableList<String> row =
                        FXCollections.observableArrayList();

                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }

                data.add(row);
            }

            mainTable.setItems(data);
            autoResizeColumns(mainTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showDataContent() {
        homeContent.setVisible(false);
        homeContent.setManaged(false);

        dataContent.setVisible(true);
        dataContent.setManaged(true);

        playPageAnimation(dataContent);
    }

    private void showTable() {
        mainTable.setVisible(true);
        mainTable.setManaged(true);

        reportScrollPane.setVisible(false);
        reportScrollPane.setManaged(false);
        playPageAnimation(mainTable);
    }

    private void showReportView() {
        mainTable.setVisible(false);
        mainTable.setManaged(false);

        reportScrollPane.setVisible(true);
        reportScrollPane.setManaged(true);
        playPageAnimation(reportScrollPane);
    }

    private void setActiveButton(Button activeButton) {
        statsBtn.getStyleClass().setAll("menu-button");
        patientsBtn.getStyleClass().remove("menu-button-active");
        wardsBtn.getStyleClass().remove("menu-button-active");
        departmentsBtn.getStyleClass().remove("menu-button-active");
        reportBtn.getStyleClass().remove("menu-button-active");
        homeBtn.getStyleClass().remove("menu-button-active");

        if (!patientsBtn.getStyleClass().contains("menu-button")) {
            patientsBtn.getStyleClass().add("menu-button");
        }

        if (!wardsBtn.getStyleClass().contains("menu-button")) {
            wardsBtn.getStyleClass().add("menu-button");
        }

        if (!departmentsBtn.getStyleClass().contains("menu-button")) {
            departmentsBtn.getStyleClass().add("menu-button");
        }

        if (!reportBtn.getStyleClass().contains("menu-button")) {
            reportBtn.getStyleClass().add("menu-button");
        }

        if (!homeBtn.getStyleClass().contains("menu-button")) {
            homeBtn.getStyleClass().add("menu-button");
        }

        activeButton.getStyleClass().add("menu-button-active");
    }
    private String getSingleValue(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0";
    }
    private void addSection(String title) {
        Label label = new Label(title);
        label.getStyleClass().add("report-section-title");
        reportBox.getChildren().add(label);
    }
    private VBox createReportCard(String title, String value, String colorClass) {
        VBox card = new VBox(12);
        card.getStyleClass().add("report-card");

        card.setPrefWidth(200);
        card.setMinWidth(200);
        card.setPrefHeight(145);
        card.setMinHeight(145);



        if (colorClass != null) {
            card.getStyleClass().add(colorClass);
        }

        Label titleLabel = new Label(title);
        titleLabel.setWrapText(true);
        titleLabel.getStyleClass().add("report-card-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(190);


        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("report-card-value");
        valueLabel.setWrapText(true);
        valueLabel.setMaxWidth(190);

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    private void addDepartmentBars() {
        VBox box = new VBox(12);
        box.getStyleClass().add("bar-card");

        String sql = """
            SELECT department_name, kol_ward
            FROM Department
            ORDER BY kol_ward DESC
            """;

        int max = Integer.parseInt(getSingleValue("SELECT MAX(kol_ward) FROM Department"));

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("department_name");
                int count = rs.getInt("kol_ward");

                HBox row = new HBox(15);
                row.getStyleClass().add("bar-row");

                Label nameLabel = new Label(name);
                nameLabel.getStyleClass().add("bar-name");
                nameLabel.setPrefWidth(230);

                ProgressBar bar = new ProgressBar((double) count / max);
                bar.getStyleClass().add("report-progress");
                bar.setPrefWidth(540);

                Label countLabel = new Label(String.valueOf(count));
                countLabel.getStyleClass().add("bar-value");

                row.getChildren().addAll(nameLabel, bar, countLabel);
                box.getChildren().add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        reportBox.getChildren().add(box);
    }

    private void filterTable(String keyword) {

        if (currentQuery.isEmpty()) {
            return;
        }

        String sql;

        if (pageTitle.getText().equals("Пациенты")) {

            sql = """
                SELECT patient_id, patient_name, patient_number,
                       patient_adress, patient_diagnosis,
                       date_of_admission, number_of_ward,
                       type_ward, department_name, specialization
                FROM patient_department
                WHERE patient_name LIKE ?
                   OR patient_adress LIKE ?
                   OR patient_diagnosis LIKE ?
                """;

        } else if (pageTitle.getText().equals("Палаты")) {

            sql = """
                SELECT number_of_ward, type_ward, kol_seats,
                       department_name
                FROM Ward
                INNER JOIN Department
                ON Ward.department_id = Department.department_id
                WHERE type_ward LIKE ?
                   OR department_name LIKE ?
                """;

        } else {

            sql = """
                SELECT department_name, specialization, kol_ward
                FROM Department
                WHERE department_name LIKE ?
                   OR specialization LIKE ?
                """;
        }

        mainTable.getColumns().clear();
        mainTable.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            stmt.setString(1, value);
            stmt.setString(2, value);

            if (pageTitle.getText().equals("Пациенты")) {
                stmt.setString(3, value);
            }

            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {

                final int columnIndex = i - 1;

                String columnName = metaData.getColumnName(i);

                columnName = switch (columnName) {
                    case "patient_id" -> "ID пациента";
                    case "patient_name" -> "Пациент";
                    case "patient_number" -> "Телефон";
                    case "patient_adress" -> "Адрес";
                    case "patient_diagnosis" -> "Диагноз";
                    case "date_of_admission" -> "Дата поступления";
                    case "number_of_ward" -> "Номер палаты";
                    case "type_ward" -> "Тип палаты";
                    case "kol_seats" -> "Кол-во мест";
                    case "department_name" -> "Отделение";
                    case "specialization" -> "Специализация";
                    case "kol_ward" -> "Кол-во палат";
                    default -> columnName;
                };

                TableColumn<ObservableList<String>, String> column =
                        new TableColumn<>(columnName);

                column.setPrefWidth(220);
                column.setMinWidth(180);

                column.setCellValueFactory(data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().get(columnIndex)
                        )
                );

                mainTable.getColumns().add(column);
            }

            ObservableList<ObservableList<String>> data =
                    FXCollections.observableArrayList();

            while (rs.next()) {

                ObservableList<String> row =
                        FXCollections.observableArrayList();

                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }

                data.add(row);
            }

            mainTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        if (!pageTitle.getText().equals("Пациенты")) {
            return;
        }

        openPatientForm(null);
    }

    @FXML
    private void handleUpdate() {
        if (!pageTitle.getText().equals("Пациенты")) {
            return;
        }

        ObservableList<String> selected = mainTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Выберите пациента для редактирования");
            return;
        }

        int patientId = Integer.parseInt(selected.get(0));

        openPatientForm(patientId);
    }

    @FXML
    private void handleDelete() {
        if (!pageTitle.getText().equals("Пациенты")) {
            return;
        }

        ObservableList<String> selected = mainTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Выберите пациента для удаления");
            return;
        }

        int patientId = Integer.parseInt(selected.get(0));
        String patientName = selected.get(1);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        styleAlert(confirm);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText(null);
        confirm.setContentText("Удалить пациента: " + patientName + "?");

        Stage alertStage = (Stage) confirm.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/practica/icons/delete.png")
        ));

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                patientRepository.delete(patientId);
                showPatients();
            }
        });
    }

    private void openPatientForm(Integer patientId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/practica/patient-form.fxml")
            );

            Stage stage = new Stage();
            stage.setTitle(patientId == null ? "Добавить пациента" : "Редактировать пациента");

            String iconPath = patientId == null
                    ? "/com/example/practica/icons/add.png"
                    : "/com/example/practica/icons/edit.png";

            stage.getIcons().add(new Image(
                    getClass().getResourceAsStream(iconPath)
            ));

            stage.setScene(new Scene(loader.load(), 600, 650));

            PatientFormController controller = loader.getController();

            if (patientId != null) {
                controller.setPatientForEdit(patientId);
            }

            stage.showAndWait();
            showPatients();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        styleAlert(alert);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setPatientControlsVisible(boolean visible) {
        searchField.setVisible(visible);
        searchField.setManaged(visible);

        addBtn.setVisible(visible);
        addBtn.setManaged(visible);

        editBtn.setVisible(visible);
        editBtn.setManaged(visible);

        deleteBtn.setVisible(visible);
        deleteBtn.setManaged(visible);
    }

    private void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.getStylesheets().add(
                getClass().getResource("/com/example/practica/style.css").toExternalForm()
        );

        dialogPane.getStyleClass().add("custom-alert");
    }

    private void addWardTypePieChart() {

        ObservableList<PieChart.Data> chartData =
                FXCollections.observableArrayList();

        String sql = """
        SELECT type_ward, COUNT(*) AS total
        FROM Ward
        GROUP BY type_ward
        ORDER BY total DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String type = rs.getString("type_ward");
                int total = rs.getInt("total");

                chartData.add(
                        new PieChart.Data(
                                type + " - " + total,
                                total
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        PieChart pieChart = new PieChart(chartData);

        pieChart.setTitle("Количество палат по типам");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(false);

        pieChart.setPrefHeight(440);
        pieChart.setMinHeight(440);
        pieChart.setPrefWidth(900);

        pieChart.getStyleClass().add("pie-chart");

        VBox chartBox = new VBox(pieChart);
        chartBox.setAlignment(Pos.CENTER);
        chartBox.getStyleClass().add("pie-chart-card");

        reportBox.getChildren().add(chartBox);
    }

    private void playPageAnimation(Node node) {

        node.setOpacity(0);
        node.setTranslateY(35);
        node.setScaleX(0.96);
        node.setScaleY(0.96);

        FadeTransition fade = new FadeTransition(Duration.millis(550), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(550), node);
        slide.setFromY(35);
        slide.setToY(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(550), node);
        scale.setFromX(0.96);
        scale.setFromY(0.96);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition animation =
                new ParallelTransition(fade, slide, scale);

        animation.play();
    }

    private void addSectionToBox(VBox box, String title) {
        Label label = new Label(title);
        label.getStyleClass().add("report-section-title");
        box.getChildren().add(label);
    }

    private VBox createDiagnosisBarChart() {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(20);
        yAxis.setTickUnit(2);
        yAxis.setLabel("Количество пациентов");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        chart.setTitle("Диагнозы");
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        chart.setCategoryGap(16);
        chart.setBarGap(3);

        chart.setPrefWidth(920);
        chart.setMinWidth(920);
        chart.setPrefHeight(650);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String sql = """
        SELECT patient_diagnosis,
               COUNT(*) AS total
        FROM Patient
        GROUP BY patient_diagnosis
        ORDER BY total DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                series.getData().add(
                        new XYChart.Data<>(
                                rs.getString("patient_diagnosis"),
                                rs.getInt("total")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        chart.getData().add(series);

        VBox box = new VBox(chart);
        box.getStyleClass().add("chart-card");

        box.setPrefWidth(980);
        box.setMinWidth(980);
        box.setPrefHeight(720);

        return box;
    }

    private void setActiveReportFilterButton(Button activeButton, Button... otherButtons) {
        activeButton.getStyleClass().setAll("report-filter-button", "report-filter-button-active");

        for (Button button : otherButtons) {
            button.getStyleClass().setAll("report-filter-button");
        }
    }

    private void showAdmissionPatients(VBox resultBox, String title, String countSql, String patientsSql) {
        resultBox.getChildren().clear();

        resultBox.getChildren().add(
                createReportCard(title, getSingleValue(countSql), "report-card-green")
        );

        Label tableTitle = new Label("ПОСТУПИВШИЕ ПАЦИЕНТЫ");
        tableTitle.getStyleClass().add("report-section-title");

        TableView<ObservableList<String>> table = createReportTable(patientsSql);

        resultBox.getChildren().addAll(tableTitle, table);

        playPageAnimation(resultBox);
    }

    private void showDepartmentOccupancyTable(VBox box) {
        box.getChildren().clear();

        Label title = new Label("ЗАГРУЖЕННОСТЬ ОТДЕЛЕНИЙ");
        title.getStyleClass().add("report-section-title");

        TableView<ObservableList<String>> table = createReportTable("""
        SELECT
            Department.department_name AS [Отделение],
            COUNT(Patient.patient_id) AS [Пациентов],
            SUM(Ward.kol_seats) AS [Всего мест],
            SUM(Ward.kol_seats) - COUNT(Patient.patient_id) AS [Свободно],
            CAST(
                COUNT(Patient.patient_id) * 100.0 / SUM(Ward.kol_seats)
            AS DECIMAL(5,1)) AS [Занято %]
        FROM Department
        INNER JOIN Ward ON Department.department_id = Ward.department_id
        LEFT JOIN Patient ON Ward.ward_id = Patient.ward_id
        GROUP BY Department.department_name
        ORDER BY [Занято %] DESC
        """);

        box.getChildren().addAll(title, table);
        playPageAnimation(box);
    }

    private void showCriticalWardsTable(VBox box) {

        box.getChildren().clear();

        Label title = new Label("ПАЛАТЫ СО СВОБОДНЫМИ МЕСТАМИ");
        title.getStyleClass().add("report-section-title");

        TableView<ObservableList<String>> table = createReportTable("""
        SELECT
            Ward.number_of_ward AS [Номер палаты],
            Ward.type_ward AS [Тип палаты],
            Department.department_name AS [Отделение],
            Ward.kol_seats AS [Всего мест],
            COUNT(Patient.patient_id) AS [Занято],
            Ward.kol_seats - COUNT(Patient.patient_id) AS [Свободно]
        FROM Ward
        INNER JOIN Department
            ON Ward.department_id = Department.department_id
        LEFT JOIN Patient
            ON Ward.ward_id = Patient.ward_id
        GROUP BY
            Ward.number_of_ward,
            Ward.type_ward,
            Department.department_name,
            Ward.kol_seats
        HAVING Ward.kol_seats - COUNT(Patient.patient_id) > 0
        ORDER BY [Свободно] ASC
        """);

        box.getChildren().addAll(title, table);

        playPageAnimation(box);
    }

    private TableView<ObservableList<String>> createReportTable(String sql) {
        TableView<ObservableList<String>> table = new TableView<>();

        table.getStyleClass().add("report-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setPrefHeight(520);
        table.setMinHeight(520);
        table.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(table, Priority.ALWAYS);

        loadCustomTable(table, sql);

        reportTables.add(table);

        return table;
    }

    private void loadCustomTable(TableView<ObservableList<String>> table, String sql) {

        table.getColumns().clear();
        table.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {

                final int columnIndex = i - 1;

                TableColumn<ObservableList<String>, String> column =
                        new TableColumn<>(metaData.getColumnName(i));

                column.setCellValueFactory(data ->
                        new SimpleStringProperty(
                                data.getValue().get(columnIndex)
                        )
                );

                table.getColumns().add(column);
            }

            ObservableList<ObservableList<String>> data =
                    FXCollections.observableArrayList();

            while (rs.next()) {

                ObservableList<String> row =
                        FXCollections.observableArrayList();

                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }

                data.add(row);
            }

            table.setItems(data);

            autoResizeColumns(table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoResizeColumns(TableView<ObservableList<String>> table) {

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        double totalTextWidth = 0;

        List<Double> widths = new ArrayList<>();

        for (TableColumn<ObservableList<String>, ?> column : table.getColumns()) {

            Text text = new Text(column.getText());

            text.setStyle("""
            -fx-font-size: 18px;
            -fx-font-weight: bold;
        """);

            double maxWidth =
                    text.getLayoutBounds().getWidth();

            for (int i = 0; i < table.getItems().size(); i++) {

                Object cellData = column.getCellData(i);

                if (cellData != null) {

                    text = new Text(cellData.toString());

                    text.setStyle("""
                    -fx-font-size: 17px;
                """);

                    double width =
                            text.getLayoutBounds().getWidth();

                    if (width > maxWidth) {
                        maxWidth = width;
                    }
                }
            }

            maxWidth += 90;

            widths.add(maxWidth);

            totalTextWidth += maxWidth;
        }

        double tableWidth = table.getWidth();

        if (tableWidth <= 0) {
            tableWidth = 1650;
        }

        for (int i = 0; i < table.getColumns().size(); i++) {

            TableColumn<ObservableList<String>, ?> column =
                    table.getColumns().get(i);

            double percent =
                    widths.get(i) / totalTextWidth;

            column.prefWidthProperty().unbind();

            column.setResizable(true);

            column.setMinWidth(140);

            column.setPrefWidth(tableWidth * percent);
        }
    }

    @FXML
    private void handleExportReport() {
        if (reportTables.isEmpty()) {
            showWarning("Сначала выберите период отчёта");
            return;
        }

        boolean saved = exportService.exportReportToCsv(reportTables);

        if (!saved) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Экспорт");
        alert.setHeaderText(null);
        alert.setContentText("Отчёт успешно сохранён");
        alert.showAndWait();
    }
}
