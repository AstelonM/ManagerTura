package main.java;

import javafx.beans.property.SimpleStringProperty;

public class Post extends DataRoot implements ProprietateSerializabila {

    private transient SimpleStringProperty nume;

    public Post(String nume) {
        super(nume);
        this.nume = new SimpleStringProperty(numeRoot);
    }

    @Override
    public void updateazaProprietatile() {
        if(nume == null)
            nume = new SimpleStringProperty(numeRoot);
        else
            nume.set(numeRoot);
    }

    @Override
    public String toString() {
        return numeRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return numeRoot.equals(post.numeRoot);
    }

    @Override
    public int hashCode() {
        return numeRoot.hashCode();
    }
}
