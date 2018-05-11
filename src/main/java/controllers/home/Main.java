package controllers.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class Main extends Application {

    private static Stage homeStage;
    private static Stage appCloseConfirmationStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        InformationsUtils informationsUtils = new InformationsUtils();

        Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        primaryStage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - Accueil");
        primaryStage.setScene(new Scene(root));
        this.setHomeStage(primaryStage);
        this.configurateAppClose();
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

    public static Stage getAppCloseConfirmationStage() {
        return appCloseConfirmationStage;
    }

    public void setAppCloseConfirmationStage(Stage appCloseConfirmationStage) {
        Main.appCloseConfirmationStage = appCloseConfirmationStage;
    }

    private void configurateAppClose() {
        getHomeStage().setOnCloseRequest(event -> {
            event.consume();

            Stage stage = new Stage();

            try {
                Parent appCloseConfirmationParent = FXMLLoader.load(getClass().getResource("/views/appCloseConfirmation.fxml"));
                stage.setTitle("WebMusic " + new InformationsUtils().getVersionApplication() + " - Fermeture");
                stage.setScene(new Scene(appCloseConfirmationParent, 550, 140));
                this.setAppCloseConfirmationStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
