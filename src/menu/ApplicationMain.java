package menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fileName = "menu.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest((e) -> {Platform.exit();System.exit(0);});
        primaryStage.show();
    }

    public static void loadMenu(String[] args) {
        launch(args);
    }
}