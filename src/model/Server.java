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
                partita.setServer(this);
                clients = new ArrayList<Client>();
        }
    }

    public void start(){
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(2200);
        } catch(Exception e) {}

        for (int i = 0; i < partita.getNGiocatori(); i++) {
            try {
                System.out.println("In attesa del giocatore " + i + "...");
                clients.add(new Client(ss.accept()));
                System.out.println("Giocatore " + i + " connesso");
            } catch(Exception e) {}
        }

        broadcastDisposizione(false);

        boolean finito = false;
        while(!finito){
            Carta c = clients.get(partita.getTurno()).ascolta();

            partita.processaMossa(c);

            broadcastDisposizione(false);

            finito = true;
            for(Giocatore g : partita.getGiocatori())
                for(Carta carta : g.getCarteInMano())
                    if(carta != null)
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

    protected void broadcastDisposizione(boolean fineMano){
        int i = 0;
        for(Giocatore g : partita.getGiocatori()) {
            boolean turno = false;

            if (i == partita.getTurno() && !fineMano)
                turno = true;

            ArrayList<Carta> carteInMano = new ArrayList<>(g.getCarteInMano());
            ArrayList<Carta> carteInTavola = new ArrayList<>();

            if(i == 0) {
                carteInTavola.add(partita.getCarteInTavola().get(0));
                carteInTavola.add(partita.getCarteInTavola().get(1));
            }
            else{
                carteInTavola.add(partita.getCarteInTavola().get(1));
                carteInTavola.add(partita.getCarteInTavola().get(0));
            }

            Carta briscola = null;
            if(!partita.isMazzoFinito())
                briscola = partita.getBriscola();

            int avversario = 0;
            if(i == 0)
                avversario = 1;
            boolean schieneAvversarie[] = partita.getGiocatori().get(avversario).getListaSchiene();

            Disposizione d = new Disposizione(carteInMano, carteInTavola, briscola, turno,schieneAvversarie);
            clients.get(i++).inviaDisposizione(d);
        }
    }
}