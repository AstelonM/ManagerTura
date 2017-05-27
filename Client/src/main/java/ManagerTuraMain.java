package main.java;

import com.sun.istack.internal.Nullable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;


public class ManagerTuraMain extends Application {

    private ScrollPane center;
    private VBox center2;
    private TableView calendar;
    public static final ObservableList<String> test = FXCollections.observableArrayList();
    private ListView<String> testLista = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/main/resources/manager_tura.fxml"));
        BorderPane root = new BorderPane();
        AnchorPane top = new AnchorPane();
        //bottom
        VBox left = new VBox();
        //right
        center = new ScrollPane();
        center2 = new VBox();
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
        initModLuna(null);
        //Initializare bottom

        //Initializare left
        left.setAlignment(Pos.CENTER);
        left.setSpacing(5);
        left.setPadding(new Insets(10, 10, 10, 10));
        left.getStyleClass().add("vbox");
        test.addAll("Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9", "Test10");
        testLista.setItems(test);
        left.getChildren().add(testLista);
        testLista.setEditable(false);
        testLista.setCellFactory(ComboBoxListCell.forListView(test));


        //Initializare right

        //Initializare center
        center2 = new VBox();
        center2.setAlignment(Pos.CENTER);
        center2.setSpacing(5);
        center2.setPadding(new Insets(10, 10, 10, 10));
        center2.getStyleClass().add("vbox");
        TableColumn dataCol = new TableColumn("Data");
        TableColumn tura1Col = new TableColumn("Tura de Noapte");
        TableColumn tura2Col = new TableColumn("Tura de Zi");
        TableColumn tura3Col = new TableColumn("Tura de Seara");
        calendar.getColumns().addAll(dataCol, tura1Col, tura2Col, tura3Col);
        center2.getChildren().addAll(calendar);
        center.setContent(center2);
        center.getStyleClass().add("scroll-pane");
        BorderPane.setAlignment(center, Pos.CENTER);

        //Totul vine impreuna aici
        root.setTop(top);
        //root.setBottom(bottom);
        root.setLeft(left);
        //root.setRight(right);
        root.setCenter(center);

        Scene scene = new Scene(root, 300, 275);
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
