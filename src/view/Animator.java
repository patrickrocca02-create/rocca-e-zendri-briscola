package view;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Animator {

    private Pane tavola;

    private Point2D[] DESTS_PRENDI = new Point2D[4];

    public Animator(Pane tavola){
        this.tavola = tavola;
        this.DESTS_PRENDI[0]= new Point2D(tavola.getPrefWidth()/2, tavola.getPrefHeight()+150);
        this.DESTS_PRENDI[1]= new Point2D(tavola.getPrefWidth() + 150, tavola.getPrefHeight()/2);
        this.DESTS_PRENDI[2]= new Point2D(tavola.getPrefWidth()/2, -150);
        this.DESTS_PRENDI[3]= new Point2D(-150, tavola.getPrefHeight()/2);
    }

    public void mettiInTavola(ImageView carta, ImageView cartainTavola){
        carta.setVisible(false);
        cartainTavola.setVisible(false);

        sposta(carta, cartainTavola, 750, 0, 0);
    }

    public void mettiInTavolaAltriGiocatori(ImageView carta, ImageView cartaInTavola, Image imgNuovaCarta, int id){
        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        switch (id) {
            case 2:
                scopriESpostaFrontale(carta, cartaInTavola, imgNuovaCarta, 1000);
                break;
            case 1: case 3:
                scopriESpostaLaterale(carta, cartaInTavola, imgNuovaCarta, 1000);
                break;
        }
    }

    public void prendiCarte(ImageView[] carteInTavola, int id){
        for(ImageView c : carteInTavola){
            c.setVisible(false);
            ImageView dest = new ImageView();
            dest.setLayoutX(DESTS_PRENDI[id].getX());
            dest.setLayoutY(DESTS_PRENDI[id].getY());
            sposta(c, dest, 750,0,0);
        }
    }

    public void pescaCarta(ImageView mazzo, ImageView cartaInMano, Image imgNuovaCarta){
        cartaInMano.setVisible(false);

        scopriESpostaFrontale(mazzo,  cartaInMano, imgNuovaCarta, 1000);
    }

    public void pescaAltriGiocatori(ImageView mazzo, ImageView destinazione, int id){
        destinazione.setVisible(false);

        switch(id){
            case 1:
                sposta(mazzo, destinazione, 750, 0, 90);
                break;
            case 2:
                sposta(mazzo, destinazione, 750, 0, 0);
                break;
            case 3:
                sposta(mazzo, destinazione, 750, 0, -90);
                break;
        }
    }

    private void sposta(ImageView carta, ImageView destinazione, int durata, int fromAngle, int toAngle){
        ImageView copia = copyImageView(carta);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(durata), copia);

        translateTransition.setFromX(0);
        translateTransition.setFromY(0);

        double x = destinazione.getLayoutX() - copia.getLayoutX();
        double y = destinazione.getLayoutY() - copia.getLayoutY();

        translateTransition.setToX(x);
        translateTransition.setToY(y);

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(durata),copia);
        rotateTransition.setFromAngle(fromAngle);
        rotateTransition.setToAngle(toAngle);

        ParallelTransition parallelTransition = new ParallelTransition(copia);
        parallelTransition.getChildren().addAll(translateTransition,rotateTransition);

        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                carta.setVisible(true);
                destinazione.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

        parallelTransition.setCycleCount(1);
        parallelTransition.play();
    }

    private void scopriESpostaFrontale(ImageView carta, ImageView destinazione, Image imgNuovaCarta, int durata){
        ImageView copia = copyImageView(carta);

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
        rotateTransition1.setAxis(new Point3D(0,1,0));
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
        rotateTransition2.setAxis(new Point3D(0,1,0));

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

    private void scopriESpostaLaterale(ImageView carta, ImageView destinazione, Image imgNuovaCarta, int durata){
        ImageView copia = copyImageView(carta);

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
        rotateTransition1.setAxis(new Point3D(1,0,0));
        rotateTransition1.setFromAngle(0);
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
        rotateTransition2.setToAngle(180);
        rotateTransition2.setAxis(new Point3D(0,1,0));

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
        newIv.getTransforms().add(new Rotate(iv.getRotate(), iv.getRotationAxis()));
        newIv.setLayoutX(iv.getLayoutX());
        newIv.setLayoutY(iv.getLayoutY());
        newIv.setFitWidth(iv.getFitWidth());
        newIv.setFitHeight(iv.getFitHeight());
        tavola.getChildren().add(newIv);
        return newIv;
    }
}