package org.example.model;

import java.util.ArrayList;
import java.util.List;

/** Учебная группа со списком студентов. */
public class Group {
    private String name;
    private List<Student> students = new ArrayList<>();

    /** Конструктор без аргументов нужен для DSL. */
    public Group() {
    }

    /** Конструктор с именем группы. */
    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
