package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    static int nGiocatori;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fileName = "view" + nGiocatori + ".fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        primaryStage.setTitle("Briscola");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void loadGame(String[] args,int nGiocatori) {
        ClientMain.nGiocatori = nGiocatori;
        launch(args);
    }

    public static void main(String[] args){
        loadGame(args,2);
    }
}
