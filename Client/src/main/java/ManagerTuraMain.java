package main.java;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManagerTuraMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/main/resources/manager_tura.fxml"));
        BorderPane root = new BorderPane();
        AnchorPane top = new AnchorPane();
        //bottom
        //left
        //right
        ScrollPane center = new ScrollPane();

        //Initializare top
        MenuBar meniu = new MenuBar();
        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        meniu.getMenus().addAll(file, edit);
        AnchorPane.setTopAnchor(meniu, 0.0);
        AnchorPane.setLeftAnchor(meniu, 0.0);
        AnchorPane.setRightAnchor(meniu, 0.0);
        top.getChildren().addAll(meniu);


        //Initializare bottom

        //Initializare left

        //Initializare right

        //Initializare center
        VBox center2 = new VBox();
        center2.setAlignment(Pos.CENTER);

        //Totul vine impreuna aici
        root.setTop(top);
        //root.setBottom(bottom);
        //root.setLeft(left);
        //root.setRight(right);
        root.setCenter(center);

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("Manager de Tura");
        stage.setScene(scene);
        stage.show();
    }
}
