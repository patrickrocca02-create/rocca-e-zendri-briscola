package classifica;

import java.io.Serializable;

public class Giocatore implements Serializable {
    private String nome;
    private int nVittorie;

    public Giocatore(String nome, int nVittorie){
        this.nome = nome;
        this.nVittorie = nVittorie;
    }

    public void incrementaVittorie(){
        nVittorie++;
    }

    public String getNome() {
        return nome;
    }
    public int getnVittorie() {
        return nVittorie;
    }
}
