package main.java.dialoguri;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.ManagerTuraMain;

public class DialogPosturi extends DialogInput {

    public DialogPosturi(ManagerTuraMain aplicatie) {
        super(aplicatie);
    }

    @Override
    public Stage start() {
        Stage primaryStage = new Stage();
        GridPane root = new GridPane();
        root.setId("dialog");
        Label eroare = new Label();
        Label nume = new Label("Nume post:");
        root.add(nume, 0, 0);
        TextField numePost = new TextField();
        root.add(numePost, 0, 1, 2, 1);
        Button adauga = new Button("Adauga");
        Button renunta = new Button("Renunta");
        adauga.setOnAction(event -> {
            if(numePost.getText().trim().isEmpty())
                eroare.setText("Numele nu trebuie sa fie gol");
            else {
                aplicatie.rezultatDialogPosturi(numePost.getText());
                primaryStage.close();
            }
        });
        renunta.setOnAction(event -> primaryStage.close());
        root.add(adauga, 0, 2);
        root.add(renunta, 1, 2);
        eroare.setId("label_eroare");
        root.add(eroare, 1, 3, 2, 1);
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add("main/resources/stilizare.css");
        primaryStage.setTitle("Adauga un post");
        primaryStage.setScene(scene);
        return primaryStage;
    }
}
