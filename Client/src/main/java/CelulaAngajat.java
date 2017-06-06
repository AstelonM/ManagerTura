package main.java;

import javafx.scene.control.ListCell;

public class CelulaAngajat extends ListCell<Angajat> {
    @Override
    protected void updateItem( Angajat item, boolean empty )
    {
        super.updateItem( item, empty );
        if(item != null)
            setText( item.getNumeRoot() );
        else
            setText("");
    }
}
