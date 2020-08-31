package model;

import java.util.ArrayList;

public class Partita3 extends Partita{
    private int giocata;

    public Partita3() {
        super();

        int seme = 1;
        int briscola = getBriscola().getSeme();
        while(seme == briscola){
            seme = (int) (Math.random()*4) + 1;
        }

        mazzo.remove(new Carta(seme,2));

        giocata = 0;

        giocatori = new ArrayList<Giocatore>();
        giocatori.add(new Giocatore());
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
        carteInTavola.add(null);

        turno = (int) (Math.random() * 3);
    }

    @Override
    public void processaMossa(Carta c) {
        carteInTavola.set(turno,c);

        System.out.println(c);
        System.out.println(turno + System.lineSeparator());
        giocatori.get(turno).rimuoviCarta(c);

        if(giocata == 2){

            server.broadcastDisposizione();

            int[] indiciGiocatori = getIndiciGiocatoriTurno();
            int g1 = indiciGiocatori[0], g2= indiciGiocatori[1], g3= indiciGiocatori[2];

            int vincitore;
            vincitore = vincitore(carteInTavola.get(g1),g1,carteInTavola.get(g2),g2);
            vincitore = vincitore(carteInTavola.get(vincitore),vincitore,carteInTavola.get(g3),g3);

            turno = vincitore;

            for (Carta carta : carteInTavola) {
                giocatori.get(vincitore).addPunti(carta.getPunti());
            }

            giocata = 0;

            clearCarteInTavola();
            pescaCarte();

        } else{
            if(turno != 2)
                turno++;
            else
                turno = 0;

            giocata++;
        }

    }

    @Override
    public int[] getIndiciGiocatoriSuccessivi(int giocatore) {
        int[] indiciGiocatori = new int[3];

        switch(giocatore) {
            case 0:
                indiciGiocatori[0] = 0;
                indiciGiocatori[1] = 1;
                indiciGiocatori[2] = 2;
                break;
            case 1:
                indiciGiocatori[0] = 1;
                indiciGiocatori[1] = 2;
                indiciGiocatori[2] = 0;
                break;
            case 2:
                indiciGiocatori[0] = 2;
                indiciGiocatori[1] = 0;
                indiciGiocatori[2] = 1;
                break;
        }

        return indiciGiocatori;
    }

    @Override
    public int[] getIndiciGiocatoriTurno(){
        int[] indiciGiocatori = new int[3];

        switch(turno){
            case 0:
                indiciGiocatori[0] = 1;
                indiciGiocatori[1] = 2;
                indiciGiocatori[2] = 0;
                break;
            case 1:
                indiciGiocatori[0] = 2;
                indiciGiocatori[1] = 0;
                indiciGiocatori[2] = 1;
                break;
            case 2:
                indiciGiocatori[0] = 0;
                indiciGiocatori[1] = 1;
                indiciGiocatori[2] = 2;
                break;
        }

        return indiciGiocatori;
    }

    public int[] getVincitori(){
        int[] vincitori = new int[3];

        int p1 = giocatori.get(0).getPunti(), p2 = giocatori.get(1).getPunti(), p3 = giocatori.get(2).getPunti();

        if(p1 > p2 && p1 > p3) {
            vincitori[0] = 0;
            vincitori[1] = 1;
            vincitori[2] = 1;
        }
        else if(p2 > p1 && p2 > p3){
            vincitori[0] = 1;
            vincitori[1] = 0;
            vincitori[2] = 1;
        }
        else if(p3 > p1 && p3 > p2) {
            vincitori[0] = 1;
            vincitori[1] = 1;
            vincitori[2] = 0;
        }
        else if(p1 == p2 && p3 < p1){
            vincitori[0] = 2;
            vincitori[1] = 2;
            vincitori[2] = 1;
        }
        else if(p1 == p3 && p2 < p1){
            vincitori[0] = 2;
            vincitori[1] = 1;
            vincitori[2] = 2;
        }
        else if(p2 == p3 && p1 < p2){
            vincitori[0] = 1;
            vincitori[1] = 2;
            vincitori[2] = 2;
        }
        else{
            vincitori[0] = 2;
            vincitori[1] = 2;
            vincitori[2] = 2;
        }

        return vincitori;
    }
}
