package main.java;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Clasa pentru a cere lista posturilor de la server
 */
public class ConexiuneServerPosturi extends Service<ObservableList<Post>> {

    @Override
    protected Task<ObservableList<Post>> createTask() {
        return null;
    }
}
