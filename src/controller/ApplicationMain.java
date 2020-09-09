package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationMain extends Application {
    static int nGiocatori;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fileName = "view" + nGiocatori + ".fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        primaryStage.setTitle("Briscola");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest((e) -> {Platform.exit();System.exit(0);});
        primaryStage.show();
    }

    public static void loadGame(String[] args,int nGiocatori) {
        ApplicationMain.nGiocatori = nGiocatori;
        launch(args);
    }
}