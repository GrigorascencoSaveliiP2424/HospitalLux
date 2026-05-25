# HospitalLux

**HospitalLux** is a desktop application for managing hospital patients, wards, and departments.  
The project is built with JavaFX and uses a SQL Server database.

## Project Description

The application allows hospital staff to manage patient records, view ward and department information, generate reports, and export data to CSV files.

## Features

- add new patients;
- edit patient information;
- delete patients;
- view patient records;
- view wards and departments;
- search and filter data;
- check available ward places;
- generate reports;
- export data to CSV;
- validate user input.

## Technologies Used

- Java
- JavaFX
- FXML
- CSS
- JDBC
- SQL Server
- Maven

## Project Structure

```text
HospitalLux
├── src
│   └── main
│       ├── java
│       │   └── com.example.practica
│       │       ├── controller
│       │       ├── db
│       │       ├── model
│       │       ├── repository
│       │       └── service
│       └── resources
├── database
│   └── hospitallux.sql
├── pom.xml
