package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class MenuController {

    // ABOUT POP-UP STAGE
    private static Stage aboutStage;

    private final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * ITEMS LISTENERS OF "WebMusic" MENU
     */

    public void appHomeItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - Accueil");
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
    }

    public void addMusicItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.show();

        try {
            AddMusicController addMusicController = new AddMusicController();
            addMusicController.initialize(getClass().getResource("/views/addMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/addMusic.fxml"));
            homeStage.setTitle(homeStage.getTitle().replace("Accueil", "Ajout d'une musique"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ITEMS LISTENERS OF "Album" MENU
     */

    public void addAlbumItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.setTitle(homeStage.getTitle().replace("Accueil", "Ajout d'un album"));
        homeStage.show();
    }

    public void editAlbumItemClicked(ActionEvent actionEvent) {
    }

    public void deleteAlbumItemClicked(ActionEvent actionEvent) {
    }

    /**
     * ITEM LISTENER OF "Aide" MENU
     */

    public void aboutItemClicked(ActionEvent actionEvent) {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/about.fxml"));
            stage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - À propos");
            stage.setScene(new Scene(root, 450, 140));
            this.setAboutStage(stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAboutStage() {
        return MenuController.aboutStage;
    }

    private void setAboutStage(Stage stage) {
        MenuController.aboutStage = stage;
    }
}
