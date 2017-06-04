package main.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ConexiuneClient extends Thread {

    private Socket conexiune;
    private Cerere cerere;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ConexiuneClient(Socket conexiune, Cerere cerere, ObjectInputStream input, ObjectOutputStream output){
        this.conexiune = conexiune;
        this.cerere = cerere;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        System.out.println("Test");
        try(//ObjectInputStream input = new ObjectInputStream(conexiune.getInputStream());
            //ObjectOutputStream output = new ObjectOutputStream(conexiune.getOutputStream());
            Connection conexiuneDB = DriverManager.getConnection(ServerMain.DB_URL, ServerMain.DB_USER, ServerMain.DB_PASSWORD)) {
            System.out.println("Conexiune");
            //Cerere cerere = (Cerere) input.readObject();
            switch(cerere) {
                case CERERE_ANGAJATI: {
                    System.out.println("Conexiune pentru angajati");
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT nume FROM angajat ORDER BY nume;");
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Angajat> rezultat = new ArrayList<>();
                    while(rs.next()) {
                        rezultat.add(new Angajat(rs.getString("nume")));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    break;
                }
                case CERERE_POSTURI: {
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT nume_post FROM post ORDER BY nume_post;");
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Post> rezultat = new ArrayList<>();
                    while(rs.next()) {
                        rezultat.add(new Post(rs.getString("nume_post")));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    break;
                }
                case CERERE_LUNA: {
                    Date dataStart = (Date) input.readObject();
                    Date dataEnd = (Date) input.readObject();
                    Post post = (Post) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT z.data, a1.nume, a2.nume, a3.nume  " +
                            "FROM zi_de_lucru z LEFT JOIN angajat a1 ON z.id_angajat_tura1 = a1.id_angajat " +
                            "LEFT JOIN angajat a2 ON z.id_angajat_tura2 = a2.id_angajat " +
                            "LEFT JOIN angajat a3 ON z.id_angajat_tura3 = a3.id_angajat " +
                            "JOIN post p ON z.id_post = p.id_post " +
                            "WHERE z.data BETWEEN ? AND ?  AND STRCMP(p.nume_post, ?) = 0;");
                    ps.setDate(1, dataStart);
                    ps.setDate(2, dataEnd);
                    ps.setString(3, post.toString());
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Zi> rezultat = new ArrayList<>();
                    while(rs.next()) {
                        LocalDate data = rs.getDate(1).toLocalDate();
                        String angajat1 = rs.getString(2);
                        if(rs.wasNull())
                            angajat1 = null;
                        String angajat2 = rs.getString(3);
                        if(rs.wasNull())
                            angajat2 = null;
                        String angajat3 = rs.getString(4);
                        if(rs.wasNull())
                            angajat3 = null;
                        Angajat a1, a2, a3;
                        if(angajat1 == null)
                            a1 = null;
                        else
                            a1 = new Angajat(angajat1);
                        if(angajat2 == null)
                            a2 = null;
                        else
                            a2 = new Angajat(angajat2);
                        if(angajat3 == null)
                            a3 = null;
                        else
                            a3 = new Angajat(angajat3);
                        rezultat.add(new Zi(data, a1, a2, a3, post));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    break;
                }
                case ANGAJAT_NOU: {
                    Angajat angajat = (Angajat) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement("INSERT IGNORE INTO angajat (nume) VALUE (?);");
                    ps.setString(1, angajat.getNumeRoot());
                    ps.executeUpdate();
                    ps.close();
                    break;
                }
                case POST_NOU: {
                    Post post = (Post) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement("INSERT IGNORE INTO post (nume_post) VALUE (?);");
                    ps.setString(1, post.toString());
                    ps.executeUpdate();
                    ps.close();
                    break;
                }
                default: {
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
        finally {
            try {
                conexiune.close();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
