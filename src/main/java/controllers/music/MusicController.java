package controllers.music;

import controllers.common.Home;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicController implements Initializable {

    // MUSIC'S LENGTH ERROR POP-UP STAGE
    protected static Stage musicLengthErrorStage;

    // MUSIC'S TITLE ERROR POP-UP STAGE
    protected static Stage musicTitleErrorStage;

    // MUSIC'S ARTIST(S) ERROR POP-UP STAGE
    protected static Stage musicArtistErrorStage;

    // MUSIC ACTION SUCCESSFUL POP-UP STAGE
    protected static Stage musicActionSuccessStage;

    // MUSIC LIST PAGE STAGE
    protected static Stage listMusicStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    // MUSIC ACTION SUCCESSFUL POP-UP LABEL
    @FXML
    private Label musicActionSuccessLabel = new Label();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getMusicLengthErrorStage() {
        return musicLengthErrorStage;
    }

    public static void setMusicLengthErrorStage(Stage musicLengthErrorStage) {
        AddMusicController.musicLengthErrorStage = musicLengthErrorStage;
    }

    public static Stage getMusicTitleErrorStage() {
        return musicTitleErrorStage;
    }

    public static void setMusicTitleErrorStage(Stage musicTitleErrorStage) {
        MusicController.musicTitleErrorStage = musicTitleErrorStage;
    }

    public static Stage getMusicArtistErrorStage() {
        return musicArtistErrorStage;
    }

    public static void setMusicArtistErrorStage(Stage musicArtistErrorStage) {
        MusicController.musicArtistErrorStage = musicArtistErrorStage;
    }

    public static Stage getMusicActionSuccessStage() {
        return musicActionSuccessStage;
    }

    public static void setMusicActionSuccessStage(Stage musicActionSuccessStage) {
        MusicController.musicActionSuccessStage = musicActionSuccessStage;
    }

    public static Stage getListMusicStage() {
        return listMusicStage;
    }

    public static void setListMusicStage(Stage listMusicStage) {
        MusicController.listMusicStage = listMusicStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.musicActionSuccessLabel.setText("Votre musique a bien été " + PopUpUtils.getActionDone() + ".");
    }

    // MUSIC'S FILE SELECTION
    public String getFileSelected(ActionEvent actionEvent) {
        FileChooser musicFileChooser = new FileChooser();
        musicFileChooser.setTitle("Sélection de la musique");
        musicFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        File musicFile = musicFileChooser.showOpenDialog(Home.getHomeStage());

        if (musicFile != null) {
            return musicFile.getAbsolutePath();
        }
        return "";
    }

    // MUSIC'S LENGTH ERROR POP-UP "OK" BUTTON CLICKED
    public void musicLengthErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicLengthErrorStage().close();
    }

    // MUSIC'S TITLE ERROR POP-UP "OK" BUTTON CLICKED
    public void musicTitleErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicTitleErrorStage().close();
    }

    // MUSIC'S ARTIST(S) ERROR POP-UP "OK" BUTTON CLICKED
    public void musicArtistErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicArtistErrorStage().close();
    }

    // MUSIC ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void musicActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getMusicActionSuccessStage().close();

        Stage homeStage = Home.getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/music/listMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/listMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des musiques"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}