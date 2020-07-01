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

    public Client(InetAddress ipServer, int porta, View view){
        this.view = view;

        Socket client = null;
        try{
            client = new Socket(ipServer,porta);
        }catch (Exception e){}
        try {
            out = new ObjectOutputStream(client.getOutputStream());
        }catch (Exception e){}
        try {
            in = new ObjectInputStream(client.getInputStream());
        }catch (Exception e){}
    }

    public void inviaCarta(int indice){
        try{
            out.writeObject(view.getCarta(indice).getCarta());
        }catch (Exception e) {}
    }

    public void inviaCarta(Carta c){
        try {
            out.writeObject(c);
        }catch (Exception e) {}
    }

    private void finePartita(int esito) {
        if(esito == 0)
            System.out.println("VINTO");
        else if (esito == 1)
            System.out.println("PERSO");
        else
            System.out.println("PAREGGIO");
    }

    @Override
    public void run() {

        boolean fine = false;
        Object inObj = null;

        while (!fine) {
            try {
                System.out.println("In attesa di una nuova dispozione...");
                inObj = in.readObject();
            } catch (Exception e) {
            }

            if (inObj instanceof Disposizione) {
                view.aggiornaView((Disposizione) inObj);
            } else {
                finePartita((int) (Integer) inObj);
                fine = true;
            }
        }
    }
}
