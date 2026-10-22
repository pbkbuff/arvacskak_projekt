package hu.unideb.inf.model;

import java.time.LocalDate;

public class Model {
    private Student student;

    public Model() {
        student = new Student("Robert Smith",
                                    18,
                                    LocalDate.of(2006,10,1));
    }

    public Student getStudent() {
        return student;
    }
}
