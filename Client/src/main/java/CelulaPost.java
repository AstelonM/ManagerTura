package main.java;

import javafx.scene.control.ListCell;

public class CelulaPost extends ListCell<Post> {
    @Override
    protected void updateItem( Post item, boolean empty )
    {
        super.updateItem( item, empty );
        if(item != null)
            setText( item.getNumeRoot() );
        else
            setText("");
    }
}
