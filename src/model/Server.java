package model;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Client> clients;
    private Partita partita;

    public Server(int nGiocatori){
        switch (nGiocatori) {
            case 2:
                partita = new Partita2();
                break;
            case 3:
                partita = new Partita3();
                break;
            case 4:
                partita = new Partita4();
                break;
        }

        partita.setServer(this);
        clients = new ArrayList<Client>();
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

            int[] indiciGiocatoriSuccessivi = partita.getIndiciGiocatoriSuccessivi(i);
            for(int idGiocatore : indiciGiocatoriSuccessivi){
                carteInTavola.add(partita.getCarteInTavola().get(idGiocatore));
            }

            Carta briscola = null;
            if(!partita.isMazzoFinito())
                briscola = partita.getBriscola();

            boolean schiene[][] = new boolean[partita.getGiocatori().size()-1][];
            for(int j = 0; j < schiene.length; j++){
                schiene[j] = partita.getGiocatori().get(indiciGiocatoriSuccessivi[j+1]).getListaSchiene();
            }

            Disposizione d = new Disposizione(carteInMano, carteInTavola, briscola, turno,schiene);
            clients.get(i++).inviaDisposizione(d);
        }
    }
}