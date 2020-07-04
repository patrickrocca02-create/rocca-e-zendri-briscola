package model;

import javafx.scene.AccessibleAction;

import java.util.ArrayList;

public class Partita2 extends Partita{

    private int giocata;

     public Partita2(){
         super();

         giocata = 0;

         giocatori = new ArrayList<Giocatore>();
         giocatori.add(new Giocatore());
         giocatori.add(new Giocatore());

         for (Giocatore g : giocatori) {
             for (int i = 0; i < 3; i++) {
                 g.aggiungiCarta(prossimaCarta());
             }
         }

         carteInTavola = new ArrayList<Carta>();
         carteInTavola.add(null);
         carteInTavola.add(null);

         turno = (int) (Math.random()* 2);
     }

     @Override
     public void processaMossa(Carta c){
         carteInTavola.set(turno,c);

         giocatori.get(turno).rimuoviCarta(c);

         if(giocata == 1){

             server.broadcastDisposizione(true);

             Carta c1;
             Carta c2;
             if(turno == 0) {
                 c1 = carteInTavola.get(1);
                 c2 = carteInTavola.get(0);
             }else{
                 c1 = carteInTavola.get(0);
                 c2 = carteInTavola.get(1);
             }

             System.out.println("Carta 1: " + c1.toString());
             System.out.println("Carta 2: " + c2.toString());

             int winner;
             int g1 = 0;
             int g2 = turno;

             if(g2 == 0)
                 g1 = 1;

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

             turno = winner;

             giocatori.get(winner).addPunti(c1.getPunti() + c2.getPunti());

             giocata = 0;

             for (int i = 0; i < carteInTavola.size(); i++) {
                 carteInTavola.set(i,null);
             }

             for(Giocatore g : giocatori) {
                 System.out.println("Punti: " + g.getPunti());
                 if(mazzo.size() > 0) {
                     g.aggiungiCarta(prossimaCarta());
                 }else if(!mazzoFinito) {
                     g.aggiungiCarta(briscola);
                     mazzoFinito = true;
                 }
             }
             System.out.println(System.lineSeparator());
         }
         else {
             if (turno == 0)
                 turno = 1;
             else
                 turno = 0;

             giocata = 1;
         }
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
