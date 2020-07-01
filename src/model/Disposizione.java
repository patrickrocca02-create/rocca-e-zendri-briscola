package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Disposizione implements Serializable {
    private ArrayList<Carta> carteInMano;
    private ArrayList<Carta> cartaInTavola;
    private Carta briscola;
    private boolean turno;

    public Disposizione(ArrayList<Carta> carteInMano, ArrayList<Carta> cartaInTavola, Carta briscola, boolean turno){
        this.carteInMano = carteInMano;
        this.cartaInTavola = cartaInTavola;
        this.briscola = briscola;
        this.turno = turno;
    }

    public ArrayList<Carta> getCarteInMano() {
        return carteInMano;
    }
    public ArrayList<Carta> getCartaInTavola() {
        return cartaInTavola;
    }
    public Carta getBriscola() {
        return briscola;
    }
    public boolean isTurno() {
        return turno;
    }
}
