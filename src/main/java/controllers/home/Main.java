package controllers.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.InformationsUtils;

public class Main extends Application {

    private static Stage homeStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.setHomeStage(primaryStage);

        InformationsUtils informationsUtils = new InformationsUtils();

        Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        primaryStage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - Accueil");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setHomeStage(Stage stage) {
        Main.homeStage = stage;
    }

    public static Stage getHomeStage() {
        return Main.homeStage;
    }

}
