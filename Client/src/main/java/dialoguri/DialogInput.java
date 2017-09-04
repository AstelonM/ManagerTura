package main.java.dialoguri;


import javafx.stage.Stage;
import main.java.ManagerTuraMain;

public abstract class DialogInput {

    protected ManagerTuraMain aplicatie;

    public DialogInput(ManagerTuraMain aplicatie) { this.aplicatie = aplicatie; }

    public abstract Stage start();
}
