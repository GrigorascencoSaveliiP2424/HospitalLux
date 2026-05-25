package com.example.practica.model;

import java.sql.Date;

public class Patient extends BaseEntity {

    private String patientName;
    private String patientAdress;
    private String patientNumber;
    private String patientDiagnosis;
    private Date dateOfAdmission;
    private int wardId;

    private String departmentName;
    private String wardType;

    public Patient(int patientId, String patientName, String patientAdress,
                   String patientNumber, String patientDiagnosis,
                   Date dateOfAdmission, int wardId) {
        super(patientId);
        this.patientName = patientName;
        this.patientAdress = patientAdress;
        this.patientNumber = patientNumber;
        this.patientDiagnosis = patientDiagnosis;
        this.dateOfAdmission = dateOfAdmission;
        this.wardId = wardId;
    }

    public String getPatientName() { return patientName; }
    public String getPatientAdress() { return patientAdress; }
    public String getPatientNumber() { return patientNumber; }
    public String getPatientDiagnosis() { return patientDiagnosis; }
    public Date getDateOfAdmission() { return dateOfAdmission; }

    public String getDepartmentName() { return departmentName; }
    public String getWardType() { return wardType; }

    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientAdress(String patientAdress) { this.patientAdress = patientAdress; }
    public void setPatientNumber(String patientNumber) { this.patientNumber = patientNumber; }
    public void setPatientDiagnosis(String patientDiagnosis) { this.patientDiagnosis = patientDiagnosis; }
    public void setDateOfAdmission(Date dateOfAdmission) { this.dateOfAdmission = dateOfAdmission; }
    public void setWardId(int wardId) { this.wardId = wardId; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setWardType(String wardType) { this.wardType = wardType; }
}