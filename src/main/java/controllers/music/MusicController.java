package controllers.music;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AuteurDb;
import enums.TypeAction;
import enums.TypeSource;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import modal.generic.error.CommonErrorModal;
import modal.generic.error.MusicErrorModal;
import modal.generic.success.ActionSuccessModal;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;
import utils.LogUtils;
import utils.InformationsUtils;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

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
                    LogUtils.generateSQLExceptionLog(MusicController.class, e);
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
        ActionSuccessModal.getActionSuccessAlert(TypeSource.MUSIC, action, true);
    }

    protected void showArtistErrorPopUp() {
        MusicErrorModal.getMusicArtistErrorAlert();
    }

    protected void showLengthErrorPopUp() {
        MusicErrorModal.getMusicLengthErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        CommonErrorModal.getTitleErrorAlert(TypeSource.MUSIC);
    }
}
