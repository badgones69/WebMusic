package controllers.music;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AuteurDb;
import enums.TypeAction;
import enums.TypeSource;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modal.ActionSuccessModal;
import modal.MusicErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;
import utils.InformationsUtils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicController {

    private static final Logger LOG = LogManager.getLogger(MusicController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // MUSIC'S TITLE ERROR POP-UP STAGE
    protected static Stage musicTitleErrorStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getMusicTitleErrorStage() {
        return musicTitleErrorStage;
    }

    public static void setMusicTitleErrorStage(Stage musicTitleErrorStage) {
        MusicController.musicTitleErrorStage = musicTitleErrorStage;
    }

    // MUSIC'S FILE SELECTION
    public String getFileSelected() {
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
    public void musicTitleErrorCloseButtonClicked() {
        getMusicTitleErrorStage().close();
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

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.MUSIC, action);
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
