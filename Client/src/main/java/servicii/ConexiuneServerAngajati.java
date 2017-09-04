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
import java.util.ArrayList;

public class ConexiuneServerAngajati extends Service<ArrayList<Angajat>> {

    private Post post;
    private Logger logger;

    public ConexiuneServerAngajati(Post post, Logger logger) {
        this.post = post;
        this.logger = logger;
    }

    @Override
    protected Task<ArrayList<Angajat>> createTask() {
        return new Task<ArrayList<Angajat>>(){

            @Override
            protected ArrayList<Angajat> call() throws Exception {
                ArrayList<Angajat> lista = new ArrayList<>();
                try (Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                    if (post == null)
                        output.writeInt(Cerere.CERE_TOTI_ANGAJATII);
                    else {
                        output.writeInt(Cerere.CERERE_ANGAJATI);
                        output.writeObject(post);
                    }
                    output.flush();
                    lista = (ArrayList<Angajat>) input.readObject();
                } catch (IOException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-au putut cere angajatii: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-au putut cere angajatii: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                }
                return lista;
            }
        };
    }
}
