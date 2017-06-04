package main.java;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Post implements Serializable, ProprietateSerializabila {

    private String numeRoot;
    private transient SimpleStringProperty nume;

    public Post(String nume) {
        numeRoot = nume;
        this.nume = new SimpleStringProperty(numeRoot);
    }

    @Override
    public void updateazaProprietatile() {
        if(nume == null)
            nume = new SimpleStringProperty(numeRoot);
        else
            nume.set(numeRoot);
    }

    @Override
    public String toString() {
        return numeRoot;
    }
}
