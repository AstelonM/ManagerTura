package main.java.servicii;


import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConexiuneServerTura extends Service<Boolean> {
    private Angajat angajat;
    private Zi zi;
    private int tura;
    private Boolean raspuns;
    private Logger logger;

    public ConexiuneServerTura(Angajat angajat, Zi zi, int tura, Logger logger) {
        this.angajat = angajat;
        this.zi = zi;
        this.tura = tura;
        this.logger = logger;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                try (Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                    output.writeInt(Cerere.TURA_NOUA);
                    output.writeObject(angajat);
                    output.writeObject(zi);
                    output.writeInt(tura);
                    output.flush();
                    raspuns = input.readBoolean();
                } catch (IOException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-a putut adauga tura: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                }
                return raspuns;
            }
        };
    }
}
