package menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import model.Server;

import java.net.InetAddress;

public class Controller {

    @FXML private Pane menuCreaPartita;
    @FXML private Pane menuJoinPartita;

    @FXML private Button btnCrea;
    @FXML private TextField labelNomeCrea;

    @FXML private ToggleButton btn2Giocatori;
    @FXML private ToggleButton btn3Giocatori;
    @FXML private ToggleButton btn4Giocatori;

    @FXML private Button btnUnisciti;
    @FXML private TextField labelNomeUnisciti;
    @FXML private TextField labelPorta;
    @FXML private TextField labelIpv4;

    private ToggleButton[] btnsNGiocatori;
    private int nGiocatori;

    private boolean nGiocatoriScelti = false;
    private boolean nomeCreaScelto = false;
    private boolean portaScelta = false;
    private boolean nomeJoinScelto = false;
    private boolean ipScelto = false;

    @FXML
    void initialize(){
        btnsNGiocatori = new ToggleButton[]{btn2Giocatori, btn3Giocatori, btn4Giocatori};
    }

    @FXML
    void onClick2Giocatori(MouseEvent event) {
        for(ToggleButton btn : btnsNGiocatori)
            btn.setSelected(false);
        btn2Giocatori.setSelected(true);

        nGiocatoriScelti = true;
        if(nomeCreaScelto)
            btnCrea.setDisable(false);

        nGiocatori = 2;
    }

    @FXML
    void onClick3Giocatori(MouseEvent event) {
        for(ToggleButton btn : btnsNGiocatori)
            btn.setSelected(false);
        btn3Giocatori.setSelected(true);

        nGiocatoriScelti = true;
        if(nomeCreaScelto)
            btnCrea.setDisable(false);

        nGiocatori = 3;
    }

    @FXML
    void onClick4Giocatori(MouseEvent event) {
        for(ToggleButton btn : btnsNGiocatori)
            btn.setSelected(false);
        btn4Giocatori.setSelected(true);

        nGiocatoriScelti = true;
        if(nomeCreaScelto)
            btnCrea.setDisable(false);

        nGiocatori = 4;
    }

    @FXML
    void onClickCrea(MouseEvent event) {
        String nome = labelNomeCrea.getText();

        Server s = new Server(nGiocatori);

        new Thread(() -> {
            s.start();
        }).start();

        controller.ApplicationMain.loadGame(new String[0], s.getPorta(), s.getIpv4());
    }

    @FXML
    void onClickJoinPartita(MouseEvent event) {
        menuCreaPartita.setVisible(false);
        menuJoinPartita.setVisible(true);
    }

    @FXML
    void onClickNuovaPartita(MouseEvent event) {
        menuJoinPartita.setVisible(false);
        menuCreaPartita.setVisible(true);
    }

    @FXML
    void onClickUnisciti(MouseEvent event) {
        try {
            controller.ApplicationMain.loadGame(new String[0], Integer.valueOf(labelPorta.getText()), InetAddress.getByName(labelIpv4.getText()));
        }catch (Exception e){}
    }

    @FXML
    void onTextChangeNomeCrea(KeyEvent event) {
        if(labelNomeCrea.getText().length() > 2){
            labelNomeUnisciti.setText(labelNomeCrea.getText());
            nomeJoinScelto = true;
            if(portaScelta && ipScelto)
                btnUnisciti.setDisable(false);

            nomeCreaScelto = true;
            if(nGiocatoriScelti)
                btnCrea.setDisable(false);
        }else{
            nomeCreaScelto = false;
            btnCrea.setDisable(true);
        }
    }

    @FXML
    void onTextChangeNomeJoin(KeyEvent event) {
        if(labelNomeUnisciti.getText().length() > 2){
            labelNomeCrea.setText(labelNomeUnisciti.getText());
            nomeCreaScelto = true;
            if(nGiocatoriScelti)
                btnCrea.setDisable(false);

            nomeJoinScelto = true;
            if(portaScelta && ipScelto)
                btnUnisciti.setDisable(false);
        }else{
            nomeJoinScelto = false;
            btnUnisciti.setDisable(true);
        }
    }

    @FXML
    void onTextChangePorta(KeyEvent event) {
        try {
            if (Integer.valueOf(labelPorta.getText()) >= Server.MIN_PORTA && Integer.valueOf(labelPorta.getText()) < Server.MAX_PORTA) {
                portaScelta = true;
                if (nomeJoinScelto && ipScelto)
                    btnUnisciti.setDisable(false);
            } else {
                portaScelta = false;
                btnUnisciti.setDisable(true);
            }
        }catch (Exception e){
            portaScelta = false;
            btnUnisciti.setDisable(true);
        }
    }

    @FXML
    void onTextChangeIpv4(KeyEvent event) {
        if(validIP(labelIpv4.getText())){
            ipScelto = true;
            if(nomeJoinScelto && portaScelta)
                btnUnisciti.setDisable(false);
        }else{
            ipScelto = false;
            btnUnisciti.setDisable(true);
        }
    }

    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
