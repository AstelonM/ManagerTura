package main.java;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Zi implements Serializable, ProprietateSerializabila {

    private LocalDate data;
    private Angajat angajatTura1;
    private Angajat angajatTura2;
    private Angajat angajatTura3;
    private Post post;

    private transient SimpleStringProperty dataZilei;
    private transient SimpleStringProperty tura1;
    private transient SimpleStringProperty tura2;
    private transient SimpleStringProperty tura3;
    private transient SimpleStringProperty postulZilei;

    public Zi() {
        tura1 = new SimpleStringProperty("");
        tura2 = new SimpleStringProperty("");
        tura3 = new SimpleStringProperty("");
    }

    /**
     * Constructor pentru initializarea unei singure ture
     * @param data data turei
     * @param angajat angajatul care se ocupa de o anumita tura
     * @param poz pozitia turei in calendar (1 pentru noapte, 2 pentru zi, 3 pentru seara
     */
    public Zi(@Nullable LocalDate data, @NotNull Angajat angajat, int poz, @NotNull Post post) {
        this.data = data;
        if(this.data == null)
            dataZilei = new SimpleStringProperty("");
        else
            dataZilei = new SimpleStringProperty(String.valueOf(this.data.getDayOfMonth()) +
                    this.data.getDayOfWeek());
        this.post = post;
        postulZilei = new SimpleStringProperty(this.post.toString());
        switch(poz) {
            case 1: {
                angajatTura1 = angajat;
                tura1 = new SimpleStringProperty(angajatTura1.getNumeRoot());
                tura2 = new SimpleStringProperty("");
                tura3 = new SimpleStringProperty("");
                break;
            }
            case 2: {
                angajatTura2 = angajat;
                tura1 = new SimpleStringProperty("");
                tura2 = new SimpleStringProperty(angajatTura2.getNumeRoot());
                tura3 = new SimpleStringProperty("");
                break;
            }
            case 3: {
                angajatTura3 = angajat;
                tura1 = new SimpleStringProperty("");
                tura2 = new SimpleStringProperty("");
                tura3 = new SimpleStringProperty(angajatTura3.getNumeRoot());
                break;
            }
            default: {
                tura1 = new SimpleStringProperty("");
                tura2 = new SimpleStringProperty("");
                tura3 = new SimpleStringProperty("");
                break;
            }
        }
    }

    /**
     * Constructor pentru a seta doua ture
     * @param data dat turei
     * @param angajat1 primul angajat de atribuit
     * @param angajat2 al doilea angajat de atribuit
     * @param poz1 pozitia turei primului angajat (1 pentru noape, 2 pentru zi, 3 pentru seara)
     * @param poz2 pozitia turei celui de-al doilea angajat (la fel ca poz1)
     */
    public Zi(@Nullable LocalDate data, @NotNull Angajat angajat1, @NotNull Angajat angajat2, int poz1, int poz2,
              @NotNull Post post) {
        this.data = data;
        if(this.data == null)
            dataZilei = new SimpleStringProperty("");
        else
            dataZilei = new SimpleStringProperty(String.valueOf(this.data.getDayOfMonth()) +
                    this.data.getDayOfWeek());
        this.post = post;
        postulZilei = new SimpleStringProperty(this.post.toString());
        switch(poz1) {
            case 1: {
                angajatTura1 = angajat1;
                tura1 = new SimpleStringProperty(angajatTura1.getNumeRoot());
                break;
            }
            case 2: {
                angajatTura2 = angajat1;
                tura2 = new SimpleStringProperty(angajatTura2.getNumeRoot());
                break;
            }
            case 3: {
                angajatTura3 = angajat1;
                tura3 = new SimpleStringProperty(angajatTura3.getNumeRoot());
                break;
            }
            default: {
                break;
            }
        }
        if(poz1 != poz2) {
            switch (poz2) {
                case 1: {
                    angajatTura1 = angajat2;
                    tura1 = new SimpleStringProperty(angajatTura1.getNumeRoot());
                    if(poz1 != 2)
                        tura2 = new SimpleStringProperty("");
                    else
                        tura3 = new SimpleStringProperty("");
                    break;
                }
                case 2: {
                    angajatTura2 = angajat2;
                    tura2 = new SimpleStringProperty(angajatTura2.getNumeRoot());
                    if(poz1 != 1)
                        tura1 = new SimpleStringProperty("");
                    else
                        tura3 = new SimpleStringProperty("");
                    break;
                }
                case 3: {
                    angajatTura3 = angajat2;
                    if(poz1 != 1)
                        tura1 = new SimpleStringProperty("");
                    else
                        tura2 = new SimpleStringProperty("");
                    tura3 = new SimpleStringProperty(angajatTura3.getNumeRoot());
                    break;
                }
                default: {
                    if(poz1 != 1)
                        tura1 = new SimpleStringProperty("");
                    if(poz1 != 2)
                        tura2 = new SimpleStringProperty("");
                    if(poz1 != 3)
                        tura3 = new SimpleStringProperty("");
                    break;
                }
            }
        }
        else {
            if(poz1 != 1)
                tura1 = new SimpleStringProperty("");
            if(poz1 != 2)
                tura2 = new SimpleStringProperty("");
            if(poz1 != 3)
                tura3 = new SimpleStringProperty("");
        }
    }

    /**
     * Contrusctor pentru atribuirea tuturor turelor
     * @param data data turei
     * @param angajat1 angajatul turei de noapte
     * @param angajat2 angajatul turei de zi
     * @param angajat3 angajatul turei de seara
     */
    public Zi(@Nullable LocalDate data, Angajat angajat1, Angajat angajat2, Angajat angajat3, Post post) {
        this.data = data;
        if(this.data == null)
            dataZilei = new SimpleStringProperty("");
        else
            dataZilei = new SimpleStringProperty(String.valueOf(this.data.getDayOfMonth()) +
                    this.data.getDayOfWeek());
        this.post = post;
        postulZilei = new SimpleStringProperty(this.post.toString());
        angajatTura1 = angajat1;
        angajatTura2 = angajat2;
        angajatTura3 = angajat3;
        tura1 = new SimpleStringProperty(angajatTura1.getNumeRoot());
        tura2 = new SimpleStringProperty(angajatTura2.getNumeRoot());
        tura3 = new SimpleStringProperty(angajatTura3.getNumeRoot());
    }

    public Zi(LocalDate data) {
        this.data = data;
        String dataFormat = String.valueOf(data.getDayOfMonth()) + " ";
        dataZilei = new SimpleStringProperty(String.valueOf(data.getDayOfMonth()) + " " +
                dayOfWeekToRoDate(data.getDayOfWeek()));
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

    private void updateazaIndividual(Angajat angajat, SimpleStringProperty tura) {
        if(angajat == null) {
            if(tura == null)
                tura = new SimpleStringProperty("");
            else
                tura.set("");
        }
        else {
            if(tura == null)
                tura = new SimpleStringProperty(angajat.getNumeRoot());
            else
                tura.set(angajat.getNumeRoot());
        }
    }
    @Override
    public void updateazaProprietatile() {
        if(data == null)
            dataZilei = new SimpleStringProperty("");
        else
            dataZilei = new SimpleStringProperty(String.valueOf(data.getDayOfMonth()) + " " +
                    data.getDayOfWeek().getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("ro")));
        updateazaIndividual(angajatTura1, tura1);
        updateazaIndividual(angajatTura2, tura2);
        updateazaIndividual(angajatTura3, tura3);
    }

    public String getDataZilei() {
        return dataZilei.get();
    }

    public SimpleStringProperty dataZileiProperty() {
        return dataZilei;
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
}
