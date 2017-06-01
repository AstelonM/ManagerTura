package main.java;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain extends Application {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/managertura";
    public static final String DB_USER = "userPAO";
    public static final String DB_PASSWORD = "userPAO";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private boolean ruleaza = true;

    public class ServiciuServer extends Service {

        @Override
        protected Task createTask() {
            return new Task() {

                @Override
                protected Object call() throws Exception {
                    try (ServerSocket server = new ServerSocket(Port.port)) {
                        while (ruleaza) {
                            Socket conexiune = server.accept();
                            new ConexiuneClient(conexiune).start();
                        }
                    }//TODO poate tratez exceptiile
                    return null;
                }
            };
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class.forName(DB_DRIVER);
        VBox root = new VBox(8);

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setMaximized(true);
        primaryStage.setTitle("Manager de Tura (Server)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
