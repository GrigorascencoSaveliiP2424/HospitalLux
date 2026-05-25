package com.example.practica.model;

public abstract class BaseEntity {

    protected int id;

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id; // -> возвращает общий ID
    }

    public boolean hasValidId() {
        return id > 0; // -> проверяет, что ID корректный
    }
}