package view;

import com.sun.security.jgss.GSSUtil;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import model.Carta;
import model.Disposizione;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class View {
    private Pane tavola;

    private ArrayList<CartaGrafica> carteInMano;
    private ArrayList<CartaGrafica> carteInTavola;
    private CartaGrafica[][] schiene;
    private CartaGrafica briscola;
    private CartaGrafica mazzo;
    private Ellipse[] ellissiGiocatori;
    private Pane schermataIniziale;

    private Pane schermataVittoria;
    private Pane schermataSconfitta;
    private Pane schermataPareggio;
    private Pane schermataCarteCompagno;

    private ArrayList<ImageView> carteCompagno;
    private Text testoCarteCompagno;

    private Text puntiVittoria;
    private Text puntiSconfitta;
    private Text puntiPareggio;

    private Text testoIpServer;
    private Text testoPorta;

    private Text[] usernames;

    private Animator animator;

    private boolean primaDisposizione;


    public View(Pane tavola, ImageView[] carteInTavola, ImageView briscola, ImageView[] carteInMano, ImageView[][] schiene, ImageView mazzo, Text[] usernames, Ellipse[] ellissiGiocatori, Pane[] schermate, Text[] testi, ImageView[] carteCompagno){
        this.tavola = tavola;

        this.carteInMano = new ArrayList<>();
        this.carteInMano.add(new CartaGrafica(carteInMano[0]));
        this.carteInMano.add(new CartaGrafica(carteInMano[1]));
        this.carteInMano.add(new CartaGrafica(carteInMano[2]));

        this.carteInTavola = new ArrayList<>();
        for (ImageView c : carteInTavola) {
            this.carteInTavola.add(new CartaGrafica(c));
        }

        this.schiene = new CartaGrafica[schiene.length][];
        for (int i = 0; i < schiene.length; i++) {
            this.schiene[i] = new CartaGrafica[3];
            for (int j = 0; j < this.schiene[i].length; j++) {
                this.schiene[i][j] = new CartaGrafica(schiene[i][j]);
            }
        }

        this.schermataIniziale = schermate[0];
        this.schermataVittoria = schermate[1];
        this.schermataSconfitta = schermate[2];
        this.schermataPareggio = schermate[3];
        this.schermataCarteCompagno = schermate[4];

        this.carteCompagno = new ArrayList<>();
        for(ImageView iv : carteCompagno)
            this.carteCompagno.add(iv);

        this.puntiVittoria = testi[0];
        this.puntiSconfitta = testi[1];
        this.puntiPareggio = testi[2];
        this.testoIpServer = testi[3];
        this.testoPorta = testi[4];
        this.testoCarteCompagno = testi[5];

        this.briscola = new CartaGrafica(briscola);
        this.mazzo = new CartaGrafica(mazzo);
        this.usernames = usernames;
        this.ellissiGiocatori = ellissiGiocatori;

        this.animator = new Animator(this.tavola, this.carteInTavola.size());

        this.primaDisposizione = true;
    }

    public void aggiornaView(Disposizione d){
        ArrayList<Carta> carteInManoD = d.getCarteInMano();
        ArrayList<Carta> carteInTavolaD = d.getCarteInTavola();

        int[] IDs = new int[this.schiene.length + 1];
        //allineamento degli ID client con gli ID server
        IDs[0] = d.getID();
        for (int i = 1; i < IDs.length; i++)
            if (IDs[i - 1] == IDs.length - 1)
                IDs[i] = 0;
            else
                IDs[i] = IDs[i - 1] + 1;

        //ID client del vincitore
        int indexOfTurno = 0;
        for (int i = 0; i < IDs.length; i++)
            if (IDs[i] == d.getTurno()) {
                indexOfTurno = i;
                break;
            }

        if(!primaDisposizione) {
            int nCarteInMano = 0;
            for(Carta c : carteInManoD)
                if(c != null)
                    nCarteInMano++;

            int i = 0;
            //messa in tavola della carta del giocatore principale
            for (CartaGrafica c : this.carteInMano) {
                if (!(c.getCarta() == null && carteInManoD.get(i) == null || (c.getCarta() != null && c.getCarta().equals(carteInManoD.get(i))))) {
                    if (this.carteInTavola.get(0).getCarta() == null) {
                        ImageView cartaInMano = this.carteInMano.get(i).getImageView();
                        ImageView cartaInTavola = this.carteInTavola.get(0).getImageView();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                animator.mettiInTavola(cartaInMano, cartaInTavola);
                            }
                        });
                    }
                }
                i++;
            }

            //messa in tavola delle carte degli altri giocatori
            for (i = 0; i < this.schiene.length; i++) {
                for (int j = 0; j < this.schiene[i].length; j++) {
                    if (this.schiene[i][j].isDiSchiena() != d.getSchiene()[i][j]) {
                        if (this.schiene[i][j].isDiSchiena()) {
                            ImageView schiena = this.schiene[i][j].getImageView();
                            ImageView cartaInTavola = this.carteInTavola.get(i + 1).getImageView();
                            Carta nuovaCarta = d.getCarteInTavola().get(i + 1);
                            Image imgNuovaCarta = nuovaCarta.getImage();
                            final int posGiocatore = animator.getPosGiocatori(i + 1);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    animator.mettiInTavolaAltriGiocatori(schiena, cartaInTavola, imgNuovaCarta, posGiocatore);
                                }
                            });
                        }
                    }
                }
            }

            //pesca delle carte
            boolean pesca = true;
            for (Carta c : carteInTavolaD)
                if (c != null) {
                    pesca = false;
                    break;
                }

            if (pesca) {
                ImageView[] IVCarteInTavola = new ImageView[carteInTavolaD.size()];
                for (i = 0; i < IVCarteInTavola.length; i++)
                    IVCarteInTavola[i] = this.carteInTavola.get(i).getImageView();

                final int id = animator.getPosGiocatori(indexOfTurno);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        animator.prendiCarte(IVCarteInTavola, id);
                    }
                });

                //assegnazione delle nuove carte in tavola alla view
                i = 0;
                for (CartaGrafica c : this.carteInTavola)
                    c.setCarta(carteInTavolaD.get(i++));


                if (nCarteInMano == 3) {
                    //disposizione degli ID per ordine di pesca
                    IDs[0] = indexOfTurno;
                    for (i = 1; i < IDs.length; i++)
                        if (IDs[i - 1] == IDs.length - 1)
                            IDs[i] = 0;
                        else
                            IDs[i] = IDs[i - 1] + 1;

                    for (i = 0; i < IDs.length; i++) {
                        try {
                            Thread.sleep(animator.DELAY_PESCA*3/4);
                        } catch (Exception e) {
                        }

                        if (IDs[i] == 0) {
                            int j = 0;
                            for (CartaGrafica c : this.carteInMano) {
                                if (c.getCarta() == null && carteInManoD.get(j) != null) {
                                    ImageView cartaInMano = this.carteInMano.get(j).getImageView();
                                    Carta nuovaCarta = carteInManoD.get(j);
                                    Image imgNuovaCarta = nuovaCarta.getImage();
                                    final int idPesca = i;
                                    try {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (briscola != null && d.getBriscola() == null && idPesca == schiene.length)
                                                    animator.pescaBriscola(briscola.getImageView(), cartaInMano);

                                                else {
                                                    animator.pescaCarta(mazzo.getImageView(), cartaInMano, imgNuovaCarta);
                                                    if (briscola != null && d.getBriscola() == null && idPesca == schiene.length-1)
                                                        mazzo.setCarta(null);
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Thread.sleep(animator.DELAY_PESCA/4);
                                    } catch (Exception e) {
                                    }
                                    carteInMano.get(j).setCarta(carteInManoD.get(j));
                                }
                                j++;
                            }
                        } else {
                            for (int j = 0; j < this.schiene[IDs[i] - 1].length; j++) {
                                if (this.schiene[IDs[i] - 1][j].isDiSchiena() != d.getSchiene()[IDs[i] - 1][j]) {
                                    if (!this.schiene[IDs[i] - 1][j].isDiSchiena()) {
                                        ImageView cartaInMano = this.schiene[IDs[i] - 1][j].getImageView();
                                        final int posGiocatore = animator.getPosGiocatori(IDs[i]);
                                        final int idPesca = i;
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (briscola != null && d.getBriscola() == null && idPesca == schiene.length)
                                                    animator.pescaBriscolaAltriGiocatori(briscola.getImageView(), cartaInMano, posGiocatore);
                                                else {
                                                    animator.pescaAltriGiocatori(mazzo.getImageView(), cartaInMano, posGiocatore);
                                                    if (briscola != null && d.getBriscola() == null && idPesca == schiene.length-1)
                                                        mazzo.setCarta(null);
                                                }
                                            }
                                        });

                                        try {
                                            Thread.sleep(animator.DELAY_PESCA/4);
                                        } catch (Exception e) {
                                        }
                                        schiene[IDs[i] - 1][j].setSchiena(d.getSchiene()[IDs[i] - 1][j]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            if(d.getID() == schiene.length) {
                Platform.runLater(() -> {
                    tavola.getChildren().remove(schermataIniziale);
                });
            }
            else {
                Platform.runLater(() -> {
                    animator.rimuoviSchermataIniziale(schermataIniziale);
                });
            }
            inizioPartita(carteInManoD, briscola, d.getBriscola());
            this.primaDisposizione = false;
        }

        if(d.getTurno() != -1)
            animator.cambiaTurno(indexOfTurno, ellissiGiocatori);

        try{
            Thread.sleep(50);
        }catch (Exception e){}
        int i = 0;
        for(CartaGrafica c : this.carteInMano){
            c.setCarta(carteInManoD.get(i++));
        }

        i = 0;
        for(CartaGrafica c : this.carteInTavola) {
            c.setCarta(carteInTavolaD.get(i++));
        }

        for (i = 0; i < schiene.length; i++) {
            for (int j = 0; j < schiene[i].length; j++) {
                schiene[i][j].setSchiena(d.getSchiene()[i][j]);
            }
        }

        this.briscola.setCarta(d.getBriscola());

        if(d.getBriscola() == null)
            this.mazzo.setCarta(null);
    }

    public void primaSchermata(String ip, int porta){
        testoIpServer.setText("Indirizzo IP del Server: " + ip);
        testoPorta.setText("Porta della partita: " + porta);
    }

    public void inizioPartita(ArrayList<Carta> carteInManoD, CartaGrafica briscola, Carta briscolaD){
        Image[] imgCarteInMano = new Image[3];
        ImageView[] IVCarteInMano = new ImageView[3];
        ImageView[][] IVSchiene = new ImageView[schiene.length][schiene[0].length];

        for (int i = 0; i < IVSchiene.length; i++) {
            for (int j = 0; j < IVSchiene[0].length; j++) {
                IVSchiene[i][j] = schiene[i][j].getImageView();
            }
        }

        for(int i = 0; i < 3; i++){
            try {
                imgCarteInMano[i] = new Image(new FileInputStream("src/carte/" + carteInManoD.get(i).getSeme() + " " + carteInManoD.get(i).getNumero() + ".png"));
            }catch (Exception e){}

            IVCarteInMano[i] = carteInMano.get(i).getImageView();
        }

        final Image imgBriscola = briscolaD.getImage();

        for (int i = 0; i < 3; i++) {
            final int idI = i;
            try{Thread.sleep(animator.DELAY_PESCA+3/4);} catch(Exception e) {}
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    animator.pescaCarta(mazzo.getImageView(), carteInMano.get(idI).getImageView(), imgCarteInMano[idI]);
                }
            });
            try{Thread.sleep(animator.DELAY_PESCA/4);} catch(Exception e) {}
            this.carteInMano.get(i).setCarta(carteInManoD.get(i));

            for (int j = 0; j < schiene.length; j++) {
                final int idJ = j;
                try{Thread.sleep(animator.DELAY_PESCA*3/4);} catch(Exception e) {}
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        animator.pescaAltriGiocatori(mazzo.getImageView(), schiene[idJ][idI].getImageView(), animator.getPosGiocatori(idJ+1));
                    }
                });
                try{Thread.sleep(animator.DELAY_PESCA/4);} catch(Exception e) {}
                this.schiene[j][i].setSchiena(true);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                animator.scopriBriscola(mazzo.getImageView(), briscola.getImageView(), imgBriscola);
            }
        });
    }

    public void finePartita(int esito , int punti){
        if(esito == 0)
            animator.mostraSchermataFinale(schermataVittoria, puntiVittoria, punti);
        else if (esito == 1)
            animator.mostraSchermataFinale(schermataSconfitta, puntiSconfitta, punti);
        else
            animator.mostraSchermataFinale(schermataPareggio, puntiPareggio, punti);
    }

    public void mostraCarteCompagno(Carta[] carteCompagno){
        Image[] imgsCarte = new Image[3];
        for (int i = 0; i < 3; i++) {
            imgsCarte[i] = carteCompagno[i].getImage();
        }

        testoCarteCompagno.setText("Carte di " + usernames[2].getText() + ":");

        ImageView[] ivs = this.carteCompagno.toArray(new ImageView[3]);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                animator.mostraSchermataCarteCompagno(schermataCarteCompagno, ivs, imgsCarte);
                schermataCarteCompagno.setMouseTransparent(false);
            }
        });
    }

    public void nascondiCarteCompagno(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                animator.nascondiSchermataCarteCompagno(schermataCarteCompagno);
            }
        });
    }

    public void setNomi(String[] nomi, int indice){
        for (int i = 0; i < nomi.length; i++) {
            if(indice == nomi.length)
                indice = 0;
            this.usernames[i].setText(nomi[indice++]);
        }
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
    public CartaGrafica getBriscola(){
        return this.briscola;
    }
}
