package controllers.music;

import dao.AlbumDao;
import dao.AuteurDao;
import dao.MusiqueDao;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import dto.MusiqueDto;
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
import listeners.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import org.controlsfx.control.ListSelectionView;
import utils.DaoTestsUtils;
import utils.FormUtils;

import java.net.URL;
import java.text.Collator;
import java.util.*;

public class EditMusicController extends MusicController implements Initializable {

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
    protected ComboBox<String> album = new ComboBox<>();
    @FXML
    protected ListSelectionView<Label> artistes = new ListSelectionView<>();

    /**
     * MUSIC ADDING FORM CONTAINERS
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

        if (musiqueDb.getAlbumMusique() != null) {
            this.album.setValue(musiqueDb.getAlbumMusique().getTitreAlbum());
        } else {
            this.album.setValue(null);
        }
        // "artistes" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des artistes");
        sourceLabel.setStyle("-fx-font-weight:bold");
        this.artistes.setSourceHeader(sourceLabel);
        AuteurDao auteurDao = new AuteurDao();
        ObservableList<Label> auteurSourceValues = FXCollections.observableArrayList();
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

        Collections.sort(auteursSourceInString, Collator.getInstance(new Locale("fr")));

        for (String auteur : auteursSourceInString) {
            auteurSourceValues.add(new Label(auteur));
        }

        this.artistes.getSourceItems().clear();
        this.artistes.getSourceItems().addAll(auteurSourceValues);

        Label targetLabel = new Label("Artiste(s) de la musique");
        targetLabel.setStyle("-fx-font-weight:bold");
        this.artistes.setTargetHeader(targetLabel);
        ObservableList<Label> auteurTargetValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursTargetValues = musiqueDb.getListeAuteurs();
        List<String> auteursTargetInString = new ArrayList<>();
        for (AuteurDb auteurDb : listAuteursTargetValues) {
            auteursTargetInString.add(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            );
        }

        Collections.sort(auteursTargetInString, Collator.getInstance(new Locale("fr")));

        for (String auteur : auteursTargetInString) {
            auteurTargetValues.add(new Label(auteur));
        }
        this.artistes.getTargetItems().clear();
        this.artistes.getTargetItems().addAll(auteurTargetValues);
    }

    // MUSIC SELECTION FILE CHOOSER OPENING
    public void openMusicFileChooser(ActionEvent actionEvent) {
        this.nomFichier.setText(super.getFileSelected(actionEvent));
    }

    // MUSIC EDITING FORM VALIDATION AND SENDING
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

            if (this.album.getValue() != null) {
                albumMusique.setTitreAlbum(this.album.getValue());
                DaoTestsUtils.setNumeroToAlbum(albumMusique);
            }

            musique.setCodeMusique(ListMusicSelectionListener.getMusiqueSelected().getCodeMusique());
            musique.setTitreMusique(this.titre.getText());
            musique.setDureeMusique(this.duree.getText());
            musique.setDateActionMusique(this.dateModification.getText());
            musique.setNomFichierMusique(this.nomFichier.getText());
            musique.setAlbumMusique(albumMusique);
            musique.setListeAuteurs(super.setArtistsToMusic(this.artistes.getTargetItems()));

            MusiqueDao musiqueDao = new MusiqueDao();
            musiqueDao.update(musique);

            super.showSuccessPopUp("modifiée");
        }
    }

    // MUSIC EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
