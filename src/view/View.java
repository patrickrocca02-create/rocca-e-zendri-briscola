package view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Carta;
import model.Disposizione;

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

    private boolean isAnimating;

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
        while (true){
            try {
                Thread.sleep(10);
            }catch (Exception e){}
            if(!isAnimating)
                break;
        }

        ArrayList<Carta> carteInMano = d.getCarteInMano();
        ArrayList<Carta> carteInTavola = d.getCarteInTavola();

        boolean nessunaCartaInMano = true;
        for(CartaGrafica c : this.carteInMano)
            if (c.getCarta() != null)
                nessunaCartaInMano = false;

        if(nessunaCartaInMano){
            if(d.getBriscola() != null){
                //fine partita
            }else {
                //inizio partita
            }
        }else{
            int i = 0;
            for(CartaGrafica c : this.carteInMano) {
                if (!c.equals(carteInMano.get(i))) {
                    if (this.carteInTavola.get(0).getCarta() == null) {
                        System.out.println("Metti in tavola!");
                        animator.mettiInTavola(this.carteInMano.get(i).getImageView(), this.carteInTavola.get(0).getImageView());
                    } else {
                        //pesca
                    }
                }
            }
        }

        int i = 0;
        for(CartaGrafica c : this.carteInMano){
            c.setCarta(carteInMano.get(i++));
        }

        i = 0;
        for(CartaGrafica c : this.carteInTavola)
            c.setCarta(carteInTavola.get(i++));

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
    public void setIsAnimating(boolean isAnimating){
        this.isAnimating = isAnimating;
    }
}
