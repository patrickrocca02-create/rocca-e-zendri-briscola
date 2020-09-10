package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class ApplicationMain{
    public static int nGiocatori = 0;
    public static int porta;
    public static InetAddress indirizzo;
    public static Client client;

    public void start(){
        try {
            Stage partita = new Stage();
            String fileName = "view" + nGiocatori + ".fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fileName));
            partita.setTitle("Briscola");
            partita.setScene(new Scene(root));
            partita.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public static void loadGame(String[] args, int porta, InetAddress ipLocale) {
        ApplicationMain.porta = porta;
        ApplicationMain.indirizzo = ipLocale;

        client = new Client(ApplicationMain.indirizzo, ApplicationMain.porta);
        client.start();

        while(nGiocatori == 0)
            System.out.print("");
        System.out.println(porta);

        new ApplicationMain().start();
    }
}