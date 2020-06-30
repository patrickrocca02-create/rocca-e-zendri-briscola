package model;

import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(Socket s){
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
        } catch (Exception e) {}
    }
    public Carta ascolta(){
        try {
            return (Carta) in.readObject();
        } catch (Exception e) {}
        return null;
    }

    //invia: 0 se ha vinto, 1 se ha perso, 2 se ha pareeggiato
    public void finePartita(int esito){
        try {
            out.write(esito);
        } catch (Exception e) {}
        try {
            s.close();
        } catch (Exception e) {}
    }
}
