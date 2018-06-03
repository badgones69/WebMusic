package controllers.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class Main extends Application {

    // HOME PAGE STAGE
    private static Stage homeStage;

    // APP CLOSING CONFIRMATION POP-UP STAGE
    private static Stage appCloseConfirmationStage;

    /**
     * APP LAUNCHING
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        InformationsUtils informationsUtils = new InformationsUtils();

        Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        primaryStage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - Accueil");
        primaryStage.setScene(new Scene(root));
        this.setHomeStage(primaryStage);
        this.initializeAppClose();
        this.configurateAppClose();
        primaryStage.show();
    }

    /**
     * APP CLOSING CONFIRMATION POP-UP CONFIGURATION AND INITIALIZATION
     */

    private void configurateAppClose() {
        getHomeStage().setOnCloseRequest(event -> {
            event.consume();
            Stage appCloseConfirmationStage = this.initializeAppClose();
            appCloseConfirmationStage.show();
        });
    }

    private Stage initializeAppClose() {
        Stage stage = new Stage();

        try {
            Parent appCloseConfirmationParent = FXMLLoader.load(getClass().getResource("/views/appCloseConfirmation.fxml"));
            stage.setTitle("WebMusic " + new InformationsUtils().getVersionApplication() + " - Fermeture");
            stage.setScene(new Scene(appCloseConfirmationParent, 650, 140));
            this.setAppCloseConfirmationStage(stage);

            return stage;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getHomeStage() {
        return Main.homeStage;
    }

    private void setHomeStage(Stage stage) {
        Main.homeStage = stage;
    }

    public static Stage getAppCloseConfirmationStage() {
        return appCloseConfirmationStage;
    }

    public void setAppCloseConfirmationStage(Stage appCloseConfirmationStage) {
        Main.appCloseConfirmationStage = appCloseConfirmationStage;
    }
}
