package controller;

import classifica.Classifica;
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
    private String nome;

    public Client(InetAddress ipServer, int porta, String nome) throws Exception{
        Socket client = null;
        System.out.println("Tentativo di connessione...");
        client = new Socket(ipServer,porta);

        try {
            out = new ObjectOutputStream(client.getOutputStream());
        }catch (Exception e){ e.printStackTrace(); }
        try {
            in = new ObjectInputStream(client.getInputStream());
        }catch (Exception e){ e.printStackTrace(); }
        this.nome = nome;
    }

    public void inviaCarta(int indice){
        try{
            //System.out.println("Inviata la carta: " + view.getCarta(indice).getCarta());
            out.writeObject(view.getCarta(indice).getCarta());
            out.flush();
        }catch (Exception e) { e.printStackTrace(); }
    }

    private void inviaNome(){
        try{
            out.writeObject(nome);
            out.flush();
        }catch (Exception e){ e.printStackTrace(); }
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
                view.aggiornaView(d);
                try {
                    Thread.sleep(1000);
                }catch (Exception e){}
            } else if (inObj instanceof Integer){
                //ricevuto il numero di giocatori in fase di inizializzazione
                ApplicationMain.nGiocatori = (int) (Integer) inObj;
                inviaNome();
            }else if(inObj instanceof Carta[]){
                //ricevute le carte del compagno a fine partita
                view.mostraCarteCompagno((Carta[]) inObj);
            }
            else if(inObj instanceof String[]){
                String[] nomi = (String[]) inObj;
                int indiceNomi = 0;
                try{
                    indiceNomi = (int) in.readObject();
                }catch (Exception e) { e.printStackTrace(); }

                while(view == null)
                    System.out.print("");
                view.setNomi(nomi, indiceNomi);
            }else{
                //fine partita
                finePartita((int[]) inObj);
                if(((int[])inObj)[0] == 0)
                    Classifica.addGiocatore(nome);
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