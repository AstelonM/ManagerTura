package main.java;

import javafx.beans.property.SimpleStringProperty;

public class Angajat extends DataRoot implements ProprietateSerializabila {

    private transient SimpleStringProperty nume;

    public Angajat(String nume) {
        super(nume);
        this.nume = new SimpleStringProperty(numeRoot);
    }

    /*public String getNume() {
        return nume.get();
    }*/

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

    @Override
    public String toString() {
        return numeRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angajat angajat = (Angajat) o;

        return numeRoot.equals(angajat.numeRoot);
    }

    @Override
    public int hashCode() {
        return numeRoot.hashCode();
    }
}
