package controllers.common;

import database.SQLiteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modal.generic.confirmation.CommonConfirmationModal;
import utils.LogUtils;
import utils.InformationsUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Home extends Application {

    // HOME PAGE STAGE
    private static Stage homeStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * APP LAUNCHING
     */

    public static void main(String[] args) {
        initializeDB();
        launch(args);
    }

    public static void initializeDB() {
        InputStream inputStream = SQLiteConnection.class.getResourceAsStream("/WebMusic.db");

        File file = new File(SQLiteConnection.getPathDb());
        Path target = file.toPath();

        if (!file.exists()) {
            try {
                Files.copy(inputStream, target);
            } catch (IOException e) {
                LogUtils.generateIOExceptionLog(Home.class, e);
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

    // APPLICATION STARTING
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/common/home.fxml"));
        primaryStage.setTitle(informationsUtils.buildStageTitleBar(primaryStage, "Accueil"));
        primaryStage.setScene(new Scene(root));
        setHomeStage(primaryStage);
        this.configurateAppClose();
        primaryStage.show();
    }

    /**
     * APP CLOSING CONFIRMATION POP-UP CONFIGURATION AND INITIALIZATION
     */

    private void configurateAppClose() {
        getHomeStage().setOnCloseRequest(event -> {
            event.consume();
            CommonConfirmationModal.getAppCloseConfirmationAlert();
        });
    }
}
