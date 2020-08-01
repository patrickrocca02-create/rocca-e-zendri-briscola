package view;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(750), copia);

        translateTransition.setFromX(0);
        translateTransition.setFromY(0);

        double x = cartaInTavola.getLayoutX() - copia.getLayoutX();
        double y = cartaInTavola.getLayoutY() - copia.getLayoutY();

        translateTransition.setToX(x);
        translateTransition.setToY(y);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
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

        translateTransition.setCycleCount(1);
        translateTransition.play();
    }

    public void mettiInTavolaAltriGiocatori(ImageView carta, ImageView cartaInTavola, Image imgNuovaCarta){
        System.out.println(carta);
        System.out.println(cartaInTavola);
        System.out.println(imgNuovaCarta);

        ImageView copia = copyImageView(carta);
        copia.setRotationAxis(new Point3D(0,1,0));

        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        SequentialTransition sequentialTransition = new SequentialTransition();
        ParallelTransition parallelTransition1 = new ParallelTransition();

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(375), copia);
        translateTransition1.setFromX(0);
        translateTransition1.setFromY(0);
        double x = cartaInTavola.getLayoutX() - copia.getLayoutX();
        double y = cartaInTavola.getLayoutY() - copia.getLayoutY();
        translateTransition1.setToX(x/2);
        translateTransition1.setToY(y/2);

        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(375));
        rotateTransition1.setFromAngle(0);
        rotateTransition1.setToAngle(90);

        parallelTransition1.getChildren().addAll(translateTransition1, rotateTransition1);

        parallelTransition1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                copia.setImage(imgNuovaCarta);
            }
        });

        ParallelTransition parallelTransition2 = new ParallelTransition();

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(375), copia);
        translateTransition2.setFromX(x/2);
        translateTransition2.setFromY(x/2);
        translateTransition2.setToX(x);
        translateTransition2.setToY(y);

        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(375));
        rotateTransition2.setFromAngle(90);
        rotateTransition2.setToAngle(0);

        parallelTransition2.getChildren().addAll(translateTransition2, rotateTransition2);

        sequentialTransition.getChildren().addAll(parallelTransition1, parallelTransition2);

        sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                carta.setVisible(true);
                cartaInTavola.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

        sequentialTransition.setCycleCount(1);
        parallelTransition1.play();
    }

    private ImageView copyImageView(ImageView iv){
        ImageView newIv = new ImageView();
        newIv.setImage(iv.getImage());
        newIv.setRotate(iv.getRotate());
        newIv.setLayoutX(iv.getLayoutX());
        newIv.setLayoutY(iv.getLayoutY());
        newIv.setFitWidth(iv.getFitWidth());
        newIv.setFitHeight(iv.getFitHeight());
        tavola.getChildren().add(newIv);
        return newIv;
    }
}