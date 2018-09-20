package controllers.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class Home extends javafx.application.Application {

    // HOME PAGE STAGE
    private static Stage homeStage;

    // APP CLOSING CONFIRMATION POP-UP STAGE
    private static Stage appCloseConfirmationStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * APP LAUNCHING
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getHomeStage() {
        return Home.homeStage;
    }

    private void setHomeStage(Stage stage) {
        Home.homeStage = stage;
    }

    public static Stage getAppCloseConfirmationStage() {
        return appCloseConfirmationStage;
    }

    public void setAppCloseConfirmationStage(Stage appCloseConfirmationStage) {
        Home.appCloseConfirmationStage = appCloseConfirmationStage;
    }

    // APPLICATION STARTING
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/common/home.fxml"));
        primaryStage.setTitle(informationsUtils.buildStageTitleBar(primaryStage, "Accueil"));
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
            appCloseConfirmationStage.show();
        });
    }

    private Stage initializeAppClose() {
        Stage stage = new Stage();

        try {
            Parent appCloseConfirmationParent = FXMLLoader.load(getClass().getResource("/views/common/appCloseConfirmation.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Fermeture"));
            stage.setScene(new Scene(appCloseConfirmationParent, 650, 140));
            this.setAppCloseConfirmationStage(stage);

            return stage;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}