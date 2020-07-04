package model;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Partita {

    protected ArrayList<Giocatore> giocatori;
    protected ArrayList<Carta> mazzo;
    protected Carta briscola;
    protected ArrayList<Carta> carteInTavola;
    protected int turno;
    protected Server server;
    protected boolean mazzoFinito;

    public Partita(){
        mazzoFinito = false;
        inizializzaMazzo();
        briscola = prossimaCarta();
    }

    protected void inizializzaMazzo(){
        mazzo = new ArrayList<Carta>();
        for (int seme = 1; seme <= 4; seme++) {
            for (int numero = 1; numero <= 10; numero++) {
                mazzo.add(new Carta(seme, numero));
            }
        }
        Collections.shuffle(mazzo);
    }

    protected void distribuisciCarta(int idGiocatore){
        giocatori.get(idGiocatore).aggiungiCarta(prossimaCarta());

    }
    public void setServer(Server server){
        this.server = server;
    }

    protected Carta prossimaCarta(){
        Carta c = mazzo.get(0);
        mazzo.remove(0);
        return c;
    }

    public int getNGiocatori(){
        return giocatori.size();
    }
    public int getTurno(){
        return this.turno;
    }
    public ArrayList<Giocatore> getGiocatori(){
        return this.giocatori;
    }
    public Carta getBriscola() {
        return briscola;
    }
    public ArrayList<Carta> getCarteInTavola() {
        return carteInTavola;
    }
    public boolean isMazzoFinito() {
        return mazzoFinito;
    }

    public abstract void processaMossa(Carta c);
    public abstract int[] getVincitori();
}
