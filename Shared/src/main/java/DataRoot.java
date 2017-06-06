package main.java;

import java.io.Serializable;

public abstract class DataRoot implements Serializable {
    protected final String numeRoot;

    public DataRoot(String nume) {
        numeRoot = nume;
    }

    public String getNumeRoot() {
        return numeRoot;
    }
}
