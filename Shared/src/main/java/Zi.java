package main.java;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Zi implements Serializable, ProprietateSerializabila {

    private String data; //TODO trebuie sa rezolv tipul datei
    private Angajat angajatTura1;
    private Angajat angajatTura2;
    private Angajat angajatTura3;

    private transient SimpleStringProperty tura1;
    private transient SimpleStringProperty tura2;
    private transient SimpleStringProperty tura3;

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
    public Zi(@Nullable String data, @NotNull Angajat angajat, int poz) {
        this.data = data;
        switch(poz) {
            case 1: {
                angajatTura1 = angajat;
                tura1 = new SimpleStringProperty(angajatTura1.getNume());
                tura2 = new SimpleStringProperty("");
                tura3 = new SimpleStringProperty("");
                break;
            }
            case 2: {
                angajatTura2 = angajat;
                tura1 = new SimpleStringProperty("");
                tura2 = new SimpleStringProperty(angajatTura2.getNume());
                tura3 = new SimpleStringProperty("");
                break;
            }
            case 3: {
                angajatTura3 = angajat;
                tura1 = new SimpleStringProperty("");
                tura2 = new SimpleStringProperty("");
                tura3 = new SimpleStringProperty(angajatTura3.getNume());
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
    public Zi(@Nullable String data, @NotNull Angajat angajat1, @NotNull Angajat angajat2, int poz1, int poz2) {
        this.data = data;
        switch(poz1) {
            case 1: {
                angajatTura1 = angajat1;
                tura1 = new SimpleStringProperty(angajatTura1.getNume());
                break;
            }
            case 2: {
                angajatTura2 = angajat1;
                tura2 = new SimpleStringProperty(angajatTura2.getNume());
                break;
            }
            case 3: {
                angajatTura3 = angajat1;
                tura3 = new SimpleStringProperty(angajatTura3.getNume());
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
                    tura1 = new SimpleStringProperty(angajatTura1.getNume());
                    if(poz1 != 2)
                        tura2 = new SimpleStringProperty("");
                    else
                        tura3 = new SimpleStringProperty("");
                    break;
                }
                case 2: {
                    angajatTura2 = angajat2;
                    tura2 = new SimpleStringProperty(angajatTura2.getNume());
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
                    tura3 = new SimpleStringProperty(angajatTura3.getNume());
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
    public Zi(@Nullable String data, Angajat angajat1, Angajat angajat2, Angajat angajat3) {
        this.data = data;
        angajatTura1 = angajat1;
        angajatTura2 = angajat2;
        angajatTura3 = angajat3;
        tura1 = new SimpleStringProperty(angajatTura1.getNume());
        tura2 = new SimpleStringProperty(angajatTura2.getNume());
        tura3 = new SimpleStringProperty(angajatTura3.getNume());
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

    private void updateazaIndividual(Angajat angajat, SimpleStringProperty tura) {
        if(angajat == null) {
            if(tura == null)
                tura = new SimpleStringProperty("");
            else
                tura.set("");
        }
        else {
            if(tura == null)
                tura = new SimpleStringProperty(angajat.getNume());
            else
                tura.set(angajat.getNume());
        }
    }
    @Override
    public void updateazaProprietatile() {
        updateazaIndividual(angajatTura1, tura1);
        updateazaIndividual(angajatTura2, tura2);
        updateazaIndividual(angajatTura3, tura3);
    }
}
