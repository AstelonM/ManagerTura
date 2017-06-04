package main.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clasa pentru a cere lista posturilor de la server
 */
public class ConexiuneServerPosturi extends Thread {
/*
    private class TaskPosturi extends Task<ArrayList<Post>> {

        @Override
        protected ArrayList<Post> call() throws Exception {
            ArrayList<Post> lista = new ArrayList<>();
            Socket socket = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(Cerere.CERERE_POSTURI);
                lista = (ArrayList<Post>) input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(output != null)
                    output.close();
                if(input != null)
                    input.close();
                if(socket != null)
                    socket.close();
            }
            return lista;
        }
    }

    @Override
    protected Task<ArrayList<Post>> createTask() {
        return new TaskPosturi();
    }*/
    ManagerTuraMain aplicatie;
    ConexiuneServerAngajati con;

    public ConexiuneServerPosturi(ManagerTuraMain app, ConexiuneServerAngajati con) {
        aplicatie = app;
        this.con = con;
    }

    @Override
    public void run() {
        try {
            con.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Post> lista = new ArrayList<>();
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
            socket = new Socket(AdresaPort.adresa, AdresaPort.port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            output.writeInt(Cerere.CERERE_POSTURI.getValue());
            output.flush();
            lista = (ArrayList<Post>) input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null)
                    input.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        aplicatie.rezultatPost(lista);
    }
}
