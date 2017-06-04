package main.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clasa pentru a cere lista angajatilor de la server
 */
public class ConexiuneServerAngajati extends Thread {
/*
    private class TaskAngajati extends Task<ArrayList<Angajat>> {

        @Override
        protected ArrayList<Angajat> call() throws Exception {
            ArrayList<Angajat> lista = new ArrayList<>();
            Socket socket = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                output.writeObject(Cerere.CERERE_ANGAJATI);
                lista = (ArrayList<Angajat>) input.readObject();
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
    protected Task<ArrayList<Angajat>> createTask() {
        return new TaskAngajati();
    }*/
    ManagerTuraMain aplicatie;

    public ConexiuneServerAngajati(ManagerTuraMain app) {
        aplicatie = app;
    }

    @Override
    public void run() {
        ArrayList<Angajat> lista = new ArrayList<>();
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
            socket = new Socket(AdresaPort.adresa, AdresaPort.port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            Cerere cerereAngajati = Cerere.CERERE_ANGAJATI;
            System.out.println(cerereAngajati);
            output.writeInt(cerereAngajati.getValue());
            output.flush();
            System.out.println("A scris");
            //output.writeObject(Cerere.CERERE_ANGAJATI);
            lista = (ArrayList<Angajat>) input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        aplicatie.rezultatAngajat(lista);
    }
}
