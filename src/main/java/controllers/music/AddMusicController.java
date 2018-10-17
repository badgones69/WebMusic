package controllers.music;

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
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import org.controlsfx.control.ListSelectionView;
import utils.DaoTestsUtils;
import utils.FormUtils;

import java.net.URL;
import java.text.Collator;
import java.util.*;

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

        // "album" FIELD INITIALIZATION
        AlbumDao albumDao = new AlbumDao();
        List<String> albumValues = new ArrayList<>();
        List<AlbumDb> listAlbums = albumDao.findAll();

        for (AlbumDb albumDb : listAlbums) {
            albumValues.add(albumDb.getTitreAlbum());
        }

        Collections.sort(albumValues, Collator.getInstance(new Locale("fr")));

        this.album.getItems().clear();
        this.album.getItems().add(null);
        this.album.getItems().addAll(albumValues);
        this.album.setValue(null);

        // "artistes" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des artistes");
        sourceLabel.setStyle("-fx-font-weight:bold");
        this.artistes.setSourceHeader(sourceLabel);
        AuteurDao auteurDao = new AuteurDao();
        ObservableList<Label> auteurSourceValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursValues = auteurDao.findAll();
        List<String> auteursInString = new ArrayList<>();
        for (AuteurDb auteurDb : listAuteursValues) {
            auteursInString.add(
                auteurDb.getPrenomAuteur() != null ?
                        (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                        auteurDb.getNomAuteur()
            );
        }

        Collections.sort(auteursInString, Collator.getInstance(new Locale("fr")));

        for (String auteur : auteursInString) {
            auteurSourceValues.add(new Label(auteur));
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

        Boolean titreInvalide = "".equals(titre.getText());
        Boolean dureeInvalide = !FormUtils.dureeMusiqueIsValid(duree.getText());
        Boolean artistesInvalides = artistes.getTargetItems().size() == 0;

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (dureeInvalide) {
            super.showLengthErrorPopUp();
        }

        if (artistesInvalides) {
            super.showArtistErrorPopUp();
        }

        if (Boolean.FALSE.equals(titreInvalide) && Boolean.FALSE.equals(dureeInvalide) && Boolean.FALSE.equals(artistesInvalides)) {
            MusiqueDb musique = new MusiqueDb();
            AlbumDb albumMusique = new AlbumDb();

            if (album.getValue() != null) {
                albumMusique.setTitreAlbum(album.getValue());
                DaoTestsUtils.setNumeroToAlbum(albumMusique);
            }

            musique.setTitreMusique(this.titre.getText());
            musique.setDureeMusique(this.duree.getText());
            musique.setDateActionMusique(this.dateInsertion.getText());
            musique.setNomFichierMusique(this.nomFichier.getText());
            musique.setAlbumMusique(albumMusique);
            musique.setListeAuteurs(super.setArtistsToMusic(this.artistes.getTargetItems()));

            MusiqueDao musiqueDao = new MusiqueDao();
            musiqueDao.insert(musique);

            super.showSuccessPopUp("ajoutée");
        }
    }

    // MUSIC ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}