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
            for(CartaGrafica c : this.carteInMano) {
                if (!(c.getCarta() == null && carteInManoD.get(i) == null || (c.getCarta() != null && c.getCarta().equals(carteInManoD.get(i))))) {
                    if (this.carteInTavola.get(0).getCarta() == null) {
                        ImageView cartaInMano = carteInMano.get(i).getImageView();
                        ImageView cartaInTavola = carteInTavola.get(0).getImageView();
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

            for (i = 0; i < schiene.length; i++){
                for (int j = 0; j < schiene[i].length; j++) {
                    if (schiene[i][j].isDiSchiena() != d.getSchiene()[i][j]) {
                        if (schiene[i][j].isDiSchiena()) {
                            ImageView schiena = schiene[i][j].getImageView();
                            ImageView cartaInTavola = carteInTavola.get(i + 1).getImageView();
                            Carta nuovaCarta = d.getCarteInTavola().get(i + 1);
                            try {
                                Image imgNuovaCarta = new Image(new FileInputStream("src/carte/" + nuovaCarta.getSeme() + " " + nuovaCarta.getNumero() + ".png"));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        animator.mettiInTavolaAltriGiocatori(schiena, cartaInTavola, imgNuovaCarta);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            int[] IDs = new int[schiene.length + 1];
            IDs[0] = d.getID();
            for(i = 1; i < IDs.length; i ++)
                if (IDs[i-1] == IDs.length - 1)
                    IDs[i] = 0;
                else
                    IDs[i] = IDs[i-1] + 1;

            int indexOfTurno = 0;
            for(i = 0; i < IDs.length; i++)
                if (IDs[i] == d.getTurno()) {
                    indexOfTurno = i;
                    break;
                }

            IDs[0] = indexOfTurno;
            for(i = 1; i < IDs.length; i ++)
                if (IDs[i-1] == IDs.length - 1)
                    IDs[i] = 0;
                else
                    IDs[i] = IDs[i-1] + 1;

            for (i = 0; i < IDs.length; i++) {
                if(IDs[i] == 0) {
                    int j = 0;
                    for (CartaGrafica c : this.carteInMano) {
                        if (!(c.getCarta() == null && carteInManoD.get(j) == null || (c.getCarta() != null && c.getCarta().equals(carteInManoD.get(j))))) {
                            if (this.carteInTavola.get(0).getCarta() == null) {
                                ImageView cartaInMano = carteInMano.get(j).getImageView();
                                ImageView cartaInTavola = carteInTavola.get(0).getImageView();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        animator.mettiInTavola(cartaInMano, cartaInTavola);
                                    }
                                });
                            }
                        }
                        j++;
                    }
                }
                else {
                    for (int j = 0; j < schiene[IDs[i]-1].length; j++) {
                        if (schiene[IDs[i]-1][j].isDiSchiena() != d.getSchiene()[IDs[i]-1][j]) {
                            if (!schiene[IDs[i]-1][j].isDiSchiena()) {
                                ImageView mazzo = this.mazzo.getImageView();
                                ImageView cartaInMano = schiene[IDs[i]-1][j].getImageView();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        animator.pescaAltriGiocatori(mazzo, cartaInMano);
                                    }
                                });
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

        precView = this;
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
    public CartaGrafica getBriscola(){
        return this.briscola;
    }
}
