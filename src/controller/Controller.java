package controller;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import view.Animator;
import view.View;

import java.net.InetAddress;

public class Controller {

    private Client giocatore;
    private View view;
    private Animator animator;

    @FXML private Pane tavola;

    @FXML private ImageView briscola;
    @FXML private ImageView carta1;
    @FXML private ImageView carta2;
    @FXML private ImageView carta3;
    @FXML private ImageView mazzo;

    @FXML private ImageView cartaInTavola1 = null;
    @FXML private ImageView cartaInTavola2 = null;
    @FXML private ImageView cartaInTavola3 = null;
    @FXML private ImageView cartaInTavola4 = null;

    @FXML private ImageView schiena1 = null;
    @FXML private ImageView schiena2 = null;
    @FXML private ImageView schiena3 = null;
    @FXML private ImageView schiena4 = null;
    @FXML private ImageView schiena5 = null;
    @FXML private ImageView schiena6 = null;
    @FXML private ImageView schiena7 = null;
    @FXML private ImageView schiena8 = null;
    @FXML private ImageView schiena9 = null;

    @FXML
    void initialize(){
        ImageView[] carteInMano = {carta1, carta2, carta3};

        ImageView[] carteInTavolaTot = {cartaInTavola1, cartaInTavola2, cartaInTavola3,cartaInTavola4};
        int len = 0;
        for (ImageView carta : carteInTavolaTot) {
            System.out.println(carta);
            if (carta != null)
                len++;
        }
        ImageView[] carteInTavola = new ImageView[len];
        for (int i = 0; i < len; i++) {
            carteInTavola[i] = carteInTavolaTot[i];
        }

        ImageView[] schieneTot = {schiena1, schiena2, schiena3,schiena4,schiena5,schiena6,schiena7,schiena8,schiena9};
        len = 0;
        for (ImageView carta : schieneTot)
            if(carta != null)
                len++;
        ImageView[][] schiene = new ImageView[len/3][3];
        int k = 0;
        for (int i = 0; i < len/3; i++) {
            for (int j = 0; j < 3; j++) {
                schiene[i][j] = schieneTot[k++];
            }
        }

        view = new View(carteInTavola, briscola,carteInMano,schiene,mazzo);
        animator = new Animator(carteInTavola, tavola, view);

        try {
            giocatore = new Client(InetAddress.getLocalHost(), 2201, view);
        }catch (Exception e){ e.printStackTrace(); }

        System.out.println("CONNESSO");
        giocatore.start();
    }

    @FXML
    void onClickCarta1(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(0).getCarta() != null) {
            giocatore.setTurno(false);
            animator.mettiInTavola(carta1);
            giocatore.inviaCarta(0);
        }
    }

    @FXML
    void onClickCarta2(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(1).getCarta() != null) {
            giocatore.setTurno(false);
            animator.mettiInTavola(carta2);
            giocatore.inviaCarta(1);
        }
    }

    @FXML
    void onClickCarta3(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(2).getCarta() != null) {
            giocatore.setTurno(false);
            animator.mettiInTavola(carta3);
            giocatore.inviaCarta(2);
        }
    }
}
