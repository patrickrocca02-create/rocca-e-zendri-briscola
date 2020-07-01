package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Carta;

public class CartaGrafica {
    private Carta carta;
    private ImageView img;

    public CartaGrafica(ImageView img){
        this.img = img;
    }

    public void setCarta(Carta c){
        setImg(c);
        this.carta = c;
    }
    public Carta getCarta(){
        return this.carta;
    }

    private void setImg(Carta c){
        System.out.println("../carte/" + c.getSeme() + " " + c.getNumero() + ".png");
        img.setImage(new Image("file:../carte/" + c.getSeme() + " " + c.getNumero() + ".png"));
    }
}