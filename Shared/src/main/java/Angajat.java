package main.java;

import java.io.Serializable;

public class Angajat implements Serializable {

    private String nume;
    private Post post;

    public Angajat(String nume, Post post) {
        this.nume = nume;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angajat angajat = (Angajat) o;

        if (!nume.equals(angajat.nume)) return false;
        return post.equals(angajat.post);
    }

    @Override
    public int hashCode() {
        int result = nume.hashCode();
        result = 31 * result + post.hashCode();
        return result;
    }

    public String getNume() {
        return nume;
    }

    public Post getPost() {
        return post;
    }
}
