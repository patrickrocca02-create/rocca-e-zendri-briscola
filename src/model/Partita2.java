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

         turno = (int) (Math.random() * 2);
     }

    @Override
    public int[] getIndiciGiocatoriSuccessivi(int giocatore) {
        int[] indiciGiocatori = new int[2];
        if(giocatore == 0) {
            indiciGiocatori[0] = 0;
            indiciGiocatori[1] = 1;
        } else{
            indiciGiocatori[0] = 1;
            indiciGiocatori[1] = 0;
        }
        return indiciGiocatori;
    }

     @Override
     public void processaMossa(Carta c){
         carteInTavola.set(turno,c);

         giocatori.get(turno).rimuoviCarta(c);

         if(giocata == 1){

             server.broadcastDisposizione();

             int g1,g2;

             if(turno == 0) {
                 g1 = 1;
                 g2 = 0;
             }else{
                 g1 = 0;
                 g2 = 1;
             }

             Carta c1 = carteInTavola.get(g1);
             Carta c2 = carteInTavola.get(g2);

             turno = vincitore(c1,g1,c2,g2);

             giocatori.get(turno).addPunti(c1.getPunti() + c2.getPunti());

             giocata = 0;

             clearCarteInTavola();

             pescaCarte();
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
     public int[] getIndiciGiocatoriTurno(){
         int[] indiciGiocatori = new int[2];

         if(turno == 0) {
             indiciGiocatori[0] = 1;
             indiciGiocatori[1] = 0;
         }else{
             indiciGiocatori[0] = 0;
             indiciGiocatori[1] = 1;
         }

         return indiciGiocatori;
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
