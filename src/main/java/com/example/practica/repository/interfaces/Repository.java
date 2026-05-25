package com.example.practica.repository.interfaces;

public interface Repository<T> {

    void add(T item);

    void update(T item);

    void delete(int id);
}