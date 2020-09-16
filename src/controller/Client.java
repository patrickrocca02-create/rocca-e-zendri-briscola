package controller;

import model.Carta;
import model.Disposizione;
import view.View;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{

    private View view;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean turno;

    public Client(InetAddress ipServer, int porta){
        Socket client = null;
        try{
            client = new Socket(ipServer,porta);
        }catch (Exception e){ e.printStackTrace(); }
        try {
            out = new ObjectOutputStream(client.getOutputStream());
        }catch (Exception e){ e.printStackTrace(); }
        try {
            in = new ObjectInputStream(client.getInputStream());
        }catch (Exception e){ e.printStackTrace(); }
    }

    public void inviaCarta(int indice){
        try{
            //System.out.println("Inviata la carta: " + view.getCarta(indice).getCarta());
            out.writeObject(view.getCarta(indice).getCarta());
            out.flush();
        }catch (Exception e) { e.printStackTrace(); }
    }

    private void finePartita(int[] esito) {
        view.finePartita(esito[0], esito[1]);
    }

    public boolean isTurno(){
        return this.turno;
    }

    @Override
    public void run() {

        boolean fine = false;

        while (!fine) {
            Object inObj = null;
            try {
                inObj = in.readObject();
            } catch (Exception e) { e.printStackTrace(); }

            if (inObj instanceof Disposizione) {
                //ricevuta una disposizione
                Disposizione d = (Disposizione) inObj;
                this.turno = d.getTurno() == d.getID();
                while(view == null)
                    System.out.print("");
                view.aggiornaView(d);
                try {
                    Thread.sleep(1000);
                }catch (Exception e){}
            } else if (inObj instanceof Integer){
                //ricevuto il numero di giocatori in fase di inizializzazione
                ApplicationMain.nGiocatori = (int) (Integer) inObj;
            }else if(inObj instanceof Carta[]){
                //ricevute le carte del compagno a fine partita
                view.mostraCarteCompagno((Carta[]) inObj);
            }
            else{
                //fine partita
                finePartita((int[]) inObj);
                fine = true;
            }
        }
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public void setView(View view){
        this.view = view;
    }
}