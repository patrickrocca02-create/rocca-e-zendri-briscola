package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Carta;

public class CartaGrafica extends ImageView {
    private Carta carta;

    public CartaGrafica(Carta c){
        this.carta = c;
        //setImage(new Image("../controller"));
    }
}
