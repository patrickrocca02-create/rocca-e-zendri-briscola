package controller;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import view.View;

import java.net.InetAddress;

public class Controller {

    private Client giocatore;
    private  View view;

    @FXML private ImageView cartaInTavolaClient;
    @FXML private ImageView briscola;
    @FXML private ImageView cartaInTavolaAvversaria;
    @FXML private ImageView carta2;
    @FXML private ImageView carta3;
    @FXML private ImageView carta1;
    @FXML private ImageView schiena1;
    @FXML private ImageView schiena2;
    @FXML private ImageView schiena3;
    @FXML private ImageView mazzo;

    @FXML
    void initialize(){
        ImageView[] carteInTavola = {cartaInTavolaClient, cartaInTavolaAvversaria};
        ImageView[] carteInMano = {carta1, carta2, carta3};
        ImageView[] schieneAvversarie = {schiena1, schiena2,schiena3};
        view = new View(carteInTavola, briscola,carteInMano,schieneAvversarie,mazzo);

        try {
            giocatore = new Client(InetAddress.getLocalHost(), 2200, view);
        }catch (Exception e){ e.printStackTrace(); }

        System.out.println("CONNESSO");
        giocatore.start();
    }

    @FXML
    void onClickCarta1(MouseEvent event) {
        if(giocatore.isTurno()) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(0);
        }
    }

    @FXML
    void onClickCarta2(MouseEvent event) {
        if(giocatore.isTurno()) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(1);
        }
    }

    @FXML
    void onClickCarta3(MouseEvent event) {
        if(giocatore.isTurno()) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(2);
        }
    }
}
