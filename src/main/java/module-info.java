module com.example.practica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.practica to javafx.fxml;
    opens com.example.practica.controller to javafx.fxml;
    opens com.example.practica.model to javafx.base;


    exports com.example.practica;
    exports com.example.practica.controller;
    exports com.example.practica.model;
}