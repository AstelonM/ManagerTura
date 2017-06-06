package main.java;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clasa pentru a cere lista posturilor de la server
 */
public class ConexiuneServerPosturi extends Service<ArrayList<Post>> {

    private class TaskPosturi extends Task<ArrayList<Post>> {

        @Override
        protected ArrayList<Post> call() throws Exception {
            ArrayList<Post> lista = new ArrayList<>();
            try(Socket socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeInt(Cerere.CERERE_POSTURI);
                output.flush();
                lista = (ArrayList<Post>) input.readObject();
                lista.forEach(Post::updateazaProprietatile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return lista;
        }
    }

    @Override
    protected Task<ArrayList<Post>> createTask() {
        return new TaskPosturi();
    }
}
