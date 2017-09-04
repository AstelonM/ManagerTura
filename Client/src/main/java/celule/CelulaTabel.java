package main.java.celule;

import javafx.scene.control.TableCell;
import main.java.Zi;

public class CelulaTabel extends TableCell<Zi, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
            getStyleClass().remove("bold-text");
        }
        else
            setText(item);
        if (getText() != null) {
            if (getText().equals("Adauga"))
                getStyleClass().add("bold-text");
            else
                getStyleClass().remove("bold-text");
        }
    }
}
