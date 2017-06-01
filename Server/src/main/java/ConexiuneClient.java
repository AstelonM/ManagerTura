package main.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class ConexiuneClient extends Thread {

    Socket conexiune;

    public ConexiuneClient(Socket conexiune){
        this.conexiune = conexiune;
    }

    @Override
    public void run() {
        try(ObjectInputStream input = new ObjectInputStream(conexiune.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(conexiune.getOutputStream());
            Connection conexiuneDB = DriverManager.getConnection(ServerMain.DB_URL, ServerMain.DB_USER, ServerMain.DB_PASSWORD)) {
            Cerere cerere = (Cerere) input.readObject();
            switch(cerere) {
                case CERERE_ANGAJATI: {
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT nume FROM angajat ORDER BY nume;");
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Angajat> rezultat = new ArrayList<>();
                    while(rs.next()) {

                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
