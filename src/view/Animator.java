package view;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.SequenceInputStream;
import java.security.spec.RSAOtherPrimeInfo;
import java.time.temporal.Temporal;
import java.util.Collections;

public class Animator {

    private Pane tavola;

    private Point2D[] DESTS_PRENDI = new Point2D[4];
    private int[] posGiocatori;

    private final int TEMPO_DISTRIBUISCI = 500;
    private final int TEMPO_PESCA = 500;
    private final int TEMPO_PRENDI_CARTE = 500;
    private final int TEMPO_SCOPRI = 400;
    private final int TEMPO_SCOPRI_BRISCOLA = 1750;
    public final int DELAY_PESCA = 500;
    private final int FADE_OUT_SCHERMATA_INIZIALE = 250;

    public Animator(Pane tavola, int nGiocatori){
        this.tavola = tavola;

        this.DESTS_PRENDI[0]= new Point2D(tavola.getPrefWidth()/2, tavola.getPrefHeight()+150);
        this.DESTS_PRENDI[1]= new Point2D(tavola.getPrefWidth() + 150, tavola.getPrefHeight()/2);
        this.DESTS_PRENDI[2]= new Point2D(tavola.getPrefWidth()/2, -150);
        this.DESTS_PRENDI[3]= new Point2D(-150, tavola.getPrefHeight()/2);

        this.posGiocatori = new int[nGiocatori];
        posGiocatori[0] = 0;
        switch (this.posGiocatori.length){
            case 2:
                posGiocatori[1] = 2;
                break;
            case 3:
                posGiocatori[1] = 2;
                posGiocatori[2] = 3;
                break;
            case 4:
                posGiocatori[1] = 1;
                posGiocatori[2] = 2;
                posGiocatori[3] = 3;
                break;
        }
    }
    


    public void pescaBriscola(ImageView briscola, ImageView cartaInMano){
        briscola.setVisible(false);
        cartaInMano.setVisible(false);

        sposta(briscola, cartaInMano, TEMPO_DISTRIBUISCI, -90, 0);
    }

