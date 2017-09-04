package main.java;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Zi implements Serializable {

    private LocalDate dataO;
    private Angajat angajatTura1;
    private Angajat angajatTura2;
    private Angajat angajatTura3;
    private Post post;

    private transient SimpleStringProperty data;
    private transient SimpleStringProperty tura1;
    private transient SimpleStringProperty tura2;
    private transient SimpleStringProperty tura3;

    public static final int TURA1 = 1;
    public static final int TURA2 = 2;
    public static final int TURA3 = 3;

    public Zi(LocalDate dataO, Angajat angajat1, Angajat angajat2, Angajat angajat3, Post post) {
        this.dataO = dataO;
        data = new SimpleStringProperty(String.valueOf(dataO.getDayOfMonth()) + " " +
                dayOfWeekToRoDate(dataO.getDayOfWeek()));
        this.post = post;
        angajatTura1 = angajat1;
        angajatTura2 = angajat2;
        angajatTura3 = angajat3;
        if (angajatTura1 != null)
            tura1 = new SimpleStringProperty(angajatTura1.getNume());
        else
            tura1 = new SimpleStringProperty("Adauga");
        if (angajatTura2 != null)
            tura2 = new SimpleStringProperty(angajatTura2.getNume());
        else
            tura2 = new SimpleStringProperty("Adauga");
        if (angajatTura3 != null)
            tura3 = new SimpleStringProperty(angajatTura3.getNume());
        else
            tura3 = new SimpleStringProperty("Adauga");
    }

    public Zi(LocalDate dataO, Post post) {
        this.dataO = dataO;
        this.post = post;
        data = new SimpleStringProperty(String.valueOf(dataO.getDayOfMonth()) + " " +
                dayOfWeekToRoDate(dataO.getDayOfWeek()));
        tura1 = new SimpleStringProperty("Adauga");
        tura2 = new SimpleStringProperty("Adauga");
        tura3 = new SimpleStringProperty("Adauga");
    }

    private String dayOfWeekToRoDate(DayOfWeek data) {
        switch(data) {
            case MONDAY:
            {
                return "Luni";
            }
            case TUESDAY:
            {
                return "Marti";
            }
            case WEDNESDAY:
            {
                return "Miercuri";
            }
            case THURSDAY:
            {
                return "Joi";
            }
            case FRIDAY:
            {
                return "Vineri";
            }
            case SATURDAY:
            {
                return "Sambata";
            }
            case SUNDAY:
            {
                return "Duminica";
            }
        }
        return "Luni";
    }

    public void updateazaProprietatile() {
        if (dataO == null)
            data = new SimpleStringProperty("");
        else
            data = new SimpleStringProperty(String.valueOf(dataO.getDayOfMonth()) + " " +
                    dayOfWeekToRoDate(dataO.getDayOfWeek()));
        if (angajatTura1 == null)
            tura1 = new SimpleStringProperty("Adauga");
        else
            tura1 = new SimpleStringProperty(angajatTura1.getNume());
        if (angajatTura2 == null)
            tura2 = new SimpleStringProperty("Adauga");
        else
            tura2 = new SimpleStringProperty(angajatTura2.getNume());
        if (angajatTura3 == null)
            tura3 = new SimpleStringProperty("Adauga");
        else
            tura3 = new SimpleStringProperty(angajatTura3.getNume());
    }

    public String getData() {
        return data.get();
    }

    public SimpleStringProperty dataProperty() {
        return data;
    }

    public String getTura1() {
        return tura1.get();
    }

    public SimpleStringProperty tura1Property() {
        return tura1;
    }

    public String getTura2() {
        return tura2.get();
    }

    public SimpleStringProperty tura2Property() {
        return tura2;
    }

    public String getTura3() {
        return tura3.get();
    }

    public SimpleStringProperty tura3Property() {
        return tura3;
    }

    public void setAngajat(Angajat angajat, int tura) {
        switch (tura) {
            case TURA1: {
                angajatTura1 = angajat;
                tura1.set(angajat.getNume());
                break;
            }
            case TURA2: {
                angajatTura2 = angajat;
                tura2.set(angajat.getNume());
                break;
            }
            case TURA3: {
                angajatTura3 = angajat;
                tura3.set(angajat.getNume());
                break;
            }
        }
    }

    public LocalDate getDataO() { return dataO; }

    public Post getPost() { return post; }
}
