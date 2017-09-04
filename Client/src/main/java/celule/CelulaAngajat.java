package main.java.celule;

import javafx.scene.control.ListCell;
import main.java.Angajat;

public class CelulaAngajat extends ListCell<Angajat> {

    @Override
    protected void updateItem( Angajat item, boolean empty ) {
        super.updateItem( item, empty );
        if(item != null)
            setText( item.getNume() + " - " + item.getPost().getNume() );
        else
            setText("");
    }
}
