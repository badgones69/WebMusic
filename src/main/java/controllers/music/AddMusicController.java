package controllers.music;

import dao.AuteurDao;
import dao.MusiqueDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import enums.TypeAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import modal.editable.AlbumModal;
import modal.generic.confirmation.AlbumConfirmationModal;
import utils.FormUtils;
import utils.StyleUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AddMusicController extends MusicController implements Initializable {

    private AlbumDb currentAlbumSelected;

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
    protected HBox albumHBox = new HBox();
    protected TextField textFieldAlbumSelected = new TextField();
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
     * MUSIC ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addMusicBorderPane = new BorderPane();
    @FXML
    VBox addMusicVBox = new VBox();

    /**
     * MUSIC ADDING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        addMusicBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        addMusicBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        addMusicVBox.setPrefWidth(primaryScreenBounds.getWidth());
        addMusicVBox.setPrefHeight(addMusicBorderPane.getPrefHeight() - 32);
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

        // "artistes" PICKLIST INITIALIZATION
        AuteurDao auteurDao = new AuteurDao();
        ObservableList<Label> auteurSourceValues = FXCollections.observableArrayList();
        ObservableList<Label> auteurTargetValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursValues = auteurDao.findAll();
        List<String> auteursInString = new ArrayList<>();

        for (AuteurDb auteurDb : listAuteursValues) {
            auteursInString.add(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            );
        }

        Collections.sort(auteursInString, FormUtils.getFrenchCollator());

        for (String auteur : auteursInString) {
            auteurSourceValues.add(new Label(auteur));
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

    // MUSIC SELECTION FILE CHOOSER OPENING
    public void openNewAlbumAddingModal() {
        AlbumModal albumModal = new AlbumModal();
        AlbumModal.setCurrentAlbumPreSelected(new AlbumDb());
        this.currentAlbumSelected = albumModal.getAddNewAlbumAlert();

        if(this.currentAlbumSelected.getTitreAlbum() != null) {
            albumHBox.getChildren().clear();

            this.textFieldAlbumSelected.setDisable(true);
            this.textFieldAlbumSelected.getStylesheets().add(StyleUtils.getStylesheet());
            this.textFieldAlbumSelected.getStyleClass().add(StyleUtils.getFormFieldClass());

            Button updatingChoice = new Button("Modifier");
            updatingChoice.setMnemonicParsing(false);
            updatingChoice.getStylesheets().add(StyleUtils.getStylesheet());
            updatingChoice.getStyleClass().add(StyleUtils.getFormFieldClass());

            updatingChoice.setOnAction(action -> {
                AlbumDb newAlbumAdded = AlbumConfirmationModal.getAlbumSelectChoiceConfirmationAlert(this.currentAlbumSelected);

                if (newAlbumAdded.getNumeroAlbum() != null) {
                    this.textFieldAlbumSelected.setText(newAlbumAdded.toString());
                } else {
                    this.textFieldAlbumSelected.setText("");
                }
                this.currentAlbumSelected = newAlbumAdded;
            });

            albumHBox.getChildren().addAll(this.textFieldAlbumSelected, updatingChoice);

            if (this.currentAlbumSelected.getNumeroAlbum() != null) {
                this.textFieldAlbumSelected.setText(this.currentAlbumSelected.toString());
            } else {
                this.textFieldAlbumSelected.setText("");
            }
        }
    }

    public void openExistingAlbumSelectingModal() {
        AlbumModal albumModal = new AlbumModal();
        AlbumModal.setCurrentAlbumPreSelected(new AlbumDb());
        this.currentAlbumSelected = albumModal.getSelectAlbumAlert();

        if(this.currentAlbumSelected.getTitreAlbum() != null) {
            albumHBox.getChildren().clear();

            this.textFieldAlbumSelected.setDisable(true);
            this.textFieldAlbumSelected.getStylesheets().add(StyleUtils.getStylesheet());
            this.textFieldAlbumSelected.getStyleClass().add(StyleUtils.getFormFieldClass());

            Button updatingChoice = new Button("Modifier");
            updatingChoice.setMnemonicParsing(false);
            updatingChoice.getStylesheets().add(StyleUtils.getStylesheet());
            updatingChoice.getStyleClass().add(StyleUtils.getFormFieldClass());

            updatingChoice.setOnAction(action -> {
                AlbumDb newAlbumSelected = AlbumConfirmationModal.getAlbumSelectChoiceConfirmationAlert(this.currentAlbumSelected);

                if (newAlbumSelected.getNumeroAlbum() != null) {
                    this.textFieldAlbumSelected.setText(newAlbumSelected.toString());
                } else {
                    this.textFieldAlbumSelected.setText("");
                }
                this.currentAlbumSelected = newAlbumSelected;
            });

            albumHBox.getChildren().addAll(this.textFieldAlbumSelected, updatingChoice);

            if (this.currentAlbumSelected.getNumeroAlbum() != null) {
                this.textFieldAlbumSelected.setText(this.currentAlbumSelected.toString());
            } else {
                this.textFieldAlbumSelected.setText("");
            }
        }
    }

    // MUSIC ADDING FORM VALIDATION AND SENDING
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

            musique.setTitreMusique(this.titre.getText());
            musique.setDureeMusique(this.duree.getText());
            musique.setDateActionMusique(this.dateInsertion.getText());
            musique.setNomFichierMusique(this.nomFichier.getText());
            musique.setAlbumMusique(this.currentAlbumSelected);
            musique.setListeAuteurs(super.setArtistsToMusic(this.target.getItems()));

            MusiqueDao musiqueDao = new MusiqueDao();
            musiqueDao.insert(musique);

            super.showSuccessPopUp(TypeAction.ADD);
        }
    }

    // MUSIC ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}