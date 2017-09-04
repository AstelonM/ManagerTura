package main.java.dialoguri;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.ManagerTuraMain;
import main.java.Post;

import java.util.stream.Collectors;

public class DialogAngajati extends DialogInput {

    private ObservableList<String> listaPosturi;

    public DialogAngajati(ManagerTuraMain aplicatie, ObservableList<Post> posturi) {
        super(aplicatie);
        listaPosturi = FXCollections.observableArrayList();
        listaPosturi.addAll(posturi.stream().map(Post::getNume).collect(Collectors.toList()));
    }

    @Override
    public Stage start() {
        Stage primaryStage = new Stage();
        GridPane root = new GridPane();
        root.setId("dialog");
        Label eroare = new Label();
        Label nume = new Label("Nume angajat:");
        root.add(nume, 0, 0);
        TextField numeAngajat = new TextField();
        root.add(numeAngajat, 0, 1, 2, 1);
        Label numePost = new Label("Post:");
        root.add(numePost, 0, 2);
        ComboBox<String> posturi = new ComboBox<>();
        posturi.setItems(listaPosturi);
        posturi.setValue(listaPosturi.get(0));
        root.add(posturi, 1, 2);
        Button adauga = new Button("Adauga");
        Button renunta = new Button("Renunta");
        adauga.setOnAction(event -> {
            if(numeAngajat.getText().trim().isEmpty())
                eroare.setText("Numele nu trebuie sa fie gol");
            else {
                aplicatie.rezultatDialogAngajati(numeAngajat.getText(), posturi.getValue());
                primaryStage.close();
            }
        });
        renunta.setOnAction(event -> primaryStage.close());
        root.add(adauga, 0, 3);
        root.add(renunta, 1, 3);
        eroare.setId("label_eroare");
        root.add(eroare, 1, 4, 2, 1);
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add("main/resources/stilizare.css");
        primaryStage.setTitle("Adauga un angajat");
        primaryStage.setScene(scene);
        return primaryStage;
    }
}
