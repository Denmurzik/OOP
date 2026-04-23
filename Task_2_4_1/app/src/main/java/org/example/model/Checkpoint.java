package org.example.model;

import java.time.LocalDate;

/** Контрольная точка курса. */
public class Checkpoint {
    private String name;
    private LocalDate date;

    /** Конструктор без аргументов нужен для DSL. */
    public Checkpoint() {
    }

    /** Полный конструктор. */
    public Checkpoint(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
