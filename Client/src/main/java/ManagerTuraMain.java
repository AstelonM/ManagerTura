package main.java;

import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;


public class ManagerTuraMain extends Application {

    private VBox center;
    private Stage fereastraDialog;

    private TableView calendar;
    private TableColumn dataCol;
    private TableColumn tura1Col;
    private TableColumn tura2Col;
    private TableColumn tura3Col;

    private ObservableList<Angajat> angajati = FXCollections.observableArrayList();
    private ObservableList<Post> posturi = FXCollections.observableArrayList();
    private ObservableList<Zi> programCurent = FXCollections.observableArrayList();
    private ListView<Angajat> listaAngajati = new ListView<>();
    private ListView<Post> listaPosturi = new ListView<>();
    Post postulCurent;

    private final String modAngajat = "Angajat";
    private final String modPost = "Post";

    private class AdaugaEventHandler implements EventHandler<ActionEvent> {

        private String mod;

        AdaugaEventHandler(String mod) {
            this.mod = mod;
        }

        @Override
        public void handle(ActionEvent event) {
            if(fereastraDialog == null) {
                DialogInput dialog = new DialogInput(mod, getApplication());
                fereastraDialog = dialog.start();
                fereastraDialog.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        AnchorPane top = new AnchorPane();
        VBox left = new VBox(8);
        VBox right = new VBox(8);
        center = new VBox();
        calendar = new TableView();
        ConexiuneServerAngajati conA = new ConexiuneServerAngajati();
        conA.setOnSucceeded(event -> {
            System.out.println("Angajati de la baza de date!");
            angajati.addAll(conA.getValue());
            listaAngajati.setItems(angajati);
            ConexiuneServerPosturi conP = new ConexiuneServerPosturi();
            conP.setOnSucceeded(event1 -> {
                System.out.println("Posturi de la baza de date!");
                posturi.addAll(conP.getValue());
                listaPosturi.setItems(posturi);
            });
            conP.start();
        });
        conA.start();


        //Initializare top
        MenuBar meniu = new MenuBar();
        Menu meniuFile = new Menu("File");
        MenuItem meniuFileExit = new MenuItem("Exit");
        meniuFile.getItems().add(meniuFileExit);
        Menu meniuMod = new Menu("Mod");
        MenuItem meniuModAdaugaAngajati = new MenuItem("Adauga angajati");
        meniuModAdaugaAngajati.setOnAction(new AdaugaEventHandler(modAngajat));
        MenuItem meniuModAdaugaPosturi = new MenuItem("Adauga posturi");
        meniuModAdaugaPosturi.setOnAction(new AdaugaEventHandler(modPost));
        MenuItem meniuModTure = new MenuItem("Stabileste turele");
        meniuModTure.setOnAction(this::initModTure);
        MenuItem meniuModLuna = new MenuItem("Afiseaza luna curenta");
        meniuModLuna.setOnAction(this::initModLuna);
        meniuMod.getItems().addAll(meniuModAdaugaAngajati, meniuModAdaugaPosturi, meniuModTure, meniuModLuna);
        meniu.getMenus().addAll(meniuFile, meniuMod);
        AnchorPane.setTopAnchor(meniu, 0.0);
        AnchorPane.setLeftAnchor(meniu, 0.0);
        AnchorPane.setRightAnchor(meniu, 0.0);
        top.getChildren().addAll(meniu);


        //Initializare left
        listaAngajati.setItems(angajati);
        listaAngajati.setEditable(false);
        listaAngajati.setCellFactory(param -> {
            ListCell<Angajat> celula = new CelulaAngajat();
            celula.setOnDragDetected(new DragDetectedEvent());
            return celula;
        });
        HBox leftLabelBox = new HBox();
        leftLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label leftLabel = new Label("Angajati:");
        leftLabelBox.getChildren().add(leftLabel);
        left.getChildren().addAll(leftLabelBox, listaAngajati);


        //Initializare right
        listaPosturi.setItems(posturi);
        listaPosturi.setEditable(false);
        listaPosturi.setCellFactory(param -> {
            ListCell<Post> celula = new CelulaPost();
            celula.setOnDragDetected(new DragDetectedEvent());
            return celula;
        });
        HBox rightLabelBox = new HBox();
        rightLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label rightLabel = new Label("Posturi:");
        rightLabelBox.getChildren().add(rightLabel);
        right.getChildren().addAll(rightLabelBox, listaPosturi);

        //Initializare center
        dataCol = new TableColumn("Data");
        tura1Col = new TableColumn("Tura de Noapte");
        tura2Col = new TableColumn("Tura de Zi");
        tura3Col = new TableColumn("Tura de Seara");
        creareCalendar();
        calendar.getColumns().addAll(dataCol, tura1Col, tura2Col, tura3Col);
        calendar.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox centerLabelBox = new HBox();
        centerLabelBox.setAlignment(Pos.CENTER);
        Label centerLabel = new Label("Calendar");
        Label centerLeftArrow = new Label("<");
        Label centerRightArrow = new Label(">");
        centerLabelBox.getChildren().addAll(centerLeftArrow, centerLabel, centerRightArrow);
        centerLabel.setOnDragOver(event -> {
            if (event.getGestureSource() != centerLabel &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        });
        centerLabel.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                centerLabel.setText(db.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        center.getChildren().addAll(centerLabelBox, calendar);

        //Totul vine impreuna aici
        root.setTop(top);
        root.setLeft(left);
        root.setRight(right);
        root.setCenter(center);
        //root.setBottom(bottom);



        Scene scene = new Scene(root, 300, 275);
        left.getStyleClass().addAll("vbox");
        right.getStyleClass().addAll("vbox");
        center.getStyleClass().addAll("vbox");
        leftLabelBox.getStyleClass().addAll("label_box");
        centerLabelBox.getStyleClass().addAll("label_box");
        rightLabelBox.getStyleClass().addAll("label_box");
        leftLabel.getStyleClass().addAll("label_pos");
        rightLabel.getStyleClass().addAll("label_pos");
        centerLeftArrow.getStyleClass().addAll("label_pos");
        centerLabel.getStyleClass().addAll("label_pos");
        centerRightArrow.getStyleClass().addAll("label_pos");
        scene.getStylesheets().add("main/resources/stilizare.css");
        stage.setMaximized(true);
        stage.setTitle("Manager de Tura");
        stage.setScene(scene);
        stage.show();
    }

    private void initModTure(@Nullable ActionEvent event)
    {

    }

    private void initModLuna(@Nullable ActionEvent event)
    {
        if(event != null)
        {
            //TODO seteaza regiunile la null
        }

    }

    private void creareCalendar()
    {
        YearMonth azi = YearMonth.now();
        int nrZile = azi.lengthOfMonth();
        int an = azi.getYear();
        int luna = azi.getMonthValue();
        //String luna = (azi.getMonth().getDisplayName(TextStyle.FULL, new Locale("ro"))).toUpperCase();
        for(int i = 1; i <= nrZile; i++) {
            programCurent.add(new Zi(LocalDate.of(an, luna, i)));
        }
        dataCol.setCellValueFactory(new PropertyValueFactory<Zi, String>("dataZilei"));
        tura1Col.setCellValueFactory(new PropertyValueFactory<Zi, String>("tura1"));
        tura2Col.setCellValueFactory(new PropertyValueFactory<Zi, String>("tura2"));
        tura3Col.setCellValueFactory(new PropertyValueFactory<Zi, String>("tura3"));
        calendar.setItems(programCurent);
    }

    public void rezultatDialog(String rezultat, String mod) {
        if(mod.equals(modAngajat)) {
            Angajat a = new Angajat(rezultat);
            for(Angajat i: angajati) {
                if(i.getNumeRoot().equals(rezultat))
                    return;
            }
            angajati.add(a);
            ConexiuneServerAdauga con = new ConexiuneServerAdauga(Cerere.ANGAJAT_NOU, a);
            con.setOnSucceeded(event -> System.out.println("Serviciu finalizat cu succes"));
            con.start();
        }
        else {
            Post a = new Post(rezultat);
            for (Post i : posturi) {
                if (i.getNumeRoot().equals(rezultat))
                    return;
            }
            posturi.add(a);
            ConexiuneServerAdauga con = new ConexiuneServerAdauga(Cerere.POST_NOU, a);
            con.setOnSucceeded(event -> System.out.println("Serviciu finalizat cu succes"));
            con.start();
        }
        fereastraDialog = null;
    }

    private ManagerTuraMain getApplication() { return this; }

}
