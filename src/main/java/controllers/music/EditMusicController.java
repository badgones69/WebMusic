package controllers.music;

import dao.AuteurDao;
import dao.MusiqueDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import dto.MusiqueDto;
import enums.TypeAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import listeners.music.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import modal.generic.confirmation.AlbumConfirmationModal;
import utils.FormUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class EditMusicController extends MusicController implements Initializable {

    private AlbumDb currentAlbumSelected;

    /**
     * MUSIC EDITING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField duree = new TextField();
    @FXML
    protected TextField dateModification = new TextField();
    @FXML
    protected TextField nomFichier = new TextField();
    @FXML
    protected HBox albumHBox = new HBox();
    @FXML
    protected TextField textFieldAlbumSelected = new TextField();
    @FXML
    protected Button updatingChoice = new Button();
    @FXML
    protected ListView<Label> source = new ListView<>();
    @FXML
    protected ListView<Label> target = new ListView<>();
    @FXML
    protected Button addArtist = new Button();
    @FXML
    protected Button removeArtist = new Button();
    @FXML
    protected Button addAllArtist = new Button();
    @FXML
    protected Button removeAllArtist = new Button();

    /**
     * MUSIC EDITING FORM CONTAINERS
     */

    @FXML
    BorderPane editMusicBorderPane = new BorderPane();
    @FXML
    VBox editMusicVBox = new VBox();

    /**
     * MUSIC EDITING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        editMusicBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        editMusicBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        editMusicVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editMusicVBox.setPrefHeight(editMusicBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {
        MusiqueDto musiqueDto = ListMusicSelectionListener.getMusiqueSelected();
        MusiqueDb musiqueDb = MusiqueMapper.toDb(musiqueDto);

        // "titre" FIELD INITIALIZATION
        this.titre.setText(musiqueDb.getTitreMusique());

        // "durée" FIELD INITIALIZATION
        this.duree.setText(musiqueDb.getDureeMusique());

        // "nomFichier" FIELD INITIALIZATION
        this.nomFichier.setText(musiqueDb.getNomFichierMusique());

        // "dateModification" FIELD INITIALIZATION
        this.dateModification.setText(FormUtils.getCurrentDate());

        // "album" FIELD INITIALIZATION
        if (musiqueDb.getAlbumMusique() != null) {
            this.currentAlbumSelected = musiqueDb.getAlbumMusique();
        } else {
            this.currentAlbumSelected = new AlbumDb();
        }
        this.textFieldAlbumSelected.setText(currentAlbumSelected.toString());

        this.updatingChoice.setOnAction(action -> {
            AlbumDb newAlbumSelected = AlbumConfirmationModal.getAlbumSelectChoiceConfirmationAlert(this.currentAlbumSelected);

            if(newAlbumSelected.getNumeroAlbum() != null) {
                this.textFieldAlbumSelected.setText(newAlbumSelected.toString());
            } else {
                this.textFieldAlbumSelected.setText("");
            }
            this.currentAlbumSelected = newAlbumSelected;
        });

        // "artistes" PICKLIST INITIALIZATION
        AuteurDao auteurDao = new AuteurDao();
        ObservableList<Label> auteurSourceValues = FXCollections.observableArrayList();
        ObservableList<Label> auteurTargetValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursSourceValues = auteurDao.findAll();
        listAuteursSourceValues.removeAll(musiqueDb.getListeAuteurs());

        List<String> auteursSourceInString = new ArrayList<>();
        for (AuteurDb auteurDb : listAuteursSourceValues) {
            auteursSourceInString.add(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            );
        }

        Collections.sort(auteursSourceInString, FormUtils.getFrenchCollator());

        for (String auteur : auteursSourceInString) {
            auteurSourceValues.add(new Label(auteur));
        }

        List<AuteurDb> listAuteursTargetValues = musiqueDb.getListeAuteurs();
        List<String> auteursTargetInString = new ArrayList<>();
        for (AuteurDb auteurDb : listAuteursTargetValues) {
            auteursTargetInString.add(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            );
        }

        Collections.sort(auteursTargetInString, FormUtils.getFrenchCollator());

        for (String auteur : auteursTargetInString) {
            auteurTargetValues.add(new Label(auteur));
        }

        this.source.getItems().addAll(auteurSourceValues);
        this.target.getItems().addAll(auteurTargetValues);

        this.source.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.target.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.addArtist.setOnAction((ActionEvent event) -> {
            auteurSourceValues.removeAll(source.getSelectionModel().getSelectedItems());
            auteurTargetValues.addAll(source.getSelectionModel().getSelectedItems());
            auteurTargetValues.sort((artist1, artist2) -> artist1.getText().compareToIgnoreCase(artist2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(auteurSourceValues);
            this.target.getItems().clear();
            this.target.getItems().addAll(auteurTargetValues);
        });

        this.addAllArtist.setOnAction((ActionEvent event) -> {
            auteurTargetValues.addAll(auteurSourceValues);
            auteurSourceValues.clear();
            auteurTargetValues.sort((artist1, artist2) -> artist1.getText().compareToIgnoreCase(artist2.getText()));
            this.source.getItems().clear();
            this.target.getItems().clear();
            this.target.getItems().addAll(auteurTargetValues);
        });

        this.removeArtist.setOnAction((ActionEvent event) -> {
            auteurTargetValues.removeAll(target.getSelectionModel().getSelectedItems());
            auteurSourceValues.addAll(target.getSelectionModel().getSelectedItems());
            auteurSourceValues.sort((artist1, artist2) -> artist1.getText().compareToIgnoreCase(artist2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(auteurSourceValues);
            this.target.getItems().clear();
            this.target.getItems().addAll(auteurTargetValues);
        });

        this.removeAllArtist.setOnAction((ActionEvent event) -> {
            auteurSourceValues.addAll(auteurTargetValues);
            auteurTargetValues.clear();
            auteurSourceValues.sort((artist1, artist2) -> artist1.getText().compareToIgnoreCase(artist2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(auteurSourceValues);
            this.target.getItems().clear();
        });
    }

    // MUSIC SELECTION FILE CHOOSER OPENING
    public void openMusicFileChooser() {
        this.nomFichier.setText(super.getFileSelected());
    }

    // MUSIC EDITING FORM VALIDATION AND SENDING
    public void validForm() {

        boolean titreInvalide = "".equals(titre.getText());
        boolean dureeInvalide = !FormUtils.dureeMusiqueIsValid(duree.getText());
        boolean artistesInvalides = this.target.getItems().isEmpty();

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (dureeInvalide) {
            super.showLengthErrorPopUp();
        }

        if (artistesInvalides) {
            super.showArtistErrorPopUp();
        }

        if (!titreInvalide && !dureeInvalide && !artistesInvalides) {
            MusiqueDb musique = new MusiqueDb();
            AlbumDb albumMusique = new AlbumDb();

            if (!"".equals(this.textFieldAlbumSelected.getText())) {
                albumMusique = currentAlbumSelected;
            }

            musique.setCodeMusique(ListMusicSelectionListener.getMusiqueSelected().getCodeMusique());
            musique.setTitreMusique(this.titre.getText());
            musique.setDureeMusique(this.duree.getText());
            musique.setDateActionMusique(this.dateModification.getText());
            musique.setNomFichierMusique(this.nomFichier.getText());
            musique.setAlbumMusique(albumMusique);
            musique.setListeAuteurs(super.setArtistsToMusic(this.target.getItems()));

            MusiqueDao musiqueDao = new MusiqueDao();
            musiqueDao.update(musique);

            super.showSuccessPopUp(TypeAction.EDIT);
        }
    }

    // MUSIC EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
