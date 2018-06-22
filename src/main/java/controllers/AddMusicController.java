package controllers;

import dao.AlbumDao;
import dao.AuteurDao;
import dao.MusiqueDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.DaoTestsUtils;
import utils.FormUtils;
import utils.WindowUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMusicController extends MusicController implements Initializable {

    /**
     * MUSIC ADDING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeForm();
    }

    private void initializeForm() {

        // "titre" FIELD INITIALIZATION
        super.titre.clear();

        // "durée" FIELD INITIALIZATION
        super.duree.clear();

        // "nomFichier" FIELD INITIALIZATION
        super.nomFichier.clear();

        // "dateInsertion" FIELD INITIALIZATION
        super.dateInsertion.setText(FormUtils.getCurrentDate());

        // "album" FIELD INITIALIZATION
        AlbumDao albumDao = new AlbumDao();
        List<String> albumValues = new ArrayList<>();
        List<AlbumDb> listAlbums = albumDao.findAll();

        for (AlbumDb albumDb : listAlbums) {
            albumValues.add(albumDb.getTitreAlbum());
        }

        super.album.getItems().clear();
        super.album.getItems().addAll(albumValues);
        super.album.setValue(null);

        // "artistes" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des artistes");
        sourceLabel.setStyle("-fx-font-weight:bold");
        super.artistes.setSourceHeader(sourceLabel);
        AuteurDao auteurDao = new AuteurDao();
        ObservableList<Label> auteurSourceValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursValues = auteurDao.findAll();
        for (AuteurDb auteurDb : listAuteursValues) {
            auteurSourceValues.add(new Label(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            ));
        }

        super.artistes.getSourceItems().clear();
        super.artistes.getSourceItems().addAll(auteurSourceValues);

        Label targetLabel = new Label("Artiste(s) de la musique");
        targetLabel.setStyle("-fx-font-weight:bold");
        super.artistes.setTargetHeader(targetLabel);
        super.artistes.getTargetItems().clear();
    }

    // MUSIC ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        if (!FormUtils.dureeMusiqueIsValid(duree.getText())) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicLengthError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitle(null));
                stage.setScene(new Scene(root, 330, 140));
                super.setMusicLengthErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (artistes.getTargetItems().size() == 0) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicArtistError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitle(null));
                stage.setScene(new Scene(root, 415, 140));
                super.setMusicArtistErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MusiqueDb musique = new MusiqueDb();
            AlbumDb albumMusique = new AlbumDb();
            List<AuteurDb> artistesMusique = new ArrayList<>();

            if (album.getValue() != null) {
                albumMusique.setTitreAlbum(album.getValue());
                DaoTestsUtils.setNumeroToAlbum(albumMusique);
            }

            musique.setTitreMusique(super.titre.getText());
            musique.setDureeMusique(super.duree.getText());
            musique.setDateInsertionMusique(super.dateInsertion.getText());
            musique.setNomFichierMusique(super.nomFichier.getText());
            musique.setAlbumMusique(albumMusique);

            for (int i = 0; i < super.artistes.getTargetItems().size(); i++) {
                AuteurDb artiste = new AuteurDb();
                String identiteArtiste = super.artistes.getTargetItems().get(i).getText();
                Integer indexSeparateurIdentite = identiteArtiste.indexOf(" ");

                if (indexSeparateurIdentite == -1) {
                    artiste.setNomAuteur(identiteArtiste);
                } else {
                    artiste.setNomAuteur(identiteArtiste.substring(indexSeparateurIdentite).trim());
                    artiste.setPrenomAuteur(identiteArtiste.substring(0, indexSeparateurIdentite));
                }

                DaoTestsUtils.setIdentifiantToAuteur(artiste);
                artistesMusique.add(artiste);
            }
            musique.setListeAuteurs(artistesMusique);

            MusiqueDao musiqueDao = new MusiqueDao();
            musiqueDao.insert(musique);

            Stage stage = new Stage();

            try {
                WindowUtils.setActionDone("ajoutée");
                MusicController musicController = new MusicController();
                musicController.initialize(getClass().getResource("/views/musicActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitle(null));
                stage.setScene(new Scene(root, 390, 140));
                super.setMusicActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // MUSIC ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}