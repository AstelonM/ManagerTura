package main.java.servicii;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;
import main.java.Angajat;
import main.java.Cerere;
import main.java.Zi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexiuneServerTura extends Service<Boolean> {
    private Angajat angajat;
    private Zi zi;
    private int tura;
    private Boolean raspuns;

    public ConexiuneServerTura(Angajat angajat, Zi zi, int tura) {
        this.angajat = angajat;
        this.zi = zi;
        this.tura = tura;
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
                    e.printStackTrace();
                }
                return raspuns;
            }
        };
    }
}
