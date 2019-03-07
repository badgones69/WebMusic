package controllers.common;

import database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Home extends Application {

    private static final Logger LOG = LogManager.getLogger(Home.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // HOME PAGE STAGE
    private static Stage homeStage;

    // APP CLOSING CONFIRMATION POP-UP STAGE
    private static Stage appCloseConfirmationStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * APP LAUNCHING
     */

    public static void main(String[] args) {
        initializeDB();
        launch(args);
    }

    public static void initializeDB() {
        InputStream inputStream = SQLiteConnection.class.getResourceAsStream("/WebMusicTest.db");

        File file = new File(SQLiteConnection.getPathDb());
        Path target = file.toPath();

        if (!file.exists()) {
            try {
                Files.copy(inputStream, target);
            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    public Stage getHomeStage() {
        return homeStage;
    }

    private static void setHomeStage(Stage stage) {
        homeStage = stage;
    }

    public Stage getAppCloseConfirmationStage() {
        return appCloseConfirmationStage;
    }

    private static void setAppCloseConfirmationStage(Stage stage) {
        appCloseConfirmationStage = stage;
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
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
            return null;
        }
    }
}
