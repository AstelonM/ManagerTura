package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain extends Application {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/managertura";
    public static final String DB_USER = "userPAO";
    public static final String DB_PASSWORD = "userPAO";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private boolean ruleaza = true;

    public class ServiciuServer extends Thread {

        private ServerSocket server;

        /*@Override
        protected Task createTask() {
            return new Task() {

                @Override
                protected Object call() throws Exception {
                    try {
                        server = new ServerSocket(AdresaPort.port);
                        while (ruleaza) {
                            System.out.println("Server accepting");
                            Socket conexiune = server.accept();
                            ObjectInputStream input = new ObjectInputStream(conexiune.getInputStream());
                            ObjectOutputStream output = new ObjectOutputStream(conexiune.getOutputStream());
                            int cerereInt =  input.readInt();
                            Cerere cerere = (Cerere.values())[cerereInt];
                            if(cerere == Cerere.INCHIDE)
                            {
                                input.close();
                                conexiune.close();
                                break;
                            }
                            else
                                new ConexiuneClient(conexiune, cerere, input, output).start();
                        }
                    }
                    finally {
                        server.close();
                    }
                    System.out.println("Server closed");
                    return null;
                }
            };
        }*/
        @Override
        public void run() {
            try {
                server = new ServerSocket(AdresaPort.port);
                while (ruleaza) {
                    System.out.println("Server accepting");
                    Socket conexiune = server.accept();
                    System.out.println("A acceptat o conexiune");
                    ObjectInputStream input = new ObjectInputStream(conexiune.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(conexiune.getOutputStream());
                    System.out.println("Citire");
                    int cerereInt =  input.readInt();
                    Cerere cerere = (Cerere.values())[cerereInt];
                    System.out.println(cerere);
                    if(cerere == Cerere.INCHIDE)
                    {
                        input.close();
                        conexiune.close();
                        break;
                    }
                    else
                        new ConexiuneClient(conexiune, cerere, input, output).start();
                }
            } catch(EOFException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closed");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class.forName(DB_DRIVER);
        ServiciuServer server = new ServiciuServer();
        server.start();
        VBox root = new VBox(8);
        Label label = new Label("Serverul este pornit!");
        label.setId("label_server");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setOnCloseRequest(event -> {
            ruleaza = false;
            Socket socket = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                socket = new Socket(AdresaPort.adresa, AdresaPort.port);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                output.writeInt(Cerere.INCHIDE.getValue());
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if(socket != null) {
                        socket.close();
                    }
                    if(output != null) {
                        output.close();
                    }
                    if(input != null)
                        input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.setTitle("Manager de Tura (Server)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
