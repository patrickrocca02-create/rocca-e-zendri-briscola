package model;

import java.io.*;
import java.net.Socket;

public class Client {
    private int ID;
    private Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(Socket s, int ID){
        this.ID = ID;
        this.s = s;
        try{
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
        }
        catch(Exception e){}
    }

    public void inviaDisposizione(Disposizione d){
        try {
            out.writeObject(d);
            out.flush();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public Carta ascolta(){
        try {
            return (Carta) in.readObject();
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    //invia: 0 se ha vinto, 1 se ha perso, 2 se ha pareeggiato
    public void finePartita(int esito){
        try {
            out.writeObject((Integer) esito);
            out.flush();
        } catch (Exception e) {}
        try {
            s.close();
        } catch (Exception e) {}
    }
}