package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.InformationsUtils;

import java.io.IOException;

public class HomeController {

    private static Stage aboutStage;

    public void aboutItemClicked(ActionEvent actionEvent) {
        InformationsUtils informationsUtils = new InformationsUtils();
        Stage stage = new Stage();
        this.setAboutStage(stage);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/about.fxml"));
            stage.setTitle("WebMusic " + informationsUtils.getVersionApplication() + " - À propos");
            stage.setScene(new Scene(root, 380, 140));
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
}
