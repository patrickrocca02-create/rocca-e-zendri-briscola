package model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.Serializable;

public class Carta implements Serializable {
    // 1 SPADE, 2 COPPE, 3 DENARI, 4 BASTONI
    private int seme;
    private int numero;
    private int punti;

    public Carta(int seme, int numero) {
        this.seme = seme;
        this.numero = numero;
        switch(numero){
            case 1: this.punti = 11; break;
            case 3: this.punti = 10; break;
            case 8: this.punti = 2; break;
            case 9: this.punti = 3; break;
            case 10: this.punti = 4;break;
            default: this.punti = 0;
        }
    }

    public int getPunti(){
        return this.punti;
    }

    public int getSeme() {
        return seme;
    }
    public int getNumero() {
        return numero;
    }

    public Image getImage(){
        try {
            return new Image(new FileInputStream("src/carte/" + this.getSeme() + " " + this.getNumero() + ".png"));
        }catch (Exception e){}
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Carta c = (Carta) obj;
        return c.getSeme() == this.seme && c.getNumero() == this.numero;
    }

    public String toString(){
        String strSeme = "spade";
        switch (seme){
            case 2: strSeme= "coppe"; break;
            case 3: strSeme= "denari"; break;
            case 4: strSeme= "bastoni"; break;
        }
        return "Seme: " +  strSeme + ", Numero: " + this.numero;
    }
}
