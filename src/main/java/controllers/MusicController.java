package controllers;

import controllers.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;
import utils.InformationsUtils;
import utils.WindowUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicController implements Initializable {

    // MUSIC's LENGTH ERROR POP-UP STAGE
    protected static Stage musicLengthErrorStage;

    // MUSIC's ARTIST(S) ERROR POP-UP STAGE
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.musicActionSuccessLabel.setText("Votre musique a bien été " + WindowUtils.getActionDone() + ".");
    }

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

    // MUSIC ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void musicActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getMusicActionSuccessStage().close();

        Stage homeStage = Main.getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/listMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/listMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitle("Liste des musiques"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
