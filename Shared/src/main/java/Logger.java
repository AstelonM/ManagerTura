package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.time.LocalDateTime;

public class Logger {

    private ListView<EvenimentLogger> lista;

    public Logger() {
        ObservableList<EvenimentLogger> listaInterna = FXCollections.observableArrayList();
        lista = new ListView<>(listaInterna);
        lista.setCellFactory(param -> new CelulaLogger());
    }

    public ListView<EvenimentLogger> getLista() { return lista; }

    public synchronized void adaugaEveniment(EvenimentLogger ev) {
        lista.getItems().add(ev);
    }

    public static class EvenimentLogger {
        private int grad;
        private String text;
        private LocalDateTime timp;

        public static final int GRAD_NORMAL = 1;
        public static final int GRAD_EROARE = -1;
        public static final int GRAD_ATENTIE = 100;

        public EvenimentLogger(int grad, String text, LocalDateTime timp) {
            this.grad = grad;
            this.text = text;
            this.timp = timp;
        }

        public int getGrad() { return grad; }

        public String getText() { return text; }

        public LocalDateTime getTimp() { return timp; }

        @Override
        public String toString() {
            String textGrad;
            switch (grad) {
                case GRAD_NORMAL: {
                    textGrad = "Eveniment: ";
                    break;
                }
                case GRAD_EROARE: {
                    textGrad = "Eroare: ";
                    break;
                }
                case GRAD_ATENTIE: {
                    textGrad = "Atentie: ";
                    break;
                }
                default: {
                    return "Grad incorect!";
                }
            }
            return "[" + timp.toString() + "] " + textGrad + text;
        }
    }

    public static class CelulaLogger extends ListCell<EvenimentLogger> {

        @Override
        protected void updateItem(EvenimentLogger item, boolean empty ) {
            super.updateItem(item, empty);
            if(item != null) {
                setText(item.toString());
                //TODO sa ma asigur ca merge

                if (item.getGrad() == EvenimentLogger.GRAD_EROARE)
                    getStyleClass().add("text-eroare");
                else
                    getStyleClass().remove("text-eroare");
                if (item.getGrad() == EvenimentLogger.GRAD_ATENTIE)
                    getStyleClass().add("text-atentie");
                else
                    getStyleClass().remove("text-atentie");
            } else {
                setText("");
                getStyleClass().remove("text-eroare");
                getStyleClass().remove("text-atentie");
            }
        }
    }
}
