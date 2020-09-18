package test;

import classifica.Classifica;
import classifica.Giocatore;

public class Test {
    public static void main(String[] args){
        Classifica.load();
        Classifica.reset();

        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Patrick");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Giulio");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Rupert");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Nicola");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Andrea");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Mattia");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Alessandro");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Manuel");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Giada");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Linda");
        }
        for (int i = 0; i < 100; i++) {
            Classifica.addGiocatore("Arturo");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Simone");
        }
        for (int i = 0; i < 3; i++) {
            Classifica.addGiocatore("Ennio");
        }

        for(Giocatore g : Classifica.getAttuale().getGiocatori())
            System.out.println(g.getNome() + " " + g.getnVittorie());
    }
}
