package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Carta;

import java.io.FileInputStream;

public class CartaGrafica {
    private Carta carta;
    private ImageView img;
    private boolean diSchiena;

    public CartaGrafica(ImageView img){
        this.diSchiena = false;
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
        if(c == null){
            try {
                img.setImage(new Image(new FileInputStream("src/carte/blank.png")));
            }catch (Exception e) {e.printStackTrace();}
        }else {
            try {
                img.setImage(new Image(new FileInputStream("src/carte/" + c.getSeme() + " " + c.getNumero() + ".png")));
            } catch (Exception e) {e.printStackTrace();}
        }
    }

    public void setSchiena(boolean schiena){
        this.diSchiena = schiena;
        if(schiena){
            try {
                img.setImage(new Image(new FileInputStream("src/carte/schiena.png")));
            }catch (Exception e) {}
        }else{
            setCarta(null);
        }
    }

    public boolean isDiSchiena(){
        return this.diSchiena;
    }

    public ImageView getImageView(){
        return img;
    }

    public boolean equals(CartaGrafica c){
        if(this.diSchiena)
            if(c.isDiSchiena())
                return true;
            else
                return false;

        if(this.carta == null)
            if(c.getCarta() == null && !c.isDiSchiena())
                return true;
            else
                return false;

        return this.carta.equals(c.getCarta());
    }
}