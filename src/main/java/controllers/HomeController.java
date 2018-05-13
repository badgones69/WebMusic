package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class HomeController {

    private static Stage aboutStage;
    private final InformationsUtils informationsUtils = new InformationsUtils();

    public void aboutItemClicked(ActionEvent actionEvent) {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/about.fxml"));
            stage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - À propos");
            stage.setScene(new Scene(root, 380, 140));
            this.setAboutStage(stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAlbumItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.setTitle(homeStage.getTitle().replace("Accueil", "Ajout d'un album"));
        homeStage.show();
    }

    public void editAlbumItemClicked(ActionEvent actionEvent) {
    }

    public void deleteAlbumItemClicked(ActionEvent actionEvent) {
    }

    private void setAboutStage(Stage stage) {
        HomeController.aboutStage = stage;
    }

    public static Stage getAboutStage() {
        return HomeController.aboutStage;
    }

    public void appHomeItemClicked(ActionEvent actionEvent) {
        Stage homeStage = Main.getHomeStage();
        homeStage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - Accueil");
        homeStage.show();
    }

    public void appCloseItemClicked(ActionEvent actionEvent) {
        Stage appCloseConfirmationStage = Main.getAppCloseConfirmationStage();
        appCloseConfirmationStage.show();
    }
}
