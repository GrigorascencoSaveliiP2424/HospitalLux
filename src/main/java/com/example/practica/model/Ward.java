package com.example.practica.model;

public class Ward extends BaseEntity {

    private int numberOfWard;
    private String typeWard;
    private int kolSeats;
    private int departmentId;

    public Ward(int wardId, int numberOfWard, String typeWard,
                int kolSeats, int departmentId) {
        super(wardId);
        this.numberOfWard = numberOfWard;
        this.typeWard = typeWard;
        this.kolSeats = kolSeats;
        this.departmentId = departmentId;
    }

    public int getNumberOfWard() { return numberOfWard; }
    public String getTypeWard() { return typeWard; }
    public int getKolSeats() { return kolSeats; }
    public int getDepartmentId() { return departmentId; }

    public void setNumberOfWard(int numberOfWard) { this.numberOfWard = numberOfWard; }
    public void setTypeWard(String typeWard) { this.typeWard = typeWard; }
    public void setKolSeats(int kolSeats) { this.kolSeats = kolSeats; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
}