package ihm.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.InformationsUtils;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.setPrimaryStage(primaryStage);

        Rectangle2D visualsBounds = Screen.getPrimary().getVisualBounds();

        InformationsUtils informationsUtils = new InformationsUtils();

        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        primaryStage.setTitle("WebMusic " + informationsUtils.getVersionApplication());
        primaryStage.setScene(new Scene(root, visualsBounds.getWidth(), visualsBounds.getHeight()-32));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

}
