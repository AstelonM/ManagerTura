package main.java;


import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexiuneServerAdauga extends Service {

    private Cerere cerere;
    private Object obiect;

    public ConexiuneServerAdauga(Cerere cerere, Object obiect) {
        this.cerere = cerere;
        this.obiect = obiect;
    }

    @Override
    protected Task createTask() {
        return new Task() {

            @Override
            protected Object call() throws Exception {
                Socket socket = null;
                ObjectOutputStream output = null;
                ObjectInputStream input = null;
                try {
                    socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                    output = new ObjectOutputStream(socket.getOutputStream());
                    input = new ObjectInputStream(socket.getInputStream());
                    switch(cerere) {
                        case ANGAJAT_NOU:
                        {
                            output.writeInt(cerere.getValue());
                            output.writeObject((Angajat) obiect);
                            break;
                        }
                        case POST_NOU:
                        {
                            output.writeInt(cerere.getValue());
                            output.writeObject((Post) obiect);
                            break;
                        }
                        default:
                        {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(output != null)
                        output.close();
                    if(socket != null)
                        socket.close();
                    if(input != null)
                        input.close();
                }
                return null;
            }
        };
    }
}
