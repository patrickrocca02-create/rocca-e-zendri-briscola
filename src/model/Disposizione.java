package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Disposizione implements Serializable {
    private ArrayList<Carta> carteInMano;
    private ArrayList<Carta> cartaInTavola;
    private Carta briscola;

    public Disposizione(ArrayList<Carta> carteInMano, ArrayList<Carta> cartaInTavola, Carta briscola){
        this.carteInMano = carteInMano;
        this.cartaInTavola = cartaInTavola;
        this.briscola = briscola;
    }
}
