package classifica;

import java.io.*;
import java.util.ArrayList;

public class Classifica {

    private ArrayList<Giocatore> giocatori;
    private static Classifica attuale;
    private static final String PERCORSO_CLASSIFICA = "./src/classifica/classifica.dat";

    public Classifica(){
        this.giocatori = new ArrayList<>();
    }
    public Classifica(ArrayList<Giocatore> giocatori){
        this.giocatori = giocatori;
    }

    public static void load() {
        if (new File(PERCORSO_CLASSIFICA).exists()) {
            try {
                FileInputStream fi = new FileInputStream(new File(PERCORSO_CLASSIFICA));
                ObjectInputStream oi = new ObjectInputStream(fi);

                ArrayList<Giocatore> giocatori = (ArrayList<Giocatore>) oi.readObject();
                Classifica.attuale = new Classifica(giocatori);

                oi.close();
                fi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Classifica.attuale = new Classifica();
            salva();
        }
    }

    public static void addGiocatore(String nome){
        ArrayList<Giocatore> giocatori = Classifica.getAttuale().getGiocatori();

        boolean trovato = false;
        for(Giocatore g : giocatori) {
            if (g.getNome().equals(nome)) {
                g.incrementaVittorie();
                trovato = true;
                break;
            }
        }

        if(!trovato){
            giocatori.add(new Giocatore(nome, 1));
        }else{
            ordinaGiocatori(giocatori);
        }

        salva();
    }

    public static void ordinaGiocatori(ArrayList<Giocatore> giocatori){
        for (int i = 0; i < giocatori.size(); i++) {
            for (int j = i; j < giocatori.size(); j++) {
                if(giocatori.get(i).getnVittorie() <  giocatori.get(j).getnVittorie()){
                    Giocatore g = giocatori.get(i);
                    giocatori.set(i, giocatori.get(j));
                    giocatori.set(j, g);
                }
            }
        }
    }


    private static void salva(){
        try {
            FileOutputStream f = new FileOutputStream(new File(PERCORSO_CLASSIFICA), false);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(Classifica.getAttuale().getGiocatori());

            o.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reset(){
        Classifica.getAttuale().getGiocatori().clear();
        salva();
    }

    public ArrayList<Giocatore> getGiocatori(){
        return this.giocatori;
    }
    public static Classifica getAttuale(){
        return Classifica.attuale;
    }
}
