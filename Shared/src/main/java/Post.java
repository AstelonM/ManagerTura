package main.java;

import java.io.Serializable;

public class Post implements Serializable {

    private String nume;

    public Post(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return nume.equals(post.nume);
    }

    @Override
    public int hashCode() {
        return nume.hashCode();
    }

    public String getNume() {
        return nume;
    }
}
