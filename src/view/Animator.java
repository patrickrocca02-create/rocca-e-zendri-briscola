package view;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Animator {

    private Punto[] carteInTavola;

    public Animator(ImageView[] carteInTavola){
        this.carteInTavola = new Punto[carteInTavola.length];
        for (int i = 0; i < carteInTavola.length; i++)
            this.carteInTavola[i] = new Punto(carteInTavola[i].getX(),carteInTavola[i].getY());
        System.out.println(carteInTavola[0].getX() + " " + carteInTavola[0].getY());
    }

    public void mettiInTavola(ImageView carta){
        //ImageView clone = new ImageView(carta);
        Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new LineTo(122,-173));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(2000));
        pathTransition.setPath(path);
        pathTransition.setNode(carta);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    private ImageView copyImageView(ImageView iv){
        ImageView newIv = new ImageView();
        newIv.setImage(iv.getImage());
        newIv.setX(iv.getX());
        newIv.setY(iv.getY());
        return newIv;
    }
}
