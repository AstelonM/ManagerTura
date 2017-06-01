package main.java;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Angajat implements Serializable, ProprietateSerializabila {

    private final String numeRoot;
    private transient SimpleStringProperty nume;

    public Angajat(String nume) {

        numeRoot = nume;
        this.nume = new SimpleStringProperty(numeRoot);
    }

    public String getNumeRoot() {
        return numeRoot;
    }

    public String getNume() {
        return nume.get();
    }

    public SimpleStringProperty numeProperty() {
        return nume;
    }

    @Override
    public void updateazaProprietatile() {
        if(nume == null)
            nume = new SimpleStringProperty(numeRoot);
        else
            nume.set(numeRoot);
    }
}
