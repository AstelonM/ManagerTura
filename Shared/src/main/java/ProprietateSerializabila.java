package main.java;

/**
 * Interfata care defineste o metoda unica pentru clasele cu campuri Property care nu pot fi serializate
 */
public interface ProprietateSerializabila {

    /**
     * Implementarea metodei trebuie sa updateze campurile Property
     */
    void updateazaProprietatile();
}
