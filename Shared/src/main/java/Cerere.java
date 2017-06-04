package main.java;

public enum Cerere {
    CERERE_ANGAJATI(0), CERERE_POSTURI(1), CERERE_LUNA(2), ANGAJAT_NOU(3), POST_NOU(4), INCHIDE(5);

    private int value;
    Cerere(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
