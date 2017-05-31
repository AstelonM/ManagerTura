package main.java;

import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;


public class ManagerTuraMain extends Application {

    //private ScrollPane center;
    private VBox center;
    private TableView calendar;

    static final ObservableList<String> test = FXCollections.observableArrayList();
    private ListView<String> listaAngajati = new ListView<>();
    private ListView<String> listaPosturi = new ListView<>();

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

        //Initializare top
        MenuBar meniu = new MenuBar();
        Menu meniuFile = new Menu("File");
        MenuItem meniuFileExit = new MenuItem("Exit");
        meniuFile.getItems().add(meniuFileExit);
        Menu meniuMod = new Menu("Mod");
        MenuItem meniuModAdd = new MenuItem("Adauga angajati/posturi");
        meniuModAdd.setId("add");
        meniuModAdd.setOnAction(this::initModAdd);
        MenuItem meniuModTure = new MenuItem("Stabileste turele");
        meniuModTure.setId("ture");
        meniuModTure.setOnAction(this::initModTure);
        MenuItem meniuModLuna = new MenuItem("Afiseaza luna curenta");
        meniuModLuna.setId("luna");
        meniuModLuna.setOnAction(this::initModLuna);
        meniuMod.getItems().addAll(meniuModAdd, meniuModTure, meniuModLuna);
        meniu.getMenus().addAll(meniuFile, meniuMod);
        AnchorPane.setTopAnchor(meniu, 0.0);
        AnchorPane.setLeftAnchor(meniu, 0.0);
        AnchorPane.setRightAnchor(meniu, 0.0);
        top.getChildren().addAll(meniu);

        //Initializeaza layout-ul de afisare a lunii curente
        //initModLuna(null);

        //Initializare left
        test.addAll("Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9", "Test10");
        listaAngajati.setItems(test);
        listaAngajati.setEditable(false);
        listaAngajati.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
        });
        HBox leftLabelBox = new HBox();
        leftLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label leftLabel = new Label("Angajati:");
        leftLabelBox.getChildren().add(leftLabel);
        left.setAlignment(Pos.TOP_CENTER);
        //left.setPadding(new Insets(10, 10, 10, 10));
        left.getChildren().addAll(leftLabelBox, listaAngajati);


        //Initializare right
        //test.addAll("Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9", "Test10");
        listaPosturi.setItems(test);
        listaPosturi.setEditable(false);
        listaPosturi.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
        });
        HBox rightLabelBox = new HBox();
        rightLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label rightLabel = new Label("Posturi:");
        rightLabelBox.getChildren().add(rightLabel);
        right.setAlignment(Pos.TOP_CENTER);
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
        Label centerLabel = new Label("Calendar:");
        centerLabelBox.getChildren().addAll(centerLabel);
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
        center.setAlignment(Pos.TOP_CENTER);
        //center.setSpacing(8);
        //center.setPadding(new Insets(10, 10, 10, 10));
        center.getChildren().addAll(centerLabelBox, calendar);

        //BorderPane.setAlignment(center, Pos.TOP_CENTER);
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
        leftLabel.getStyleClass().addAll("label_pos");
        rightLabel.getStyleClass().addAll("label_pos");
        centerLabel.getStyleClass().addAll("label_pos");
        scene.getStylesheets().add("main/resources/stilizare.css");

        stage.setTitle("Manager de Tura");
        stage.setScene(scene);
        stage.show();
    }

    private void initModAdd(@Nullable ActionEvent event)
    {

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

}
