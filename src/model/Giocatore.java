package model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Giocatore {
    private ArrayList<Carta> carteInMano;
    private int punti;
    private String nome;

    public Giocatore(){
        this.carteInMano = new ArrayList<Carta>();
        for (int i = 0; i < 3; i++)
            carteInMano.add(null);

        this.punti = 0;
    }

    public void rimuoviCarta(Carta c){
        carteInMano.set(carteInMano.indexOf(c),null);
    }
    public void aggiungiCarta(Carta c){
        carteInMano.set(carteInMano.indexOf(null),c);
    }

    public boolean[] getListaSchiene(){
        boolean[] lista = new boolean[3];
        int i = 0;
        for(Carta c : carteInMano) {
            if (c == null)
                lista[i++] = false;
            else
                lista[i++] = true;
        }
        return lista;
    }

    public void addPunti(int p) {
        this.punti += p;
    }
    public int getPunti() {
        return punti;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return this.nome;
    }

    public ArrayList<Carta> getCarteInMano() {
        return carteInMano;
    }
}
