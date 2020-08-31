package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Disposizione implements Serializable {
    private ArrayList<Carta> carteInMano;
    private ArrayList<Carta> carteInTavola;
    private Carta briscola;
    private int turno;
    private boolean[][] schiene;
    private int ID;

    public Disposizione(int ID, ArrayList<Carta> carteInMano, ArrayList<Carta> cartaInTavola, Carta briscola, int turno,boolean[][] schiene){
        this.ID = ID;
        this.carteInMano = carteInMano;
        this.carteInTavola = cartaInTavola;
        this.briscola = briscola;
        this.turno = turno;
        this.schiene = schiene;
    }

    public int getID(){
        return this.ID;
    }
    public ArrayList<Carta> getCarteInMano() {
        return carteInMano;
    }
    public ArrayList<Carta> getCarteInTavola() {
        return carteInTavola;
    }
    public Carta getBriscola() {
        return briscola;
    }
    public int getTurno() {
        return turno;
    }
    public boolean[][] getSchiene(){
        return schiene;
    }

    @Override
    public String toString() {
        String s = "";
        s += "Carte in mano: ";
        for(Carta c : carteInMano){
            if(c != null)
                s += c.toString() + "; ";
            else
                s += null + "; ";
        }
        s += System.lineSeparator() + "Carte in tavola:";
        for(Carta c : carteInTavola) s += c.toString() + "; ";
        s += System.lineSeparator() + "turno: " + turno;
        return s;
    }
}
