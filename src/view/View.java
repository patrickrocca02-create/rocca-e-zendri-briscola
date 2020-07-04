package view;

import javafx.scene.image.ImageView;
import model.Carta;
import model.Disposizione;

import java.util.ArrayList;

public class View {
    ArrayList<CartaGrafica> carteInMano;
    ArrayList<CartaGrafica> carteInTavola;
    ArrayList<CartaGrafica> schieneAvversarie;
    CartaGrafica briscola;
    CartaGrafica mazzo;

    public View(ImageView[] carteInTavola, ImageView briscola, ImageView[] carteInMano, ImageView[] schieneAvversarie, ImageView mazzo){
        this.carteInMano = new ArrayList<CartaGrafica>();
        this.carteInMano.add(new CartaGrafica(carteInMano[0]));
        this.carteInMano.add(new CartaGrafica(carteInMano[1]));
        this.carteInMano.add(new CartaGrafica(carteInMano[2]));

        this.carteInTavola = new ArrayList<CartaGrafica>();
        this.carteInTavola.add(new CartaGrafica(carteInTavola[0]));
        this.carteInTavola.add(new CartaGrafica(carteInTavola[1]));

        this.schieneAvversarie = new ArrayList<CartaGrafica>();
        this.schieneAvversarie.add(new CartaGrafica(schieneAvversarie[0]));
        this.schieneAvversarie.add(new CartaGrafica(schieneAvversarie[1]));
        this.schieneAvversarie.add(new CartaGrafica(schieneAvversarie[2]));

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

        i = 0;
        for(boolean schiena : d.getSchieneAvversarie()){
            schieneAvversarie.get(i++).setSchiena(schiena);
        }

        this.briscola.setCarta(d.getBriscola());

        if(d.getBriscola() == null)
            this.mazzo.setCarta(null);
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
}
