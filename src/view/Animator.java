package view;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Animator {

    private Punto[] carteInTavola;
    private Pane tavola;
    private View view;

    public Animator(ImageView[] carteInTavola, Pane tavola, View view){
        this.tavola = tavola;
        this.view = view;
        this.carteInTavola = new Punto[carteInTavola.length];
        for (int i = 0; i < carteInTavola.length; i++)
            this.carteInTavola[i] = new Punto(carteInTavola[i].getLayoutX(),carteInTavola[i].getLayoutY());
        System.out.println(carteInTavola[0].getX() + " " + carteInTavola[0].getY());
    }

    public void mettiInTavola(ImageView carta){
        Path path = new Path();
        path.getElements().add(new MoveTo(carta.getFitWidth()/2,carta.getFitHeight()/2));

        double x = carteInTavola[0].getX() - carta.getLayoutX() + carta.getFitWidth()/2;
        double y = carteInTavola[0].getY() - carta.getLayoutY() + carta.getFitHeight()/2;
        path.getElements().add(new LineTo(x,y));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setPath(path);
        pathTransition.setNode(carta);
        //pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        //pathTransition.setCycleCount(Timeline.INDEFINITE);
        //pathTransition.setAutoReverse(true);
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                carta.setTranslateX(0);
                carta.setTranslateY(0);
                view.setIsAnimating(false);
            }
        });

        view.setIsAnimating(true);
        pathTransition.play();
    }

    private ImageView copyImageView(ImageView iv){
        ImageView newIv = new ImageView();
        newIv.setImage(iv.getImage());
        newIv.setRotate(iv.getRotate());
        newIv.setLayoutX(iv.getLayoutX());
        newIv.setLayoutY(iv.getLayoutY());
        tavola.getChildren().add(newIv);
        return newIv;
    }
}