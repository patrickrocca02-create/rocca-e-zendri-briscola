package model;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private ArrayList<Client> clients;
    private Partita partita;

    private ServerSocket ss;

    public static final int MIN_PORTA = 49152;
    public static final int MAX_PORTA = 65535;

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

        try {
            ss = new ServerSocket((int) (Math.random() * (MAX_PORTA - MIN_PORTA)) + MIN_PORTA);
        } catch(Exception e) {}
    }

    public void start(){
        for (int i = 0; i < partita.getNGiocatori(); i++) {
            try {
                System.out.println("In attesa del giocatore " + i + "...");
                clients.add(new Client(ss.accept(),i));
                System.out.println("Giocatore " + i + " connesso");
                System.out.println("server: " + partita.getNGiocatori());
                clients.get(i).inviaNGiocatori(partita.getNGiocatori());
                partita.getGiocatori().get(i).setNome(clients.get(i).ascoltaNome());
            } catch(Exception e) {}
        }

        inviaNomi();
        broadcastDisposizione();

        boolean finito = false;
        while(!finito){
            Carta c = clients.get(partita.getTurno()).ascoltaCarta();

            partita.processaMossa(c);

            broadcastDisposizione();

            finito = true;
            for(Giocatore g : partita.getGiocatori())
                for(Carta carta : g.getCarteInMano())
                    if(carta != null)
                        finito = false;
        }

        int i = 0;
        int[][] esiti = partita.getVincitori();
        for(Client c : clients){
            int[] esito = {esiti[i][0], esiti[i++][1]};
            c.finePartita(esito);
        }

        try{
            ss.close();
        }catch(Exception e){}
    }

    private void inviaNomi(){
        int i = 0;
        String[] nomi = new String[partita.getNGiocatori()];
        for(Giocatore g : partita.getGiocatori()){
            nomi[i++] = g.getNome();
        }
        i = 0;
        for(Client c : clients){
            c.inviaNomi(nomi,i++);
        }
    }

    protected void broadcastDisposizione(){
        int i = 0;
        for(Giocatore g : partita.getGiocatori()) {
            int turno = partita.getTurno();

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

            Disposizione d = new Disposizione(i, carteInMano, carteInTavola, briscola, turno,schiene);
            clients.get(i).inviaDisposizione(d);

            int nCarteInTavola = 0;
            for(Carta c : carteInTavola)
                if(c != null)
                    nCarteInTavola++;

            int nCarteInMano = 0;
            for(Carta c : g.getCarteInMano())
                if(c != null)
                    nCarteInMano++;

            if(partita.isMazzoFinito() && nCarteInTavola == 0 && nCarteInMano == 3 && partita.getNGiocatori() == 4){
                if(i > 1)
                    clients.get(i - 2).inviaCarteInMano(g.getCarteInMano());
                else
                    clients.get(i + 2).inviaCarteInMano(g.getCarteInMano());
            }

            i++;
        }
    }

    public int getPorta(){
        return this.ss.getLocalPort();
    }

    public InetAddress getIpv4(){
        return this.ss.getInetAddress();
    }
}