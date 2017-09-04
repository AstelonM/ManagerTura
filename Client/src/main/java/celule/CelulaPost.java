package main.java.celule;

import javafx.scene.control.ListCell;
import main.java.Post;

public class CelulaPost extends ListCell<Post> {

    @Override
    protected void updateItem( Post item, boolean empty ) {
        super.updateItem( item, empty );
        if(item != null)
            setText( item.getNume() );
        else
            setText("");
    }
}
