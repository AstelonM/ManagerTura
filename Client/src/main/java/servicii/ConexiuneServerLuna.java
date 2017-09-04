package main.java.servicii;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConexiuneServerLuna extends Service<ArrayList<Zi>> {

    private Date start;
    private Date end;
    private Post post;
    private Logger logger;

    public ConexiuneServerLuna(Date start, Date end, Post post, Logger logger) {
        this.start = start;
        this.end = end;
        this.post = post;
        this.logger = logger;
    }

    @Override
    protected Task<ArrayList<Zi>> createTask() {
        return new Task<ArrayList<Zi>>(){

            @Override
            protected ArrayList<Zi> call() throws Exception {
                ArrayList<Zi> lista = new ArrayList<>();
                try(Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                    output.writeInt(Cerere.CERERE_LUNA);
                    output.writeObject(start);
                    output.writeObject(end);
                    output.writeObject(post);
                    output.flush();
                    lista = (ArrayList<Zi>) input.readObject();
                    lista.forEach(Zi::updateazaProprietatile);
                } catch (IOException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-a putut cere programul: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    Platform.runLater(() -> logger.adaugaEveniment(new Logger.EvenimentLogger(Logger.EvenimentLogger.GRAD_EROARE,
                            "Nu s-a putut cere programul: " + e.getMessage(), LocalDateTime.now())));
                    e.printStackTrace();
                }
                return lista;
            }
        };
    }
}
