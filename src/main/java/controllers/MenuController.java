package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    // ABOUT POP-UP STAGE
    private static Stage aboutStage;

    private final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * HOME PAGE CONTAINERS
     */

    @FXML
    BorderPane homeBorderPane = new BorderPane();
    @FXML
    Pane homePane = new Pane();
    @FXML
    VBox homeVBox = new VBox();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAboutStage() {
        return MenuController.aboutStage;
    }

    private void setAboutStage(Stage stage) {
        MenuController.aboutStage = stage;
    }

    /**
     * HOME PAGE CONTAINERS INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSizes();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        homeBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        homeBorderPane.setPrefHeight(primaryScreenBounds.getHeight()-32);
        homePane.setPrefWidth(primaryScreenBounds.getWidth());
        homePane.setPrefHeight(homeBorderPane.getPrefHeight()-32);
        homeVBox.setPrefWidth(primaryScreenBounds.getWidth());
        homeVBox.setPrefHeight(homeBorderPane.getPrefHeight()-32);
    }


    /**
     * ITEMS LISTENERS OF "WebMusic" MENU
     */

    public void appHomeItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Accueil"));
        try {
            homeStage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/views/home.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        homeStage.show();
    }

    public void appCloseItemClicked(ActionEvent actionEvent) {
        Stage appCloseConfirmationStage = Main.getAppCloseConfirmationStage();
        appCloseConfirmationStage.show();
    }

    /**
     * ITEMS LISTENERS OF "Musique" MENU
     */

    public void listMusicItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/listMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/listMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des musiques"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMusicItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.show();

        try {
            AddMusicController addMusicController = new AddMusicController();
            addMusicController.initialize(getClass().getResource("/views/addMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/addMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Ajout d'une musique"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ITEM LISTENER OF "Aide" MENU
     */

    public void aboutItemClicked(ActionEvent actionEvent) {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/about.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, "À propos"));
            stage.setScene(new Scene(root, 450, 140));
            this.setAboutStage(stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
