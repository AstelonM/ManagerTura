package main.java.servicii;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;
import main.java.Angajat;
import main.java.Cerere;
import main.java.Post;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConexiuneServerAngajati extends Service<ArrayList<Angajat>> {

    private Post post;

    public ConexiuneServerAngajati(Post post) {
        this.post = post;
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
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return lista;
            }
        };
    }
}
