package model;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Client> clients;
    private Partita partita;

    public Server(int nGiocatori){
        switch (nGiocatori) {
            case 2:
                partita = new Partita2();
                clients = new ArrayList<Client>();
        }
    }

    public void start(){
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(2201);
        } catch(Exception e) {}

        for (int i = 0; i < partita.getNGiocatori(); i++) {
            try {
                System.out.println("In attesa del giocatore " + i + "...");
                clients.add(new Client(ss.accept()));
                System.out.println("Giocatore " + i + " connesso");
            } catch(Exception e) {}
        }

        broadcastDisposizione();

        boolean finito = false;
        while(!finito){
            System.out.println("In attesa di una carta...");
            Carta c = clients.get(partita.getTurno()).ascolta();

            partita.processaMossa(c);
            System.out.println("Carta processata");

            broadcastDisposizione();

            finito = true;
            for(Giocatore g : partita.getGiocatori())
                if(g.getCarteInMano().size() != 0)
                    finito = false;
        }

        int i = 0;
        int[] esiti = partita.getVincitori();
        for(Client c : clients){
            c.finePartita(esiti[i++]);
        }

        try{
            ss.close();
        }catch(Exception e){}
    }

    private void broadcastDisposizione(){
        int i = 0;
        for(Giocatore g : partita.getGiocatori()) {
            boolean turno = false;

            if (i == partita.getTurno())
                turno = true;

            clients.get(i++).inviaDisposizione(new Disposizione(g.getCarteInMano(), partita.getCarteInTavola(), partita.getBriscola(),turno));
        }
    }
}
