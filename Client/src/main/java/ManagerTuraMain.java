package main.java;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import main.java.celule.CelulaAngajat;
import main.java.celule.CelulaPost;
import main.java.celule.CelulaTabel;
import main.java.dialoguri.DialogAngajati;
import main.java.dialoguri.DialogInput;
import main.java.dialoguri.DialogPosturi;
import main.java.servicii.*;
import main.java.Logger.EvenimentLogger;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Optional;


public class ManagerTuraMain extends Application {

    private Stage fereastraDialog;
    private Label status;
    private static Label centerLabel;
    private Label post;
    private static Logger logger;

    private static TableView<Zi> calendar;
    private TableColumn<Zi, String> dataCol;
    private TableColumn<Zi, String> tura1Col;
    private TableColumn<Zi, String> tura2Col;
    private TableColumn<Zi, String> tura3Col;

    private static ObservableList<Angajat> angajati = FXCollections.observableArrayList();
    private static ObservableList<Post> posturi = FXCollections.observableArrayList();
    private static ObservableList<Zi> programCurent = FXCollections.observableArrayList();
    private ListView<Angajat> listaAngajati = new ListView<>();
    private ListView<Post> listaPosturi = new ListView<>();

    private static Post postCurent;
    private static boolean adaugaTure = false;
    private static int offsetLuna;

    private static final String TEXT_TURA_1 = "Tura de Noapte";
    private static final String TEXT_TURA_2 = "Tura de Zi";
    private static final String TEXT_TURA_3 = "Tura de Seara";
    private static final String MESAJ_STATUS = "Status: ";
    private static final String MESAJ_POST = "Post: ";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Declararea componentelor principale ale scenei
        BorderPane root = new BorderPane();
        AnchorPane top = new AnchorPane();
        VBox left = new VBox(8);
        VBox right = new VBox(8);
        VBox center = new VBox(8);


        //Initializare top
        MenuBar meniu = new MenuBar();
        Menu meniuFile = new Menu("File");
        MenuItem meniuFileExit = new MenuItem("Exit");
        meniuFile.getItems().add(meniuFileExit);

        Menu meniuMod = new Menu("Mod");
        MenuItem meniuModAdaugaAngajati = new MenuItem("Adauga angajati");
        meniuModAdaugaAngajati.setOnAction(event -> {
            if(fereastraDialog == null && !posturi.isEmpty()) {
                DialogInput dialog = new DialogAngajati(getApplication(), posturi);
                fereastraDialog = dialog.start();
                fereastraDialog.showAndWait();
                fereastraDialog = null;
            }
        });
        MenuItem meniuModAdaugaPosturi = new MenuItem("Adauga posturi");
        meniuModAdaugaPosturi.setOnAction(event -> {
            if(fereastraDialog == null) {
                DialogInput dialog = new DialogPosturi(getApplication());
                fereastraDialog = dialog.start();
                fereastraDialog.showAndWait();
                fereastraDialog = null;
            }
        });
        MenuItem meniuModTure = new MenuItem("Stabileste turele");
        meniuModTure.setOnAction(event -> setStatusCurent(true));
        MenuItem meniuModLuna = new MenuItem("Afiseaza programul");
        meniuModLuna.setOnAction(event -> setStatusCurent(false));
        meniuMod.getItems().addAll(meniuModAdaugaAngajati, meniuModAdaugaPosturi, meniuModTure, meniuModLuna);

        meniu.getMenus().addAll(meniuFile, meniuMod);
        AnchorPane.setTopAnchor(meniu, 0.0);
        AnchorPane.setLeftAnchor(meniu, 0.0);
        AnchorPane.setRightAnchor(meniu, 0.0);
        top.getChildren().addAll(meniu);


        //Initializare left
        HBox leftLabelBox = new HBox();
        leftLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label leftLabel = new Label("Angajati:");
        leftLabelBox.getChildren().add(leftLabel);
        listaAngajati.setItems(angajati);
        listaAngajati.setEditable(false);
        listaAngajati.setCellFactory(param -> {
            ListCell<Angajat> celula = new CelulaAngajat();
            celula.setOnDragDetected(new DragDetectedEvent());
            return celula;
        });
        left.getChildren().addAll(leftLabelBox, listaAngajati);


