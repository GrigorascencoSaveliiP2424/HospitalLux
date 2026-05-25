package com.example.practica.model;

public class Department extends BaseEntity {

    private String departmentName;
    private String specialization;
    private int kolWard;

    public Department(int departmentId, String departmentName,
                      String specialization, int kolWard) {
        super(departmentId);
        this.departmentName = departmentName;
        this.specialization = specialization;
        this.kolWard = kolWard;
    }

    public String getDepartmentName() { return departmentName; }
    public String getSpecialization() { return specialization; }
    public int getKolWard() { return kolWard; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setKolWard(int kolWard) { this.kolWard = kolWard; }
}