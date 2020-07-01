package model;

import javafx.scene.AccessibleAction;

import java.util.ArrayList;

public class Partita2 extends Partita{
     public Partita2(){
         super();

         giocatori = new ArrayList<Giocatore>();
         giocatori.add(new Giocatore());
         giocatori.add(new Giocatore());
         for (Giocatore g : giocatori) {
             for (int i = 0; i < 3; i++) {
                 g.aggiungiCarta(prossimaCarta());
             }
         }

         carteInTavola = new ArrayList<Carta>();

         turno = (int) Math.random()*2;
     }

     @Override
     public void processaMossa(Carta c){
         carteInTavola.add(c);

         giocatori.get(turno).rimuoviCarta(c);

         if(carteInTavola.size() == 2){
             int winner = 0;
             Carta c1 = carteInTavola.get(0);
             Carta c2 = carteInTavola.get(1);

             if(c1.getSeme() == c2.getSeme()){

                 if(c1.getNumero() > c2.getNumero())
                     winner = 0;
                 else
                     winner = 1;

                 switch(c1.getNumero()) {
                     case 1: winner = 0; break;
                     case 3:
                         if(c2.getNumero() != 1)
                             winner = 0;
                         break;
                 }

                 switch(c2.getNumero()) {
                     case 1: winner = 1; break;
                     case 3:
                         if(c1.getNumero() != 1)
                             winner = 1;
                         break;
                 }

             } else if (c1.getSeme() == briscola.getSeme())
                 winner = 0;
             else if (c2.getSeme() == briscola.getSeme())
                 winner = 1;
             else
                 winner = 0;

             giocatori.get(winner).addPunti(c1.getPunti() + c2.getPunti());
             carteInTavola.clear();

             for(Giocatore g : giocatori)
                 g.aggiungiCarta(prossimaCarta());
         }

         if(turno == 1)
             turno = 0;
         else
             turno = 1;
     }

     @Override
    public int[] getVincitori(){
         int[] vincitori = new int[2];

         if(giocatori.get(0).getPunti() > 60) {
             vincitori[0] = 0;
             vincitori[1] = 1;
         }
         else if(giocatori.get(1).getPunti() > 60){
             vincitori[0] = 1;
             vincitori[1] = 0;
         }
         else{
             vincitori[0] = 2;
             vincitori[1] = 2;
         }

         return vincitori;
     }
}
