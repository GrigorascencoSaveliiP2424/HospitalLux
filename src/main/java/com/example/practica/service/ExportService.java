package com.example.practica.service;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportService {

    public boolean exportToCsv(TableView<ObservableList<String>> tableView) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Сохранить CSV файл");

        fileChooser.setInitialFileName("hospital_data.csv");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showSaveDialog(
                tableView.getScene().getWindow()
        );

        if (file == null) {
            return false;
        }

        try (FileWriter writer = new FileWriter(file)) {

            for (ObservableList<String> row : tableView.getItems()) {

                for (String cell : row) {

                    writer.write(cell + ";");
                }

                writer.write("\n");
            }

            System.out.println("Файл сохранён: " + file.getAbsolutePath());

            return true;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean exportReportToCsv(ObservableList<TableView<ObservableList<String>>> tables) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчёт CSV");
        fileChooser.setInitialFileName("hospital_report.csv");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showSaveDialog(null);

        if (file == null) {
            return false;
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (TableView<ObservableList<String>> table : tables) {
                for (TableColumn<ObservableList<String>, ?> column : table.getColumns()) {
                    writer.write(column.getText() + ";");
                }

                writer.write("\n");

                for (ObservableList<String> row : table.getItems()) {
                    for (String cell : row) {
                        writer.write(cell + ";");
                    }
                    writer.write("\n");
                }

                writer.write("\n\n");
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}