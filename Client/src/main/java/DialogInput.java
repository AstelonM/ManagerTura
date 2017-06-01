package main.java;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Clasa pentru dialog; imita clasa aplicatiei
 */
public class DialogInput {

    private String mod;
    private ManagerTuraMain aplicatie;
    private TextField input;
    private Label eroare;

    public DialogInput(String mod, ManagerTuraMain aplicatie) {
        this.mod = mod;
        this.aplicatie = aplicatie;
    }

    public Stage start() {
        Stage primaryStage = new Stage();
        GridPane root = new GridPane();
        root.setId("dialog");
        Label nume = new Label("Nume " + mod + ":");
        root.add(nume, 0, 0);
        input = new TextField();
        root.add(input, 0, 1, 2, 1);
        Button adauga = new Button("Adauga");
        Button renunta = new Button("Renunta");
        adauga.setOnAction(event -> {
            if(input.getText().trim().isEmpty())
                eroare.setText("Numele nu trebuie sa fie gol");
            else {
                aplicatie.rezultatDialog(input.getText(), mod);
                primaryStage.close();
            }
        });
        renunta.setOnAction(event -> primaryStage.close());
        root.add(adauga, 0, 2);
        root.add(renunta, 1, 2);
        eroare = new Label();
        eroare.setId("label_eroare");
        root.add(eroare, 1, 3, 2, 1);
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add("main/resources/stilizare.css");
        primaryStage.setTitle("Adauga un " + mod);
        primaryStage.setScene(scene);
        return primaryStage;
    }
}
