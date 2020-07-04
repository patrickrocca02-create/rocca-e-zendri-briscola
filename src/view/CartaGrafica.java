package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Carta;

import java.io.FileInputStream;

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
        if(schiena){
            try {
                img.setImage(new Image(new FileInputStream("src/carte/schiena.png")));
            }catch (Exception e) {}
        }else{
            setCarta(null);
        }
    }
}