package main.java;

import javafx.beans.property.SimpleStringProperty;

public class Zi {

    private String data; //TODO trebuie sa rezolv tipul datei
    private Angajat angajatTura1;
    private Angajat angajatTura2;
    private Angajat angajatTura3;

    private final SimpleStringProperty tura1 = new SimpleStringProperty("");
    private final SimpleStringProperty tura2 = new SimpleStringProperty("");
    private final SimpleStringProperty tura3 = new SimpleStringProperty("");

    public Zi() {

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Angajat getAngajatTura1() {
        return angajatTura1;
    }

    public void setAngajatTura1(Angajat angajatTura1) {
        this.angajatTura1 = angajatTura1;
    }

    public Angajat getAngajatTura2() {
        return angajatTura2;
    }

    public void setAngajatTura2(Angajat angajatTura2) {
        this.angajatTura2 = angajatTura2;
    }

    public Angajat getAngajatTura3() {
        return angajatTura3;
    }

    public void setAngajatTura3(Angajat angajatTura3) {
        this.angajatTura3 = angajatTura3;
    }

    public String getTura1() {
        return tura1.get();
    }

    public SimpleStringProperty tura1Property() {
        return tura1;
    }

    public void setTura1(String tura1) {
        this.tura1.set(tura1);
    }

    public String getTura2() {
        return tura2.get();
    }

    public SimpleStringProperty tura2Property() {
        return tura2;
    }

    public void setTura2(String tura2) {
        this.tura2.set(tura2);
    }

    public String getTura3() {
        return tura3.get();
    }

    public SimpleStringProperty tura3Property() {
        return tura3;
    }

    public void setTura3(String tura3) {
        this.tura3.set(tura3);
    }
}
