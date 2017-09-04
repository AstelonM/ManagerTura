package main.java;

import main.java.Logger.EvenimentLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConexiuneClient extends Thread {

    private Socket conexiune;
    private int cerere;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Logger logger;

    public ConexiuneClient(Socket conexiune, int cerere, ObjectInputStream input, ObjectOutputStream output, Logger logger){
        this.conexiune = conexiune;
        this.cerere = cerere;
        this.input = input;
        this.output = output;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (Connection conexiuneDB = DriverManager.getConnection(ServerMain.DB_URL, ServerMain.DB_USER, ServerMain.DB_PASSWORD)) {
            switch (cerere) {
                case Cerere.CERE_TOTI_ANGAJATII: {
                    PreparedStatement ps = conexiuneDB.prepareStatement(
                            "SELECT a.nume, p.nume_post FROM angajat a LEFT JOIN post p ON a.id_post = p.id_post " +
                                    "ORDER BY a.nume;");
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Angajat> rezultat = new ArrayList<>();
                    while (rs.next()) {
                        String numeAngajat = rs.getString(1);
                        String numePost = rs.getString(2);
                        Post post;
                        if (numePost == null)
                            post = null;
                        else
                            post = new Post(numePost);
                        rezultat.add(new Angajat(numeAngajat, post));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                            cerere + " (cere toti angajatii)", LocalDateTime.now()));
                    break;
                }

                case Cerere.CERERE_ANGAJATI: {
                    Post post = (Post) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement(
                            "SELECT a.nume FROM angajat a LEFT JOIN post p ON a.id_post = p.id_post " +
                                    "WHERE STRCMP(p.nume_post, ?) = 0 ORDER BY a.nume;");
                    ps.setString(1, post.getNume());
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Angajat> rezultat = new ArrayList<>();
                    while (rs.next())
                        rezultat.add(new Angajat(rs.getString(1), post));
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (cere angajati) pentru postul " + post.getNume(), LocalDateTime.now()));
                    break;
                }

                case Cerere.CERERE_POSTURI: {
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT nume_post FROM post ORDER BY nume_post;");
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Post> rezultat = new ArrayList<>();
                    while (rs.next()) {
                        rezultat.add(new Post(rs.getString(1)));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (cere posturile)", LocalDateTime.now()));
                    break;
                }

                case Cerere.CERERE_LUNA: {
                    Date dataStart = (Date) input.readObject();
                    Date dataEnd = (Date) input.readObject();
                    Post post = (Post) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement(
                            "SELECT z.data, a1.nume, a2.nume, a3.nume  " +
                            "FROM zi_de_lucru z LEFT JOIN angajat a1 ON z.id_angajat_tura1 = a1.id_angajat " +
                            "LEFT JOIN angajat a2 ON z.id_angajat_tura2 = a2.id_angajat " +
                            "LEFT JOIN angajat a3 ON z.id_angajat_tura3 = a3.id_angajat " +
                            "JOIN post p ON z.id_post = p.id_post " +
                            "WHERE z.data BETWEEN ? AND ?  AND STRCMP(p.nume_post, ?) = 0;");
                    ps.setDate(1, dataStart);
                    ps.setDate(2, dataEnd);
                    ps.setString(3, post.getNume());
                    ResultSet rs = ps.executeQuery();
                    ArrayList<Zi> rezultat = new ArrayList<>();
                    while (rs.next()) {
                        LocalDate data = rs.getDate(1).toLocalDate();
                        String angajat1 = rs.getString(2);
                        if (rs.wasNull())
                            angajat1 = null;
                        String angajat2 = rs.getString(3);
                        if (rs.wasNull())
                            angajat2 = null;
                        String angajat3 = rs.getString(4);
                        if (rs.wasNull())
                            angajat3 = null;
                        Angajat a1, a2, a3;
                        if (angajat1 == null)
                            a1 = null;
                        else
                            a1 = new Angajat(angajat1, post);
                        if (angajat2 == null)
                            a2 = null;
                        else
                            a2 = new Angajat(angajat2, post);
                        if (angajat3 == null)
                            a3 = null;
                        else
                            a3 = new Angajat(angajat3, post);
                        rezultat.add(new Zi(data, a1, a2, a3, post));
                    }
                    rs.close();
                    ps.close();
                    output.writeObject(rezultat);
                    output.flush();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (cere luna) pentru luna " + dataStart.toLocalDate().getMonthValue(),
                            LocalDateTime.now()));
                    break;
                }

                case Cerere.ANGAJAT_NOU: {
                    Angajat angajat = (Angajat) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement("SELECT id_post FROM post " +
                            "WHERE STRCMP(nume_post, ?) = 0;");
                    ps.setString(1, angajat.getPost().getNume());
                    ResultSet rs = ps.executeQuery();
                    int idPost = -1;
                    if (rs.first())
                        idPost = rs.getInt(1);
                    rs.close();
                    ps.close();
                    if (idPost == -1)
                        break;
                    ps = conexiuneDB.prepareStatement("INSERT IGNORE INTO angajat (nume, id_post) VALUES (?, ?);");
                    ps.setString(1, angajat.getNume());
                    ps.setInt(2, idPost);
                    ps.executeUpdate();
                    ps.close();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (adauga angajat) si a adaugat angajatul " +
                            angajat.getNume() + " pe postul " + angajat.getPost().getNume(), LocalDateTime.now()));
                    break;
                }

                case Cerere.POST_NOU: {
                    Post post = (Post) input.readObject();
                    PreparedStatement ps = conexiuneDB.prepareStatement("INSERT IGNORE INTO post (nume_post) VALUE (?);");
                    ps.setString(1, post.getNume());
                    ps.executeUpdate();
                    ps.close();
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (adauga post) si a adaugat postul " + post.getNume(), LocalDateTime.now()));
                    break;
                }

                case Cerere.TURA_NOUA: {
                    //Post post = (Post) input.readObject();
                    Angajat angajat = (Angajat) input.readObject();
                    Zi zi = (Zi) input.readObject();
                    Post post = zi.getPost();
                    Date data = Date.valueOf(zi.getDataO());
                    int tura = input.readInt();
                    boolean gasit = true;
                    PreparedStatement ps = null;
                    ResultSet rs;

                    //Verifica daca exista o zi care sa aiba angajatul dat intr-o tura adiacenta cu cea data
                    switch (tura) {
                        case 1: {
                            ps = conexiuneDB.prepareStatement(
                                    "(SELECT * FROM zi_de_lucru z LEFT JOIN post p ON z.id_post = p.id_post " +
                                    "LEFT JOIN angajat a ON z.id_angajat_tura3 = a.id_angajat " +
                                    "WHERE STRCMP(p.nume_post, ?) = 0 AND STRCMP(a.nume, ?) = 0 AND z.data = ? - INTERVAL 1 DAY) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z2 LEFT JOIN post p2 ON z2.id_post = p2.id_post " +
                                    "LEFT JOIN angajat a2 ON z2.id_angajat_tura1 = a2.id_angajat " +
                                    "WHERE STRCMP(p2.nume_post, ?) = 0 AND STRCMP(a2.nume, ?) = 0 AND z2.data = ?) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z3 LEFT JOIN post p3 ON z3.id_post = p3.id_post " +
                                    "LEFT JOIN angajat a3  ON z3.id_angajat_tura2 = a3.id_angajat " +
                                    "WHERE STRCMP(p3.nume_post, ?) = 0 AND STRCMP(a3.nume, ?) = 0 AND z3.data = ?);");
                            break;
                        }
                        case 2: {
                            ps = conexiuneDB.prepareStatement(
                                    "(SELECT * FROM zi_de_lucru z LEFT JOIN post p ON z.id_post = p.id_post " +
                                    "LEFT JOIN angajat a ON z.id_angajat_tura1 = a.id_angajat " +
                                    "WHERE STRCMP(p.nume_post, ?) = 0 AND STRCMP(a.nume, ?) = 0 AND z.data = ?) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z2 LEFT JOIN post p2 ON z2.id_post = p2.id_post " +
                                    "LEFT JOIN angajat a2 ON z2.id_angajat_tura2 = a2.id_angajat " +
                                    "WHERE STRCMP(p2.nume_post, ?) = 0 AND STRCMP(a2.nume, ?) = 0 AND z2.data = ?) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z3 LEFT JOIN post p3 ON z3.id_post = p3.id_post " +
                                    "LEFT JOIN angajat a3  ON z3.id_angajat_tura3 = a3.id_angajat " +
                                    "WHERE STRCMP(p3.nume_post, ?) = 0 AND STRCMP(a3.nume, ?) = 0 AND z3.data = ?);");
                            break;
                        }
                        case 3: {
                            ps = conexiuneDB.prepareStatement(
                                    "(SELECT * FROM zi_de_lucru z LEFT JOIN post p ON z.id_post = p.id_post " +
                                    "LEFT JOIN angajat a ON z.id_angajat_tura2 = a.id_angajat " +
                                    "WHERE STRCMP(p.nume_post, ?) = 0 AND STRCMP(a.nume, ?) = 0 AND z.data = ?) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z2 LEFT JOIN post p2 ON z2.id_post = p2.id_post " +
                                    "LEFT JOIN angajat a2 ON z2.id_angajat_tura3 = a2.id_angajat " +
                                    "WHERE STRCMP(p2.nume_post, ?) = 0 AND STRCMP(a2.nume, ?) = 0 AND z2.data = ?) " +
                                    "UNION " +
                                    "(SELECT * FROM zi_de_lucru z3 LEFT JOIN post p3 ON z3.id_post = p3.id_post " +
                                    "LEFT JOIN angajat a3  ON z3.id_angajat_tura1 = a3.id_angajat " +
                                    "WHERE STRCMP(p3.nume_post, ?) = 0 AND STRCMP(a3.nume, ?) = 0 AND z3.data = ? + INTERVAL 1 DAY);");
                            break;
                        }
                    }
                    //Teoretic, mereu o sa fie o tura buna, dar teoretic nu e mereu si practic
                    if (ps != null) {
                        ps.setString(1, post.getNume());
                        ps.setString(4, post.getNume());
                        ps.setString(7, post.getNume());
                        ps.setString(2, angajat.getNume());
                        ps.setString(5, angajat.getNume());
                        ps.setString(8, angajat.getNume());
                        ps.setDate(3, data);
                        ps.setDate(6, data);
                        ps.setDate(9, data);
                        rs = ps.executeQuery();
                        if (rs.first()) {
                            String rezultat = rs.getString(1);
                            gasit = rezultat != null;
                        }
                        else
                            gasit = false;
                        rs.close();
                        ps.close();
                        //Nu am gasit nicio zi care sa corespunda
                        if (!gasit) {
                            ps = conexiuneDB.prepareStatement(
                                    "SELECT a.id_angajat FROM angajat a JOIN post p ON a.id_post = p.id_post " +
                                            "WHERE STRCMP(nume, ?) = 0 AND STRCMP(p.nume_post, ?) = 0;");
                            ps.setString(1, angajat.getNume());
                            ps.setString(2, post.getNume());
                            rs = ps.executeQuery();
                            int idAngajat = -1;
                            int idPost = -1;
                            if (rs.first())
                                idAngajat = rs.getInt(1);
                            rs.close();
                            ps.close();
                            ps = conexiuneDB.prepareStatement("SELECT id_post FROM post WHERE STRCMP(nume_post, ?) = 0;");
                            ps.setString(1, post.getNume());
                            rs = ps.executeQuery();
                            if (rs.first())
                                idPost = rs.getInt(1);
                            rs.close();
                            ps.close();
                            //Verifica daca a gasit angajatul, mai mult in caz de ceva neprevazut
                            if (idAngajat != -1 && idPost != -1) {
                                //Verifica daca ziua exista deja
                                ps = conexiuneDB.prepareStatement("SELECT * FROM zi_de_lucru z " +
                                        "JOIN post p ON z.id_post = p.id_post " +
                                        "WHERE STRCMP(p.nume_post, ?) = 0 AND z.data = ?;");
                                ps.setString(1, post.getNume());
                                ps.setDate(2, data);
                                rs = ps.executeQuery();
                                boolean ziGasita = rs.first();
                                rs.close();
                                ps.close();
                                if (ziGasita) {
                                    switch (tura) {
                                        case 1: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "UPDATE zi_de_lucru SET id_angajat_tura1 = ? " +
                                                    "WHERE data = ? AND id_post = ?;");
                                            break;
                                        }
                                        case 2: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "UPDATE zi_de_lucru SET id_angajat_tura2 = ? " +
                                                            "WHERE data = ? AND id_post = ?;");
                                            break;
                                        }
                                        case 3: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "UPDATE zi_de_lucru SET id_angajat_tura3 = ? " +
                                                            "WHERE data = ? AND id_post = ?;");
                                            break;
                                        }
                                    }
                                    ps.setInt(1, idAngajat);
                                    ps.setDate(2, data);
                                    ps.setInt(3, idPost);
                                } else {
                                    switch (tura) {
                                        case 1: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "INSERT INTO zi_de_lucru (data, id_post, id_angajat_tura1) VALUES (?, ?, ?);");
                                            break;
                                        }
                                        case 2: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "INSERT INTO zi_de_lucru (data, id_post, id_angajat_tura2) VALUES (?, ?, ?);");
                                            break;
                                        }
                                        case 3: {
                                            ps = conexiuneDB.prepareStatement(
                                                    "INSERT INTO zi_de_lucru (data, id_post, id_angajat_tura3) VALUES (?, ?, ?);");
                                            break;
                                        }
                                    }
                                    ps.setDate(1, data);
                                    ps.setInt(2, idPost);
                                    ps.setInt(3, idAngajat);
                                }
                                ps.executeUpdate();
                                ps.close();
                            }
                        }
                        output.writeBoolean(gasit);
                    }
                    logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            conexiune.getInetAddress().toString() + " a executat cererea " +
                                    cerere + " (adauga tura)", LocalDateTime.now()));
                    break;
                }

                default: {
                    break;
                }
            }
        } catch (IOException e) {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                    conexiune.getInetAddress().toString() + " a cauzat exceptia " +
                            e.getMessage(), LocalDateTime.now()));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                    conexiune.getInetAddress().toString() + " a cauzat exceptia " +
                            e.getMessage(), LocalDateTime.now()));
            e.printStackTrace();
        } catch (SQLException e) {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                    conexiune.getInetAddress().toString() + " a cauzat exceptia " +
                            e.getMessage(), LocalDateTime.now()));
            e.printStackTrace();
        } finally {
            try {
                output.close();
                input.close();
                conexiune.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
