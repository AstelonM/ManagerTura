package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.Logger.EvenimentLogger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ServerMain extends Application {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/managertura";
    public static final String DB_USER = "userPAO";
    public static final String DB_PASSWORD = "userPAO";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    private boolean ruleaza = true;
    private Logger logger;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox(8);
        root.setId("vbox");
        Label label = new Label("Serverul este pornit!");
        label.setId("label_server");
        logger = new Logger();
        root.getChildren().addAll(label, logger.getLista());

        Class.forName(DB_DRIVER);
        ServiciuServer server = new ServiciuServer();
        server.start();

        primaryStage.setOnCloseRequest(event -> {
            ruleaza = false;
            try (Socket socket = new Socket(AdresaPort.ADRESA, AdresaPort.PORT);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                output.writeInt(Cerere.INCHIDE);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add("main/resources/stilizare.css");
        primaryStage.setTitle("Manager de Tura (Server)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private class ServiciuServer extends Thread {

        private ServerSocket server;

        @Override
        public void run() {
            try {
                server = new ServerSocket(AdresaPort.PORT);
                Platform.runLater(() -> logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                        "Serverul accepta!", LocalDateTime.now())));
                while (ruleaza) {
                    Socket conexiune = server.accept();
                    Platform.runLater(() -> logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                            "Conexiune stabilita cu " + conexiune.getInetAddress().toString(), LocalDateTime.now())));
                    ObjectInputStream input = new ObjectInputStream(conexiune.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(conexiune.getOutputStream());
                    int cerere =  input.readInt();
                    if (cerere == Cerere.INCHIDE)
                    {
                        input.close();
                        conexiune.close();
                        break;
                    }
                    else {
                        new ConexiuneClient(conexiune, cerere, input, output, logger).start();
                    }
                }
            } catch (EOFException e) {
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
}
