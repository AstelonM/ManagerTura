package main.java.servicii;


import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;
import main.java.Logger;
import main.java.Logger.EvenimentLogger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConexiuneServerAdauga extends Service {

    private int cerere;
    private Object obiect;
    private Logger logger;

    public ConexiuneServerAdauga(int cerere, Object obiect, Logger logger) {
        this.cerere = cerere;
        this.obiect = obiect;
        this.logger = logger;
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                try (Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    output.writeInt(cerere);
                    output.writeObject(obiect);
                    output.flush();
                } catch (IOException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_EROARE,
                            "Nu s-au putut adauga angajatul/postul: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
