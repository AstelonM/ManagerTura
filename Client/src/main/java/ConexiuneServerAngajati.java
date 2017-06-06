package main.java;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clasa pentru a cere lista angajatilor de la server
 */
public class ConexiuneServerAngajati extends Service<ArrayList<Angajat>> {

    private class TaskAngajati extends Task<ArrayList<Angajat>> {

        @Override
        protected ArrayList<Angajat> call() throws Exception {
            ArrayList<Angajat> lista = new ArrayList<>();
            try(Socket socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeInt(Cerere.CERERE_ANGAJATI);
                output.flush();
                lista = (ArrayList<Angajat>) input.readObject();
                lista.forEach(Angajat::updateazaProprietatile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return lista;
        }
    }

    @Override
    protected Task<ArrayList<Angajat>> createTask() {
        return new TaskAngajati();
    }
}
