package controllers.music;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AuteurDb;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modal.MusicErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MusicController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(MusicController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // MUSIC'S TITLE ERROR POP-UP STAGE
    protected static Stage musicTitleErrorStage;

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

    public static Stage getMusicTitleErrorStage() {
        return musicTitleErrorStage;
    }

    public static void setMusicTitleErrorStage(Stage musicTitleErrorStage) {
        MusicController.musicTitleErrorStage = musicTitleErrorStage;
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
        File musicFile = musicFileChooser.showOpenDialog(new Home().getHomeStage());

        if (musicFile != null) {
            return musicFile.getAbsolutePath();
        }
        return "";
    }

    // MUSIC'S TITLE ERROR POP-UP "OK" BUTTON CLICKED
    public void musicTitleErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicTitleErrorStage().close();
    }

    // MUSIC ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void musicActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getMusicActionSuccessStage().close();

        Stage homeStage = new Home().getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/music/listMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/listMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des musiques"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected List<AuteurDb> setArtistsToMusic(ObservableList<Label> listArtists) {
        List<AuteurDb> artistesMusique = new ArrayList<>();
        for (int i = 0; i < listArtists.size(); i++) {
            AuteurDb artiste = new AuteurDb();
            String identiteArtiste = listArtists.get(i).getText();
            Integer indexSeparateurIdentite = identiteArtiste.indexOf(' ');

            // ARTIST(S) NAME RETRIEVING (TO KNOW IF WHITESPACES CONTAINING)
            Boolean auteurHasWhitespacedPseudo = Boolean.FALSE;

            if (indexSeparateurIdentite != -1) {
                try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                        "auteur", "nomAuteur", identiteArtiste.substring(indexSeparateurIdentite).trim()))) {
                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (resultSet.isClosed()) {
                            auteurHasWhitespacedPseudo = Boolean.TRUE;
                        }
                    }
                } catch (SQLException e) {
                    LOG.error(IO_EXCEPTION + e.getMessage(), e);
                }
            }

            if (indexSeparateurIdentite == -1 || Boolean.TRUE.equals(auteurHasWhitespacedPseudo)) {
                artiste.setNomAuteur(identiteArtiste);
            } else {
                artiste.setNomAuteur(identiteArtiste.substring(indexSeparateurIdentite).trim());
                artiste.setPrenomAuteur(identiteArtiste.substring(0, indexSeparateurIdentite));
            }

            DaoTestsUtils.setIdentifiantToAuteur(artiste);
            artistesMusique.add(artiste);
        }
        return artistesMusique;
    }

    protected void showSuccessPopUp(String action) {
        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone(action);
            MusicController musicController = new MusicController();
            musicController.initialize(getClass().getResource("/views/music/musicActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/musicActionSuccess.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 390, 140));
            this.setMusicActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showArtistErrorPopUp() {
        MusicErrorModal.getMusicArtistErrorAlert();
    }

    protected void showLengthErrorPopUp() {
        MusicErrorModal.getMusicLengthErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/errors/musicTitleError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 330, 140));
            this.setMusicTitleErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
