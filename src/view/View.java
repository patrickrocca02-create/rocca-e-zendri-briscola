package view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Carta;
import model.Disposizione;

import java.io.FileInputStream;
import java.util.ArrayList;

public class View {
    private Pane tavola;

    private ArrayList<CartaGrafica> carteInMano;
    private ArrayList<CartaGrafica> carteInTavola;
    private CartaGrafica[][] schiene;
    private CartaGrafica briscola;
    private CartaGrafica mazzo;

    private View precView;
    private Animator animator;

    private int[] posGiocatori;

    public View(Pane tavola, ImageView[] carteInTavola, ImageView briscola, ImageView[] carteInMano, ImageView[][] schiene, ImageView mazzo){
        this.tavola = tavola;

        this.carteInMano = new ArrayList<CartaGrafica>();
        this.carteInMano.add(new CartaGrafica(carteInMano[0]));
        this.carteInMano.add(new CartaGrafica(carteInMano[1]));
        this.carteInMano.add(new CartaGrafica(carteInMano[2]));

        this.carteInTavola = new ArrayList<CartaGrafica>();
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

        this.briscola = new CartaGrafica(briscola);

        this.mazzo = new CartaGrafica(mazzo);

        this.animator = new Animator(this.tavola);

        this.posGiocatori = new int[this.carteInTavola.size()];
        posGiocatori[0] = 0;
        switch (this.posGiocatori.length){
            case 2:
                posGiocatori[1] = 2;
                break;
            case 3:
                posGiocatori[1] = 2;
                posGiocatori[2] = 3;
                break;
            case 4:
                posGiocatori[1] = 1;
                posGiocatori[2] = 2;
                posGiocatori[3] = 3;
                break;
        }
    }

    public void aggiornaView(Disposizione d){
        ArrayList<Carta> carteInManoD = d.getCarteInMano();
        ArrayList<Carta> carteInTavolaD = d.getCarteInTavola();

        boolean nessunaCartaInMano = true;
        for(CartaGrafica c : this.carteInMano) {
            if (c.getCarta() != null)
                nessunaCartaInMano = false;
        }

        if(nessunaCartaInMano){
            if(d.getBriscola() != null) {
                //inizio partita
            }
        }else{
            int i = 0;
            //messa in tavola della carta del giocatore principale
            for(CartaGrafica c : this.carteInMano) {
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
            for (i = 0; i < this.schiene.length; i++){
                for (int j = 0; j < this.schiene[i].length; j++) {
                    if (this.schiene[i][j].isDiSchiena() != d.getSchiene()[i][j]) {
                        if (this.schiene[i][j].isDiSchiena()) {
                            ImageView schiena = this.schiene[i][j].getImageView();
                            ImageView cartaInTavola = this.carteInTavola.get(i + 1).getImageView();
                            Carta nuovaCarta = d.getCarteInTavola().get(i + 1);
                            final int posGiocatore = posGiocatori[i + 1];
                            try {
                                Image imgNuovaCarta = new Image(new FileInputStream("src/carte/" + nuovaCarta.getSeme() + " " + nuovaCarta.getNumero() + ".png"));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        animator.mettiInTavolaAltriGiocatori(schiena, cartaInTavola, imgNuovaCarta, posGiocatore);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            //pesca delle carte
            boolean pesca = true;
            for(Carta c : carteInTavolaD)
                if (c != null){
                    pesca = false;
                    break;
                }

            if(pesca){
                int[] IDs = new int[this.schiene.length + 1];
                //allineamento degli ID client con gli ID server
                IDs[0] = d.getID();
                for(i = 1; i < IDs.length; i ++)
                    if (IDs[i-1] == IDs.length - 1)
                        IDs[i] = 0;
                    else
                        IDs[i] = IDs[i-1] + 1;

                //ID client del vincitore
                int indexOfTurno = 0;
                for(i = 0; i < IDs.length; i++)
                    if (IDs[i] == d.getTurno()) {
                        indexOfTurno = i;
                        break;
                    }

                ImageView[] IVCarteInTavola = new ImageView[carteInManoD.size()];
                for(i = 0; i < IVCarteInTavola.length; i++)
                    IVCarteInTavola[i] = this.carteInTavola.get(i).getImageView();

                final int id = posGiocatori[indexOfTurno];

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        animator.prendiCarte(IVCarteInTavola, id);
                    }
                });

                //assegnazione delle nuove carte in tavola alla view
                i = 0;
                for(CartaGrafica c : this.carteInTavola)
                    c.setCarta(carteInTavolaD.get(i++));

                //disposizione degli ID per ordine di pesca
                IDs[0] = indexOfTurno;
                for(i = 1; i < IDs.length; i ++)
                    if (IDs[i-1] == IDs.length - 1)
                        IDs[i] = 0;
                    else
                        IDs[i] = IDs[i-1] + 1;

                for (i = 0; i < IDs.length; i++) {
                    try {
                        Thread.sleep(800);
                    }catch (Exception e){}

                    if(IDs[i] == 0) {
                        int j = 0;
                        for (CartaGrafica c : this.carteInMano) {
                            if (c.getCarta() == null && carteInManoD.get(j) != null) {
                                ImageView cartaInMano = this.carteInMano.get(j).getImageView();
                                Carta nuovaCarta = carteInManoD.get(j);
                                System.out.println(nuovaCarta);
                                try {
                                    Image imgNuovaCarta = new Image(new FileInputStream("src/carte/" + nuovaCarta.getSeme() + " " + nuovaCarta.getNumero() + ".png"));
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            animator.pescaCarta(mazzo.getImageView(), cartaInMano, imgNuovaCarta);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                carteInMano.get(j).setCarta(carteInManoD.get(j));
                            }
                            j++;
                        }
                    }
                    else {
                        for (int j = 0; j < this.schiene[IDs[i]-1].length; j++) {
                            if (this.schiene[IDs[i]-1][j].isDiSchiena() != d.getSchiene()[IDs[i]-1][j]) {
                                if (!this.schiene[IDs[i]-1][j].isDiSchiena()) {
                                    ImageView cartaInMano = this.schiene[IDs[i]-1][j].getImageView();
                                    final int posGiocatore = posGiocatori[IDs[i]];
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            animator.pescaAltriGiocatori(mazzo.getImageView(), cartaInMano, posGiocatore);
                                        }
                                    });
                                    schiene[IDs[i]-1][j].setSchiena(d.getSchiene()[IDs[i]-1][j]);
                                }
                            }
                        }
                    }
                }
            }
        }

        int i = 0;
        for(CartaGrafica c : this.carteInMano){
            c.setCarta(carteInManoD.get(i++));
        }

        i = 0;
        for(CartaGrafica c : this.carteInTavola)
            c.setCarta(carteInTavolaD.get(i++));

        for (i = 0; i < schiene.length; i++) {
            for (int j = 0; j < schiene[i].length; j++) {
                schiene[i][j].setSchiena(d.getSchiene()[i][j]);
            }
        }

        this.briscola.setCarta(d.getBriscola());

        if(d.getBriscola() == null)
            this.mazzo.setCarta(null);
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
    public CartaGrafica getBriscola(){
        return this.briscola;
    }
}
