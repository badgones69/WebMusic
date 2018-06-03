package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;
import utils.InformationsUtils;

import java.io.File;

public class MusicController {

    // MUSIC's LENGTH ERROR POP-UP STAGE
    protected static Stage musicLengthErrorStage;

    // MUSIC's ARTIST(S) ERROR POP-UP STAGE
    protected static Stage musicArtistErrorStage;

    // MUSIC FORM VALIDATION SUCCESSFUL POP-UP STAGE
    protected static Stage musicValidationSuccessStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * MUSIC FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();

    @FXML
    protected TextField duree = new TextField();

    @FXML
    protected TextField dateInsertion = new TextField();

    @FXML
    protected TextField nomFichier = new TextField();

    @FXML
    protected ComboBox<String> album = new ComboBox<>();

    @FXML
    protected ListSelectionView<Label> artistes = new ListSelectionView<>();

    // MUSIC SELECTION FILE CHOOSER OPENING
    public void openMusicFileChooser(ActionEvent actionEvent) {
        FileChooser musicFileChooser = new FileChooser();
        musicFileChooser.setTitle("Sélection de la musique");
        musicFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        File musicFile = musicFileChooser.showOpenDialog(Main.getHomeStage());

        if (musicFile != null) {
            nomFichier.setText(musicFile.getAbsolutePath());
        }
    }

    // MUSIC's LENGTH ERROR POP-UP "OK" BUTTON CLICKED
    public void musicLengthErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicLengthErrorStage().close();
    }

    // MUSIC's ARTIST(S) ERROR POP-UP "OK" BUTTON CLICKED
    public void musicArtistErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicArtistErrorStage().close();
    }

    // MUSIC FORM VALIDATION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void musicValidationSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getMusicValidationSuccessStage().close();

        // TODO : Redirection vers la liste des musiques
    }

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getMusicLengthErrorStage() {
        return musicLengthErrorStage;
    }

    public static void setMusicLengthErrorStage(Stage musicLengthErrorStage) {
        AddMusicController.musicLengthErrorStage = musicLengthErrorStage;
    }

    public static Stage getMusicArtistErrorStage() {
        return musicArtistErrorStage;
    }

    public static void setMusicArtistErrorStage(Stage musicArtistErrorStage) {
        MusicController.musicArtistErrorStage = musicArtistErrorStage;
    }

    public static Stage getMusicValidationSuccessStage() {
        return musicValidationSuccessStage;
    }

    public static void setMusicValidationSuccessStage(Stage musicValidationSuccessStage) {
        MusicController.musicValidationSuccessStage = musicValidationSuccessStage;
    }
}
