package controllers;

import dao.AlbumDao;
import dao.AuteurDao;
import dao.MusiqueDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;
import utils.DaoTestsUtils;
import utils.FormUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMusicController extends MusicController implements Initializable {

    /**
     * MUSIC ADDING FORM FIELDS
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

    /**
     * MUSIC ADDING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeForm();
    }

    private void initializeForm() {

        // "titre" FIELD INITIALIZATION
        this.titre.clear();

        // "durée" FIELD INITIALIZATION
        this.duree.clear();

        // "nomFichier" FIELD INITIALIZATION
        this.nomFichier.clear();

        // "dateInsertion" FIELD INITIALIZATION
        this.dateInsertion.setText(FormUtils.getCurrentDate());

        // "album" FIELD INITIALIZATION
        AlbumDao albumDao = new AlbumDao();
        List<String> albumValues = new ArrayList<>();
        List<AlbumDb> listAlbums = albumDao.findAll();

        for (AlbumDb albumDb : listAlbums) {
            albumValues.add(albumDb.getTitreAlbum());
        }

        this.album.getItems().clear();
        this.album.getItems().addAll(albumValues);
        this.album.setValue(null);

        // "artistes" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des artistes");
        sourceLabel.setStyle("-fx-font-weight:bold");
        this.artistes.setSourceHeader(sourceLabel);
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

        this.artistes.getSourceItems().clear();
        this.artistes.getSourceItems().addAll(auteurSourceValues);

        Label targetLabel = new Label("Artiste(s) de la musique");
        targetLabel.setStyle("-fx-font-weight:bold");
        this.artistes.setTargetHeader(targetLabel);
        this.artistes.getTargetItems().clear();
    }

    // MUSIC SELECTION FILE CHOOSER OPENING
    public void openMusicFileChooser(ActionEvent actionEvent) {
        this.nomFichier.setText(super.getFileSelected(actionEvent));
    }

    // MUSIC ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        if (!FormUtils.dureeMusiqueIsValid(duree.getText())) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicLengthError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 330, 140));
                this.setMusicLengthErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (artistes.getTargetItems().size() == 0) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicArtistError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 415, 140));
                this.setMusicArtistErrorStage(stage);
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

            musique.setTitreMusique(this.titre.getText());
            musique.setDureeMusique(this.duree.getText());
            musique.setDateInsertionMusique(this.dateInsertion.getText());
            musique.setNomFichierMusique(this.nomFichier.getText());
            musique.setAlbumMusique(albumMusique);

            for (int i = 0; i < this.artistes.getTargetItems().size(); i++) {
                AuteurDb artiste = new AuteurDb();
                String identiteArtiste = this.artistes.getTargetItems().get(i).getText();
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
                PopUpUtils.setActionDone("ajoutée");
                MusicController musicController = new MusicController();
                musicController.initialize(getClass().getResource("/views/musicActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                this.setMusicActionSuccessStage(stage);
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