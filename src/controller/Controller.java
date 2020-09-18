package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import view.View;

public class Controller {

    private Client giocatore;
    private View view;

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

    @FXML private Ellipse ellisse1 = null;
    @FXML private Ellipse ellisse2 = null;
    @FXML private Ellipse ellisse3 = null;
    @FXML private Ellipse ellisse4 = null;

    @FXML private Pane schermataIniziale = null;
    @FXML private Pane schermataVittoria = null;
    @FXML private Pane schermataSconfitta = null;
    @FXML private Pane schermataPareggio = null;
    @FXML private Pane schermataCarteCompagno = null;

    @FXML private ImageView cartaCompagno1 = null;
    @FXML private ImageView cartaCompagno2 = null;
    @FXML private ImageView cartaCompagno3 = null;
    @FXML private Text testoCarteCompagno = null;

    @FXML private Text puntiVittoria = null;
    @FXML private Text puntiSconfitta = null;
    @FXML private Text puntiPareggio = null;

    @FXML private Text testoIpServer;
    @FXML private Text testoPorta;

    @FXML private Text username1 = null;
    @FXML private Text username2 = null;
    @FXML private Text username3 = null;
    @FXML private Text username4 = null;

    @FXML
    void initialize(){
        ImageView[] carteInMano = {carta1, carta2, carta3};

        ImageView[] carteInTavolaTot = {cartaInTavola1, cartaInTavola2, cartaInTavola3,cartaInTavola4};
        int len = 0;
        for (ImageView carta : carteInTavolaTot) {
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

        Ellipse[] ellissiTot = {ellisse1, ellisse2, ellisse3,ellisse4};
        len = 0;
        for (Ellipse e : ellissiTot) {
            if (e != null)
                len++;
        }
        Ellipse[] ellissiGiocatori = new Ellipse[len];
        for (int i = 0; i < len; i++)
            ellissiGiocatori[i] = ellissiTot[i];

        Text[] usernamesTot = {username1, username2, username3, username4};
        len = 0;
        for(Text username : usernamesTot)
            if(username != null)
                len++;
        Text[] usernames = new Text[len];
        for (int i = 0; i < len; i++)
            usernames[i] = usernamesTot[i];

        Pane[] schermate = {schermataIniziale, schermataVittoria, schermataSconfitta, schermataPareggio, schermataCarteCompagno};
        Text[] testi = {puntiVittoria, puntiSconfitta, puntiPareggio, testoIpServer, testoPorta, testoCarteCompagno};
        ImageView[] carteCompagno = {cartaCompagno1, cartaCompagno2, cartaCompagno3};

        view = new View(tavola, carteInTavola, briscola,carteInMano,schiene,mazzo, usernames, ellissiGiocatori, schermate, testi, carteCompagno);

        giocatore = ApplicationMain.client;
        giocatore.setView(view);

        view.primaSchermata(ApplicationMain.indirizzo.getHostAddress(), ApplicationMain.porta);
    }

    @FXML
    void onClickCarta1(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(0).getCarta() != null && (Color) ellisse1.getStroke() == Color.LIGHTGREEN) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(0);
        }
    }

    @FXML
    void onClickCarta2(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(1).getCarta() != null && (Color) ellisse1.getStroke() == Color.LIGHTGREEN) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(1);
        }
    }

    @FXML
    void onClickCarta3(MouseEvent event) {
        if(giocatore.isTurno() && view.getCarta(2).getCarta() != null && (Color) ellisse1.getStroke() == Color.LIGHTGREEN) {
            giocatore.setTurno(false);
            giocatore.inviaCarta(2);
        }
    }

    @FXML
    void onReleaseCarteCompagno(MouseEvent event){
        view.nascondiCarteCompagno();
    }
}
