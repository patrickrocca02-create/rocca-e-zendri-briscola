package model;

import java.util.ArrayList;

public class Partita4 extends Partita{

    private int giocata;

    public Partita4() {
        super();

        giocata = 0;

        giocatori = new ArrayList<Giocatore>();
        giocatori.add(new Giocatore());
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
        carteInTavola.add(null);

        turno = (int) (Math.random() * 4);
    }

    @Override
    public void processaMossa(Carta c) {
        carteInTavola.set(turno,c);

        giocatori.get(turno).rimuoviCarta(c);

        if(giocata == 3){

            int temp = turno;
            turno = -1;
            server.broadcastDisposizione();
            turno = temp;

            int[] indiciGiocatori = getIndiciGiocatoriTurno();
            int g1 = indiciGiocatori[0], g2= indiciGiocatori[1], g3= indiciGiocatori[2], g4 = indiciGiocatori[3];

            int vincitore;
            vincitore = vincitore(carteInTavola.get(g1),g1,carteInTavola.get(g2),g2);
            vincitore = vincitore(carteInTavola.get(vincitore),vincitore,carteInTavola.get(g3),g3);
            vincitore = vincitore(carteInTavola.get(vincitore),vincitore,carteInTavola.get(g4),g4);

            turno = vincitore;

            for (Carta carta : carteInTavola) {
                giocatori.get(vincitore).addPunti(carta.getPunti());
            }

            giocata = 0;

            clearCarteInTavola();

            pescaCarte();

        } else{
            if(turno != 3)
                turno++;
            else
                turno = 0;

            giocata++;
        }

    }

    @Override
    public int[] getIndiciGiocatoriSuccessivi(int giocatore) {
        int[] indiciGiocatori = new int[4];

        switch(giocatore){
            case 0:
                indiciGiocatori[0] = 0;
                indiciGiocatori[1] = 1;
                indiciGiocatori[2] = 2;
                indiciGiocatori[3] = 3;
                break;
            case 1:
                indiciGiocatori[0] = 1;
                indiciGiocatori[1] = 2;
                indiciGiocatori[2] = 3;
                indiciGiocatori[3] = 0;
                break;
            case 2:
                indiciGiocatori[0] = 2;
                indiciGiocatori[1] = 3;
                indiciGiocatori[2] = 0;
                indiciGiocatori[3] = 1;
                break;
            case 3:
                indiciGiocatori[0] = 3;
                indiciGiocatori[1] = 0;
                indiciGiocatori[2] = 1;
                indiciGiocatori[3] = 2;
                break;
        }

        return indiciGiocatori;
    }

    @Override
    public int[] getIndiciGiocatoriTurno(){
        int[] indiciGiocatori = new int[4];

        switch(turno){
            case 0:
                indiciGiocatori[0] = 1;
                indiciGiocatori[1] = 2;
                indiciGiocatori[2] = 3;
                indiciGiocatori[3] = 0;
                break;
            case 1:
                indiciGiocatori[0] = 2;
                indiciGiocatori[1] = 3;
                indiciGiocatori[2] = 0;
                indiciGiocatori[3] = 1;
                break;
            case 2:
                indiciGiocatori[0] = 3;
                indiciGiocatori[1] = 0;
                indiciGiocatori[2] = 1;
                indiciGiocatori[3] = 2;
                break;
            case 3:
                indiciGiocatori[0] = 0;
                indiciGiocatori[1] = 1;
                indiciGiocatori[2] = 2;
                indiciGiocatori[3] = 3;
                break;
        }

        return indiciGiocatori;
    }

    public int[] getVincitori(){
        int[] vincitori = new int[4];

        if(giocatori.get(0).getPunti() + giocatori.get(2).getPunti() > 60) {
            vincitori[0] = 0;
            vincitori[1] = 1;
            vincitori[2] = 0;
            vincitori[3] = 1;
        }
        else if(giocatori.get(1).getPunti() + giocatori.get(3).getPunti() > 60){
            vincitori[0] = 1;
            vincitori[1] = 0;
            vincitori[2] = 1;
            vincitori[3] = 0;
        }
        else{
            vincitori[0] = 2;
            vincitori[1] = 2;
            vincitori[2] = 2;
            vincitori[3] = 2;
        }

        return vincitori;
    }
}
