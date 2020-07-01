package controller;


import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import view.View;

import java.net.InetAddress;

public class Controller {

    private Client giocatore;

    @FXML
    private ImageView cartaInTavolaClient;
    @FXML
    private ImageView briscola;
    @FXML
    private ImageView cartaInTavolaAvversaria;
    @FXML
    private ImageView carta2;
    @FXML
    private ImageView carta3;
    @FXML
    private ImageView carta1;


    @FXML
    void initialize(){
        View view = new View(cartaInTavolaClient, cartaInTavolaAvversaria, briscola, carta1, carta2, carta3);
        try {
            giocatore = new Client(InetAddress.getLocalHost(), 2201, view);
        }catch (Exception e){}
        System.out.println("CONNESSO");
        giocatore.start();
    }

    @FXML
    void onClickCarta1(MouseEvent event) {
        System.out.println("Carta 1 clickata");
        giocatore.inviaCarta(0);
    }

    @FXML
    void onClickCarta2(MouseEvent event) {
        giocatore.inviaCarta(1);
    }

    @FXML
    void onClickCarta3(MouseEvent event) {
        giocatore.inviaCarta(2);
    }

}
