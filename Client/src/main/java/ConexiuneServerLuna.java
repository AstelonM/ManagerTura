package main.java;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Clasa pentru a cere lista de programul stabilit pentru o anumita luna
 */
public class ConexiuneServerLuna extends Service<ObservableList<Zi>> {

    public ConexiuneServerLuna() {
        //TODO sa decid ce fel de timp trebuie trimis
    }

    @Override
    protected Task<ObservableList<Zi>> createTask() {
        return null;
    }
}
