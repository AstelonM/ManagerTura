package main.java;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Clasa pentru a cere lista de programul stabilit pentru o anumita luna
 */
public class ConexiuneServerLuna extends Service<ArrayList<Zi>> {

    private Date start;
    private Date end;
    private Post post;

    public ConexiuneServerLuna(Date start, Date end, Post post) {
        this.start = start;
        this.end = end;
        this.post = post;
    }

    private class TaskZile extends Task<ArrayList<Zi>> {

        @Override
        protected ArrayList<Zi> call() throws Exception {
            ArrayList<Zi> lista = new ArrayList<>();
            try(Socket socket = new Socket(AdresaPort.adresa, AdresaPort.port);
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
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return lista;
        }
    }

    @Override
    protected Task<ArrayList<Zi>> createTask() {
        return new TaskZile();
    }
}
