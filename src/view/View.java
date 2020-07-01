package view;

import javafx.scene.image.ImageView;
import model.Carta;
import model.Disposizione;

import java.util.ArrayList;

public class View {
    ArrayList<CartaGrafica> carteInMano;
    ArrayList<CartaGrafica> carteInTavola;
    CartaGrafica briscola;

    public View(ImageView cartaInTavolaClient, ImageView cartaInTavolaAvversario, ImageView briscola, ImageView carta1, ImageView carta2, ImageView carta3){
        this.carteInMano = new ArrayList<CartaGrafica>();
        this.carteInMano.add(new CartaGrafica(carta1));
        this.carteInMano.add(new CartaGrafica(carta2));
        this.carteInMano.add(new CartaGrafica(carta3));

        this.carteInTavola = new ArrayList<CartaGrafica>();
        this.carteInTavola.add(new CartaGrafica(cartaInTavolaClient));
        this.carteInTavola.add(new CartaGrafica(cartaInTavolaAvversario));

        this.briscola = new CartaGrafica(briscola);
    }

    public void aggiornaView(Disposizione d){
        ArrayList<Carta> carteInMano = d.getCarteInMano();
        ArrayList<Carta> carteInTavola = d.getCartaInTavola();

        int i = 0;
        for(CartaGrafica c : this.carteInMano){
            c.setCarta(carteInMano.get(i++));
        }

        Carta carta1 = null;
        Carta carta2 = null;

        if(carteInTavola.size() != 0){
            carta1 = carteInTavola.get(0);
            if(carteInTavola.size() == 2)
                carta2 =carteInTavola.get(1);
        }

        if(d.isTurno()){
            if(carta1 != null)
                this.carteInTavola.get(0).setCarta(carta1);
            if(carta2 != null)
                this.carteInTavola.get(1).setCarta(carta2);
        }else{
            if(carta1 != null)
                this.carteInTavola.get(1).setCarta(carta1);
            if(carta2 != null)
                this.carteInTavola.get(0).setCarta(carta2);
        }

        this.briscola.setCarta(d.getBriscola());
    }

    public CartaGrafica getCarta(int indice) {
        return carteInMano.get(indice);
    }
}
