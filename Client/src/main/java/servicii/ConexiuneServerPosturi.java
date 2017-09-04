package main.java.servicii;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.AdresaPort;
import main.java.Cerere;
import main.java.Post;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConexiuneServerPosturi extends Service<ArrayList<Post>> {

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
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return lista;
            }
        };
    }
}