    public void pescaBriscolaAltriGiocatori(ImageView briscola, ImageView schiena, int id){
        briscola.setVisible(false);
        schiena.setVisible(false);

        Image imgSchiena = null;
        try {
            imgSchiena = new Image(new FileInputStream("src/carte/schiena.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch(id){
            case 1: case 3:
                scopriESpostaFrontale(briscola, schiena, imgSchiena, TEMPO_PESCA);
                break;
            case 2:
                scopriESpostaLaterale(briscola, schiena, imgSchiena, TEMPO_PESCA);
                break;
        }
    }

    public void mettiInTavola(ImageView carta, ImageView cartaInTavola){
        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        sposta(carta, cartaInTavola, TEMPO_DISTRIBUISCI, 0, 0);
    }

    public void mettiInTavolaAltriGiocatori(ImageView carta, ImageView cartaInTavola, Image imgNuovaCarta, int id){
        carta.setVisible(false);
        cartaInTavola.setVisible(false);

        switch (id) {
            case 2:
                scopriESpostaFrontale(carta, cartaInTavola, imgNuovaCarta, TEMPO_DISTRIBUISCI);
                break;
            case 1: case 3:
                scopriESpostaLaterale(carta, cartaInTavola, imgNuovaCarta, TEMPO_DISTRIBUISCI);
                break;
        }
    }

    public void prendiCarte(ImageView[] carteInTavola, int id){
        for(ImageView c : carteInTavola){
            c.setVisible(false);
            ImageView dest = new ImageView();
            dest.setLayoutX(DESTS_PRENDI[id].getX());
            dest.setLayoutY(DESTS_PRENDI[id].getY());
            sposta(c, dest, TEMPO_PRENDI_CARTE,0,0);
        }
    }

    public void pescaCarta(ImageView mazzo, ImageView cartaInMano, Image imgNuovaCarta){
        cartaInMano.setVisible(false);

        scopriESpostaFrontale(mazzo,  cartaInMano, imgNuovaCarta, TEMPO_PESCA);
    }

    public void pescaAltriGiocatori(ImageView mazzo, ImageView destinazione, int id){
        destinazione.setVisible(false);

        switch(id){
            case 1:
                sposta(mazzo, destinazione, TEMPO_PESCA, 0, 90);
                break;
            case 2:
                sposta(mazzo, destinazione, TEMPO_PESCA, 0, 0);
                break;
            case 3:
                sposta(mazzo, destinazione, TEMPO_PESCA, 0, -90);
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

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(durata), copia);
        translateTransition.setFromX(0);
        translateTransition.setFromY(0);
        double x = destinazione.getLayoutX() - copia.getLayoutX();
        double y = destinazione.getLayoutY() - copia.getLayoutY();
        translateTransition.setToX(x);
        translateTransition.setToY(y);

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(durata - TEMPO_SCOPRI), copia);

        RotateTransition scopri1 = new RotateTransition(Duration.millis(TEMPO_SCOPRI/2), copia);
        scopri1.setAxis(Rotate.Y_AXIS);
        scopri1.setFromAngle(180);
        scopri1.setToAngle(90);

        scopri1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                copia.setImage(imgNuovaCarta);
            }
        });

        ParallelTransition parallelTransition = new ParallelTransition(copia);

        RotateTransition scopri2 = new RotateTransition(Duration.millis(TEMPO_SCOPRI/2), copia);
        scopri2.setAxis(Rotate.Y_AXIS);
        scopri2.setFromAngle(90);
        scopri2.setToAngle(0);

        sequentialTransition.getChildren().addAll(rotateTransition, scopri1, scopri2);

        parallelTransition.getChildren().addAll(translateTransition, sequentialTransition);

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

    private void scopriESpostaLaterale(ImageView carta, ImageView destinazione, Image imgNuovaCarta, int durata){
        ImageView copia = copyImageView(carta);

        SequentialTransition sequentialTransition = new SequentialTransition();
        ParallelTransition parallelTransition = new ParallelTransition();

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(durata), copia);
        translateTransition1.setFromX(0);
        translateTransition1.setFromY(0);
        double x = destinazione.getLayoutX() - copia.getLayoutX();
        double y = destinazione.getLayoutY() - copia.getLayoutY();
        translateTransition1.setToX(x);
        translateTransition1.setToY(y);

        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(durata - TEMPO_SCOPRI), copia);
        rotateTransition1.setAxis(Rotate.Z_AXIS);
        rotateTransition1.setFromAngle(90);
        rotateTransition1.setToAngle(0);

        RotateTransition rotateTransition2 = new RotateTransition(Duration.millis(TEMPO_SCOPRI), copia);
        rotateTransition2.setAxis(Rotate.Y_AXIS);
        rotateTransition2.setFromAngle(180);
        rotateTransition2.setToAngle(270);

        rotateTransition2.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                copia.setImage(imgNuovaCarta);
            }
        });

        RotateTransition rotateTransition3 = new RotateTransition(Duration.millis(TEMPO_SCOPRI), copia);
        rotateTransition3.setFromAngle(270);
        rotateTransition3.setToAngle(360);
        rotateTransition3.setAxis(Rotate.Y_AXIS);

        sequentialTransition.getChildren().addAll(rotateTransition1, rotateTransition2, rotateTransition3);
        parallelTransition.getChildren().addAll(sequentialTransition, translateTransition1);

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

    public void scopriBriscola(ImageView mazzo, ImageView briscola, Image imgBriscola){
        briscola.setVisible(false);
        ImageView copia = copyImageView(mazzo);

        SequentialTransition sequentialTranslate = new SequentialTransition();
        SequentialTransition sequentialRotate = new SequentialTransition();
        ParallelTransition parallelTransition = new ParallelTransition();

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(TEMPO_SCOPRI_BRISCOLA/2), copia);
        translateTransition1.setFromX(0);
        translateTransition1.setToX(-200);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(TEMPO_SCOPRI_BRISCOLA/2), copia);
        translateTransition2.setFromX(-200);
        translateTransition2.setFromY(0);
        translateTransition2.setToX(briscola.getLayoutX() - copia.getLayoutX());
        translateTransition2.setToY(briscola.getLayoutY() - copia.getLayoutY());

        sequentialTranslate.getChildren().addAll(translateTransition1, translateTransition2);

        RotateTransition scopri1 = new RotateTransition(Duration.millis(TEMPO_SCOPRI_BRISCOLA*2/10), copia);
        scopri1.setAxis(Rotate.Y_AXIS);
        scopri1.setFromAngle(180);
        scopri1.setToAngle(270);

        scopri1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                copia.setImage(imgBriscola);

                ObservableList<Node> workingCollection = FXCollections.observableArrayList(tavola.getChildren());
                Collections.swap(workingCollection, tavola.getChildren().indexOf(copia), tavola.getChildren().indexOf(mazzo));
                tavola.getChildren().setAll(workingCollection);
            }
        });

        RotateTransition scopri2 = new RotateTransition(Duration.millis(TEMPO_SCOPRI_BRISCOLA*2/10), copia);
        scopri2.setFromAngle(270);
        scopri2.setToAngle(360);
        scopri2.setAxis(Rotate.Y_AXIS);

        RotateTransition rotateTransition1 = new RotateTransition(Duration.millis(TEMPO_SCOPRI_BRISCOLA*6/10), copia);
        rotateTransition1.setAxis(Rotate.Z_AXIS);
        rotateTransition1.setFromAngle(0);
        rotateTransition1.setToAngle(-90);

        sequentialRotate.getChildren().addAll(scopri1, scopri2, rotateTransition1);
        parallelTransition.getChildren().addAll(sequentialTranslate, sequentialRotate);

        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                briscola.setVisible(true);
                tavola.getChildren().remove(copia);
            }
        });

        parallelTransition.setCycleCount(1);
        parallelTransition.play();
    }

    public void cambiaTurno(int idTurno, Ellipse[] e){
        int attesa = TEMPO_PESCA;

        for(int i = 0; i < e.length; i++){
            if (i == idTurno)
                new SequentialTransition(new StrokeTransition(Duration.millis(attesa), e[i]),new StrokeTransition(Duration.millis(10), e[i], (Color) e[i].getStroke(), Color.LIGHTGREEN)).play();
            else
                new SequentialTransition(new StrokeTransition(Duration.millis(attesa), e[i]),new StrokeTransition(Duration.millis(10), e[i], (Color) e[i].getStroke(), Color.rgb(161, 92, 19))).play();
        }
    }

    public void rimuoviSchermataIniziale(Pane schermataIniziale){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(FADE_OUT_SCHERMATA_INIZIALE), schermataIniziale);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent actionEvent) {
                tavola.getChildren().remove(schermataIniziale);
            }
        });

        fadeTransition.play();
    }

    public void schermataFinale(Pane schermata, Text testoPunti, int punti){
        testoPunti.setText("Punti: " + punti);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(FADE_OUT_SCHERMATA_INIZIALE), schermata);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.play();
    }

    private ImageView copyImageView(ImageView iv){
        ImageView newIv = new ImageView();
        newIv.setImage(iv.getImage());
        newIv.setRotate(iv.getRotate());
        newIv.setRotationAxis(iv.getRotationAxis());
        newIv.setLayoutX(iv.getLayoutX());
        newIv.setLayoutY(iv.getLayoutY());
        newIv.setFitWidth(iv.getFitWidth());
        newIv.setFitHeight(iv.getFitHeight());
        tavola.getChildren().add(newIv);
        return newIv;
    }

    public int getPosGiocatori(int i){
        return posGiocatori[i];
    }
}