package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        InformationsUtils informationsUtils = new InformationsUtils();

        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        primaryStage.setTitle("WebMusic " + informationsUtils.getVersionApplication());
        primaryStage.setScene(new Scene(root, 375, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