        //Initializare right
        listaPosturi.setItems(posturi);
        listaPosturi.setEditable(false);
        listaPosturi.setCellFactory(param -> {
            ListCell<Post> celula = new CelulaPost();
            celula.setOnMouseClicked(new DoubleClickHandler());
            return celula;
        });
        HBox rightLabelBox = new HBox();
        rightLabelBox.setAlignment(Pos.CENTER_LEFT);
        Label rightLabel = new Label("Posturi:");
        rightLabelBox.getChildren().add(rightLabel);
        right.getChildren().addAll(rightLabelBox, listaPosturi);


        //Initializare center
        GridPane centerTop = new GridPane();
        centerTop.setId("center-top");
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(34);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);
        centerTop.getColumnConstraints().addAll(col1, col2, col3);

        HBox statusBox = new HBox(8);
        status = new Label(MESAJ_STATUS + "Vizualizare program");
        status.setId("status");
        statusBox.getChildren().add(status);
        statusBox.setId("status-box");

        HBox centerLabelBox = new HBox();
        centerLabelBox.setAlignment(Pos.CENTER);
        centerLabel = new Label("Calendar");
        Label centerLeftArrow = new Label("<");
        centerLeftArrow.setOnMouseClicked(event -> {
            offsetLuna--;
            cereCalendarul(false, false);
        });
        Label centerRightArrow = new Label(">");
        centerRightArrow.setOnMouseClicked(event -> {
            offsetLuna++;
            cereCalendarul(false, false);
        });

        HBox postBox = new HBox(8);
        post = new Label(MESAJ_POST);
        post.setId("post");
        postBox.getChildren().add(post);
        postBox.setId("post-box");

        calendar = new TableView<>();
        calendar.setEditable(true);
        calendar.setOnScroll(event -> calendar.refresh());
        calendar.setOnScrollFinished(event -> calendar.refresh());
        calendar.setOnScrollStarted(event -> calendar.refresh());
        dataCol = new TableColumn<>("Data");
        tura1Col = new TableColumn<>(TEXT_TURA_1);
        tura2Col = new TableColumn<>(TEXT_TURA_2);
        tura3Col = new TableColumn<>(TEXT_TURA_3);
        calendar.getColumns().addAll(dataCol, tura1Col, tura2Col, tura3Col);
        calendar.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(calendar, Priority.ALWAYS);

        logger = new Logger();
        centerLabelBox.getChildren().addAll(centerLeftArrow, centerLabel, centerRightArrow);
        centerTop.add(statusBox, 0, 0);
        centerTop.add(centerLabelBox, 1, 0);
        centerTop.add(postBox, 2, 0);
        center.getChildren().addAll(centerTop, calendar, logger.getLista());


        //Asamblarea scenei
        root.setTop(top);
        root.setLeft(left);
        root.setRight(right);
        root.setCenter(center);


        //Incepe lantul de cereri la baza de date
        ConexiuneServerAngajati conA = new ConexiuneServerAngajati(null, logger);
        conA.setOnSucceeded(event -> {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                    "Angajati de la baza de date!", LocalDateTime.now()));
            angajati.addAll(conA.getValue());
            listaAngajati.setItems(angajati);
            ConexiuneServerPosturi conP = new ConexiuneServerPosturi(logger);
            conP.setOnSucceeded(event1 -> {
                logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                        "Posturi de la baza de date!", LocalDateTime.now()));
                posturi.addAll(conP.getValue());
                listaPosturi.setItems(posturi);
                if (!posturi.isEmpty()) {
                    postCurent = posturi.get(0);
                    post.setText(MESAJ_POST + posturi.get(0).getNume());
                }
                cereCalendarul(true, false);
            });
            conP.start();
        });
        conA.start();


        //Crearea scenei, stilizare si afisare
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
        status.getStyleClass().addAll("label_pos");
        post.getStyleClass().addAll("label_pos");
        scene.getStylesheets().add("main/resources/stilizare.css");
        stage.setMaximized(true);
        stage.setTitle("Manager de Tura");
        stage.setScene(scene);
        stage.show();
    }

    private void cereCalendarul(boolean inceput, boolean cereAngajati) {
        YearMonth azi = YearMonth.now();
        int an = azi.getYear();
        int luna = azi.getMonthValue();
        if (luna + offsetLuna > 12) {
            luna += offsetLuna - 12;
            an++;
        } else if (luna + offsetLuna < 1) {
            luna += 12 - offsetLuna;
            an--;
        } else
            luna += offsetLuna;
        azi = YearMonth.of(an, luna);
        int nrZile = azi.lengthOfMonth();
        String textLuna = intToMonth(luna);
        if (textLuna == null)
            return;
        centerLabel.setText(textLuna + " " + an);
        if (!programCurent.isEmpty())
            programCurent.remove(0, programCurent.size());
        Date start = Date.valueOf(LocalDate.of(an, luna, 1));
        Date end = Date.valueOf(LocalDate.of(an, luna, nrZile));
        ConexiuneServerLuna conL = new ConexiuneServerLuna(start, end, postCurent, logger);
        int finalAn = an;
        int finalLuna = luna;
        conL.setOnSucceeded(event -> {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                    "Program de la baza de date!", LocalDateTime.now()));
            ArrayList<Zi> rezultat = conL.getValue();
            for (int i = 1; i <= nrZile; i++) {
                LocalDate data = LocalDate.of(finalAn, finalLuna, i);
                Optional<Zi> optional = rezultat.stream().filter(zi -> zi.getDataO().equals(data)).findFirst();
                if (optional.isPresent())
                    programCurent.add(optional.get());
                else
                    programCurent.add(new Zi(data, postCurent));
            }
            creazaCalendarul(inceput);
            if (cereAngajati) {
                ConexiuneServerAngajati conA = new ConexiuneServerAngajati(postCurent, logger);
                conA.setOnSucceeded(event1 -> {
                    if (!angajati.isEmpty())
                        angajati.remove(0, angajati.size());
                    angajati.addAll(conA.getValue());
                });
                conA.start();
            }
        });
        conL.start();
    }

    private void creazaCalendarul(boolean inceput) {
        if (inceput) {
            dataCol.setCellFactory(new TableCellFactory());
            dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
            dataCol.setSortable(false);
            tura1Col.setCellFactory(new TableCellFactory());
            tura1Col.setCellValueFactory(new PropertyValueFactory<>("tura1"));
            tura1Col.setSortable(false);
            tura2Col.setCellFactory(new TableCellFactory());
            tura2Col.setCellValueFactory(new PropertyValueFactory<>("tura2"));
            tura2Col.setSortable(false);
            tura3Col.setCellFactory(new TableCellFactory());
            tura3Col.setCellValueFactory(new PropertyValueFactory<>("tura3"));
            tura3Col.setSortable(false);
        }
        calendar.setItems(programCurent);
        calendar.refresh();
    }

    public void rezultatDialogAngajati(String numeAngajat, String numePost) {
        Post post = posturi.get(posturi.indexOf(new Post(numePost)));
        Angajat angajat = new Angajat(numeAngajat, post);
        if (angajati.contains(angajat)) {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_ATENTIE,
                    "Angajatul " + angajat.getNume() + " exista deja pe postul " + numePost + ".", LocalDateTime.now()));
            return;
        }
        angajati.add(angajat);
        ConexiuneServerAdauga con = new ConexiuneServerAdauga(Cerere.ANGAJAT_NOU, angajat, logger);
        con.start();
    }

    public void rezultatDialogPosturi(String numePost) {
        Post post = new Post(numePost);
        if (posturi.contains(post)) {
            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_ATENTIE,
                    "Postul " + post.getNume() + " exista deja.", LocalDateTime.now()));
            return;
        }
        posturi.add(post);
        ConexiuneServerAdauga con = new ConexiuneServerAdauga(Cerere.POST_NOU, post, logger);
        con.start();
    }

    private void setStatusCurent(boolean status) {
        adaugaTure = status;
        if (adaugaTure) {
            this.status.setText(MESAJ_STATUS + "Stabilire ture");
            if (posturi.isEmpty())
                return;
            postCurent = posturi.get(0);
            post.setText(MESAJ_POST + postCurent.getNume());
            ConexiuneServerAngajati con = new ConexiuneServerAngajati(postCurent, logger);
            con.setOnSucceeded(event -> {
                logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                        "Angajati de la baza de date!", LocalDateTime.now()));
                if (!angajati.isEmpty())
                    angajati.remove(0, angajati.size());
                angajati.addAll(con.getValue());
            });
            con.start();
        } else {
            this.status.setText(MESAJ_STATUS + "Vizualizare program");
            ConexiuneServerAngajati con = new ConexiuneServerAngajati(null, logger);
            con.setOnSucceeded(event -> {
                logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                        "Angajati de la baza de date!", LocalDateTime.now()));
                if (!angajati.isEmpty())
                    angajati.remove(0, angajati.size());
                angajati.addAll(con.getValue());
                offsetLuna = 0;
                cereCalendarul(false, false);
            });
            con.start();
        }
    }

    private ManagerTuraMain getApplication() { return this; }

    public static String intToMonth(int luna) {
        switch (luna) {
            case 1: {
                return "Ianuarie";
            }
            case 2: {
                return "Februarie";
            }
            case 3: {
                return "Martie";
            }
            case 4: {
                return "Aprilie";
            }
            case 5: {
                return "Mai";
            }
            case 6: {
                return "Iunie";
            }
            case 7: {
                return "Iulie";
            }
            case 8: {
                return "August";
            }
            case 9: {
                return "Septembrie";
            }
            case 10: {
                return "Octombrie";
            }
            case 11: {
                return "Noiembrie";
            }
            case 12: {
                return "Decembrie";
            }
            default: {
                return null;
            }
        }
    }


    private class DoubleClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() == 2) {
                CelulaPost celula = (CelulaPost) event.getSource();
                if (celula.getItem() == null) {
                    event.consume();
                    return;
                }
                postCurent = celula.getItem();
                post.setText(MESAJ_POST + postCurent.getNume());
                if (adaugaTure)
                    cereCalendarul(false, true);
                else
                    cereCalendarul(false, false);
                event.consume();
            }
        }
    }

    public static class TableCellFactory implements Callback<TableColumn<Zi, String>, TableCell<Zi, String>> {
        @Override
        public TableCell<Zi, String> call(TableColumn<Zi, String> param) {
            TableCell<Zi, String> celula = new CelulaTabel();
            celula.setOnDragOver(event -> {
                if (!adaugaTure || offsetLuna < 0) {
                    event.consume();
                    return;
                }
                if (event.getGestureSource() != celula && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            });
            celula.setOnDragDropped(event -> {
                if (!adaugaTure) {
                    event.setDropCompleted(true);
                    event.consume();
                    return;
                }
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    int indexAngajat = Integer.parseInt(db.getString());
                    int indexCelula = celula.getIndex();
                    int indexColoana = 0;
                    String textTura = "";
                    String textColoana = param.getText();
                    if (textColoana.equals(TEXT_TURA_1)) {
                        indexColoana = Zi.TURA1;
                        textTura = "tura de noapte";
                    }
                    if (textColoana.equals(TEXT_TURA_2)) {
                        indexColoana = Zi.TURA2;
                        textTura = "tura de zi";
                    }
                    if (textColoana.equals(TEXT_TURA_3)) {
                        indexColoana = Zi.TURA3;
                        textTura = "tura de seara";
                    }
                    Zi zi = programCurent.get(indexCelula);
                    Angajat angajat = angajati.get(indexAngajat);
                    ConexiuneServerTura con = new ConexiuneServerTura(angajat, zi, indexColoana, logger);
                    int finalIndexColoana = indexColoana;
                    String finalTextTura = textTura;
                    int finalIndexCelula = indexCelula + 1;
                    con.setOnSucceeded(event1 -> {
                        boolean gasit = con.getValue();
                        if (!gasit) {
                            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_NORMAL,
                                    "Angajatul " + angajat.getNume() + " adaugat pe " + finalTextTura +
                                    " pe data de " + finalIndexCelula + " " + centerLabel.getText(), LocalDateTime.now()));
                            zi.setAngajat(angajat, finalIndexColoana);
                            calendar.refresh();
                        } else
                            logger.adaugaEveniment(new EvenimentLogger(EvenimentLogger.GRAD_ATENTIE,
                                    "Angajatul " + angajat.getNume() + " nu poate fi adaugat pe " + finalTextTura +
                                    " pe data de " + finalIndexCelula + " " + centerLabel.getText() , LocalDateTime.now()));
                    });
                    con.start();
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            return celula;
        }
    }

    public static class DragDetectedEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (!adaugaTure) {
                event.consume();
                return;
            }
            String continut;
            Dragboard db;
            CelulaAngajat celula = (CelulaAngajat) event.getSource();
            if (celula.getItem() == null) {
                event.consume();
                return;
            }
            continut = "" + angajati.indexOf(celula.getItem());
            db = celula.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(continut);
            db.setContent(content);
            event.consume();
        }
    }
}
