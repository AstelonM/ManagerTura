package main.java;

import javafx.beans.property.SimpleStringProperty;

public class Angajat {

    private int id;
    private SimpleStringProperty nume;
    private static int lastId = 0;

    public Angajat(String nume) {
        this.nume = new SimpleStringProperty(nume);
        id = lastId++;
    }

    public String getName()
    {
        return nume.get();
    }
}
