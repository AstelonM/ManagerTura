package main.java;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Clasa pentru a cere lista angajatilor de la server
 */
public class ConexiuneServerAngajati extends Service<ObservableList<Angajat>> {

    private class TaskAngajati extends Task<ObservableList<Angajat>> {

        @Override
        protected ObservableList<Angajat> call() throws Exception {
            return null;
        }
    }

    @Override
    protected Task<ObservableList<Angajat>> createTask() {

    }
}
