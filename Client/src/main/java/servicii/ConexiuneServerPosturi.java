package main.java.servicii;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;
import main.java.Cerere;
import main.java.Logger;
import main.java.Post;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConexiuneServerPosturi extends Service<ArrayList<Post>> {

    private Logger logger;

    public ConexiuneServerPosturi(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected Task<ArrayList<Post>> createTask() {
        return new Task<ArrayList<Post>>(){

            @Override
            protected ArrayList<Post> call() throws Exception {
                ArrayList<Post> lista = new ArrayList<>();
                try(Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                    output.writeInt(Cerere.CERERE_POSTURI);
                    output.flush();
                    lista = (ArrayList<Post>) input.readObject();
                } catch (IOException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-au putut cere posturile: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-au putut cere posturile: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                }
                return lista;
            }
        };
    }
}
