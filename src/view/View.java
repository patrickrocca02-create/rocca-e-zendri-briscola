package view;

import javafx.scene.image.ImageView;
import model.Carta;
import model.Disposizione;

import java.util.ArrayList;

public class View {
    ArrayList<CartaGrafica> carteInMano;
    ArrayList<CartaGrafica> carteInTavola;
    CartaGrafica[][] schiene;
    CartaGrafica briscola;
    CartaGrafica mazzo;

    public View(ImageView[] carteInTavola, ImageView briscola, ImageView[] carteInMano, ImageView[][] schiene, ImageView mazzo){
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
    }

    public void aggiornaView(Disposizione d){
        ArrayList<Carta> carteInMano = d.getCarteInMano();
        ArrayList<Carta> carteInTavola = d.getCarteInTavola();

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
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
    public CartaGrafica getBriscola(){
        return this.briscola;
    }
}
