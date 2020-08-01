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

    private Pane tavola;

    public Animator(Pane tavola){
        this.tavola = tavola;
    }

    public void mettiInTavola(ImageView carta, ImageView cartaInTavola){
        ImageView copia = copyImageView(carta);

        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        Path path = new Path();
        path.getElements().add(new MoveTo(copia.getFitWidth()/2,copia.getFitHeight()/2));

        double x = cartaInTavola.getLayoutX() - carta.getLayoutX() + carta.getFitWidth()/2;
        double y = cartaInTavola.getLayoutY() - carta.getLayoutY() + carta.getFitHeight()/2;
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
                //carta.setTranslateX(0);
                //carta.setTranslateY(0);
                //view.setIsAnimating(false);
                carta.setVisible(true);
                cartaInTavola.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

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