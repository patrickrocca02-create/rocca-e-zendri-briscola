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

    protected int vincitore(Carta c1, int idG1, Carta c2, int idG2){
        int winner;
        int g1 = idG1;
        int g2 = idG2;

        if(c1.getSeme() == c2.getSeme()){

            if(c1.getNumero() > c2.getNumero())
                winner = g1;
            else
                winner = g2;

            switch(c1.getNumero()) {
                case 1: winner = g1; break;
                case 3:
                    if(c2.getNumero() != 1)
                        winner = g1;
                    break;
            }

            switch(c2.getNumero()) {
                case 1: winner = g2; break;
                case 3:
                    if(c1.getNumero() != 1)
                        winner = g2;
                    break;
            }

        } else if (c1.getSeme() == briscola.getSeme())
            winner = g1;
        else if (c2.getSeme() == briscola.getSeme())
            winner = g2;
        else
            winner = g1;

        return winner;
    }
    protected void clearCarteInTavola(){
        for (int i = 0; i < carteInTavola.size(); i++) {
            carteInTavola.set(i,null);
        }
    }
    protected void pescaCarte(){
        for(int i : getIndiciGiocatoriSuccessivi(turno)) {
            if(mazzo.size() > 0) {
                giocatori.get(i).aggiungiCarta(prossimaCarta());
            }else if(!mazzoFinito) {
                giocatori.get(i).aggiungiCarta(briscola);
                mazzoFinito = true;
            }
        }
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

    public abstract int[] getIndiciGiocatoriTurno();
    public abstract int[] getIndiciGiocatoriSuccessivi(int giocatore);
    public abstract void processaMossa(Carta c);
    public abstract int[][] getVincitori();
}
