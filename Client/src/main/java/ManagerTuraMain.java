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
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;


public class ManagerTuraMain extends Application {

    //private ScrollPane center;
    private VBox center;
    private TableView calendar;
    private Stage fereastraDialog;

    private ObservableList<Angajat> angajati = FXCollections.observableArrayList();
    private ObservableList<Post> posturi = FXCollections.observableArrayList();
    private ListView<Angajat> listaAngajati = new ListView<>();
    private ListView<Post> listaPosturi = new ListView<>();

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
        final ConexiuneServerAngajati conA = new ConexiuneServerAngajati(this);
        /*conA.setOnSucceeded(event -> {
            System.out.println("Event succeeded");
            angajati.addAll(conA.getValue());
            listaAngajati.setItems(angajati);
            final ConexiuneServerPosturi conP = new ConexiuneServerPosturi();
            conA.setOnSucceeded(event2 -> posturi.addAll(conP.getValue()));
            conP.start();
        });*/
        conA.start();
        final ConexiuneServerPosturi conP = new ConexiuneServerPosturi(this, conA);
        conP.start();
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
        /*listaAngajati.setCellFactory(new Callback<ListView<Angajat>, ListCell<Angajat>>() {
            @Override
                public ListCell<Angajat> call(ListView<Angajat> param) {
                ListCell<Angajat> celula = new ListCell<Angajat>()
                {
                    @Override
                    protected void updateItem( Angajat item, boolean empty )
                    {
                        super.updateItem( item, empty );
                        setText( item.getNume() );
                    }
                };
                celula.setOnDragDetected(event -> {
                    Dragboard db = celula.startDragAndDrop( TransferMode.COPY );
                    ClipboardContent content = new ClipboardContent();
                    content.putString( celula.getItem().getNume() );
                    db.setContent( content );
                    event.consume();
                });
                return celula;
            }
        });*/
        HBox leftLabelBox = new HBox();
        leftLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label leftLabel = new Label("Angajati:");
        leftLabelBox.getChildren().add(leftLabel);
        //left.setAlignment(Pos.TOP_CENTER);
        //left.setPadding(new Insets(10, 10, 10, 10));
        left.getChildren().addAll(leftLabelBox, listaAngajati);


        //Initializare right
        listaPosturi.setItems(posturi);
        listaPosturi.setEditable(false);
        /*listaPosturi.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> celula = new ListCell<String>()
                {
                    @Override
                    protected void updateItem( String item, boolean empty )
                    {
                        super.updateItem( item, empty );
                        setText( item );
                    }
                };
                celula.setOnDragDetected(event -> {
                    Dragboard db = celula.startDragAndDrop( TransferMode.COPY );
                    ClipboardContent content = new ClipboardContent();
                    content.putString( celula.getItem() );
                    db.setContent( content );
                    event.consume();
                });
                return celula;
            }
        });*/
        HBox rightLabelBox = new HBox();
        rightLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label rightLabel = new Label("Posturi:");
        rightLabelBox.getChildren().add(rightLabel);
        //right.setAlignment(Pos.TOP_CENTER);
        //right.setSpacing(8);
        //right.setPadding(new Insets(10, 10, 10, 10));
        right.getChildren().addAll(rightLabelBox, listaPosturi);

        //Initializare center
        TableColumn dataCol = new TableColumn("Data");
        TableColumn tura1Col = new TableColumn("Tura de Noapte");
        TableColumn tura2Col = new TableColumn("Tura de Zi");
        TableColumn tura3Col = new TableColumn("Tura de Seara");
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
        //center.setAlignment(Pos.TOP_CENTER);
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
        int nrZile = azi.getMonthValue();
        int an = azi.getYear();
        String luna = (azi.getMonth().getDisplayName(TextStyle.FULL, new Locale("ro"))).toUpperCase();

    }

    private ManagerTuraMain getApplication() { return this; }

    public void rezultatDialog(String rezultat, String mod) {
        if(mod.equals(modAngajat)) {
            Angajat a = new Angajat(rezultat);
            for(Angajat i: angajati) {
                if(i.getNumeRoot().equals(rezultat))
                    return;
            }
            angajati.add(a);
            new ConexiuneServerAdauga(Cerere.ANGAJAT_NOU, a).start();
        }
        else {
            Post a = new Post(rezultat);
            for (Post i : posturi) {
                if (i.toString().equals(rezultat))
                    return;
            }
            posturi.add(a);
            new ConexiuneServerAdauga(Cerere.POST_NOU, a).start();
        }
        fereastraDialog = null;
    }

    public void rezultatAngajat(ArrayList<Angajat> lista) {
        angajati.addAll(lista);
        System.out.println("Am terminat queryul pentru angajati");
        for(Angajat a: angajati)
            System.out.println(a.getNumeRoot());
    }

    public void rezultatPost(ArrayList<Post> lista) {
        posturi.addAll(lista);
    }

}
