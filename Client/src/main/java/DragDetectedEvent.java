package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class DragDetectedEvent implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        Object sursa = event.getSource();
        if(sursa instanceof CelulaAngajat) {
            CelulaAngajat celula = (CelulaAngajat) sursa;
            if(celula.getItem() == null) {
                event.consume();
                return;
            }
            Dragboard db = celula.startDragAndDrop( TransferMode.COPY );
            ClipboardContent content = new ClipboardContent();
            content.putString( celula.getItem().getNumeRoot() );
            db.setContent( content );
            event.consume();
        } else {
            CelulaPost celula = (CelulaPost) sursa;
            if(celula.getItem() == null) {
                event.consume();
                return;
            }
            Dragboard db = celula.startDragAndDrop( TransferMode.COPY );
            ClipboardContent content = new ClipboardContent();
            content.putString( celula.getItem().getNumeRoot() );
            db.setContent( content );
            event.consume();
        }
    }
}
