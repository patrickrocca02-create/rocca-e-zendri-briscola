package model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Giocatore {
    private ArrayList<Carta> carteInMano;
    private int punti;

    public Giocatore(){
        this.carteInMano = new ArrayList<Carta>();
        this.punti = 0;
    }

    public void rimuoviCarta(Carta c){
        carteInMano.remove(c);
    }
    public void aggiungiCarta(Carta c){
        carteInMano.add(c);
    }

    public void addPunti(int p) {
        this.punti += p;
    }
    public int getPunti() {
        return punti;
    }

    public ArrayList<Carta> getCarteInMano() {
        return carteInMano;
    }
}
