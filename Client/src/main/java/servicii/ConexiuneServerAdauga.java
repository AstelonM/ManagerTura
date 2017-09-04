package main.java.servicii;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexiuneServerAdauga extends Service {

    private int cerere;
    private Object obiect;

    public ConexiuneServerAdauga(int cerere, Object obiect) {
        this.cerere = cerere;
        this.obiect = obiect;
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
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
