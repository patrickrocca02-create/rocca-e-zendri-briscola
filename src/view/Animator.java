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

    public void mettiInTavola(ImageView carta, ImageView cartainTavola){
        carta.setVisible(false);
        cartainTavola.setVisible(false);

        sposta(carta, cartainTavola, 750);
    }

    public void mettiInTavolaAltriGiocatori(ImageView carta, ImageView cartaInTavola, Image imgNuovaCarta){
        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        scopriESposta(carta, cartaInTavola, imgNuovaCarta, 1000);
    }

    public void pescaCarta(ImageView mazzo, ImageView cartaInMano, Image imgNuovaCarta){
        cartaInMano.setVisible(false);

        scopriESposta(mazzo,  cartaInMano, imgNuovaCarta, 1000);
    }

    public void pescaAltriGiocatori(ImageView mazzo, ImageView destinazione){
        destinazione.setVisible(false);

        sposta(mazzo, destinazione, 750);
    }

    private void sposta(ImageView carta, ImageView destinazione, int durata){
        ImageView copia = copyImageView(carta);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(durata), copia);

        translateTransition.setFromX(0);
        translateTransition.setFromY(0);

        double x = destinazione.getLayoutX() - copia.getLayoutX();
        double y = destinazione.getLayoutY() - copia.getLayoutY();

        translateTransition.setToX(x);
        translateTransition.setToY(y);

        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                carta.setVisible(true);
                destinazione.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

        translateTransition.setCycleCount(1);
        translateTransition.play();
    }

    private void scopriESposta(ImageView carta, ImageView destinazione, Image imgNuovaCarta, int durata){
        ImageView copia = copyImageView(carta);
        copia.setRotationAxis(new Point3D(0,1,0));

        SequentialTransition sequentialTransition = new SequentialTransition();
        ParallelTransition parallelTransition1 = new ParallelTransition(copia);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(durata/2), copia);
        translateTransition1.setFromX(0);
        translateTransition1.setFromY(0);
        double x = destinazione.getLayoutX() - copia.getLayoutX();
        double y = destinazione.getLayoutY() - copia.getLayoutY();
        translateTransition1.setToX(x/2);
        translateTransition1.setToY(y/2);

        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(durata/2), copia);
        rotateTransition1.setFromAngle(180);
        rotateTransition1.setToAngle(90);

        parallelTransition1.getChildren().addAll(translateTransition1, rotateTransition1);

        parallelTransition1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                copia.setImage(imgNuovaCarta);
            }
        });

        ParallelTransition parallelTransition2 = new ParallelTransition(copia);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(durata/2), copia);
        translateTransition2.setFromX(x/2);
        translateTransition2.setFromY(y/2);
        translateTransition2.setToX(x);
        translateTransition2.setToY(y);

        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(durata/2), copia);
        rotateTransition2.setFromAngle(90);
        rotateTransition2.setToAngle(0);

        parallelTransition2.getChildren().addAll(translateTransition2, rotateTransition2);

        sequentialTransition.getChildren().addAll(parallelTransition1, parallelTransition2);

        sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                carta.setVisible(true);
                destinazione.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

        sequentialTransition.setCycleCount(1);
        sequentialTransition.play();
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