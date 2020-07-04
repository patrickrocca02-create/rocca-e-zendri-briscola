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

    public Client(InetAddress ipServer, int porta, View view){
        this.view = view;

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
            out.writeObject(view.getCarta(indice).getCarta());
            out.flush();
        }catch (Exception e) { e.printStackTrace(); }
    }

    private void finePartita(int esito) {
        if(esito == 0)
            System.out.println("VINTO");
        else if (esito == 1)
            System.out.println("PERSO");
        else
            System.out.println("PAREGGIO");
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
                Disposizione d = (Disposizione) inObj;
                this.turno = d.isTurno();
                view.aggiornaView(d);
                try {
                    Thread.sleep(1000);
                }catch (Exception e){}
            } else {
                finePartita((int) (Integer) inObj);
                fine = true;
            }
        }
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }
}
