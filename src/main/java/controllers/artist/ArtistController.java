package controllers.artist;

import controllers.common.Home;
import controllers.music.ListMusicController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArtistController implements Initializable {

    // ARTIST'S NAME ERROR POP-UP STAGE
    protected static Stage artistNameErrorStage;

    // ARTIST ACTION SUCCESSFUL POP-UP STAGE
    protected static Stage artistActionSuccessStage;

    // ARTIST LIST PAGE STAGE
    protected static Stage listArtistStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    // ARTIST ACTION SUCCESSFUL POP-UP LABEL
    @FXML
    private Label artistActionSuccessLabel = new Label();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getArtistNameErrorStage() {
        return artistNameErrorStage;
    }

    public static void setArtistNameErrorStage(Stage artistNameErrorStage) {
        ArtistController.artistNameErrorStage = artistNameErrorStage;
    }

    public static Stage getArtistActionSuccessStage() {
        return artistActionSuccessStage;
    }

    public static void setArtistActionSuccessStage(Stage artistActionSuccessStage) {
        ArtistController.artistActionSuccessStage = artistActionSuccessStage;
    }

    public static Stage getListArtistStage() {
        return listArtistStage;
    }

    public static void setListArtistStage(Stage listArtistStage) {
        ArtistController.listArtistStage = listArtistStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.artistActionSuccessLabel.setText("Votre artiste a bien été " + PopUpUtils.getActionDone() + ".");
    }

    // ARTIST'S NAME ERROR POP-UP "OK" BUTTON CLICKED
    public void artistNameErrorCloseButtonClicked(ActionEvent actionEvent) {
        getArtistNameErrorStage().close();
    }

    // ARTIST ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void artistActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getArtistActionSuccessStage().close();

        Stage homeStage = Home.getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/artist/listArtist.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/artist/listArtist.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des artistes"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
