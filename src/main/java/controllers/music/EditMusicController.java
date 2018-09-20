package controllers.music;

import dao.AlbumDao;
import dao.AuteurDao;
import dao.MusiqueDao;
import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import dto.MusiqueDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import listeners.listMusic.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import org.controlsfx.control.ListSelectionView;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;
import utils.FormUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
        editMusicBorderPane.setPrefHeight(primaryScreenBounds.getHeight()-32);
        editMusicVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editMusicVBox.setPrefHeight(editMusicBorderPane.getPrefHeight()-32);
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
        for (AuteurDb auteurDb : listAuteursSourceValues) {
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
        ObservableList<Label> auteurTargetValues = FXCollections.observableArrayList();
        List<AuteurDb> listAuteursTargetValues = musiqueDb.getListeAuteurs();
        for (AuteurDb auteurDb : listAuteursTargetValues) {
            auteurTargetValues.add(new Label(
                    auteurDb.getPrenomAuteur() != null ?
                            (auteurDb.getPrenomAuteur() + " " + auteurDb.getNomAuteur()).trim() :
                            auteurDb.getNomAuteur()
            ));
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
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/errors/musicTitleError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 330, 140));
                this.setMusicTitleErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (dureeInvalide) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/errors/musicLengthError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 330, 140));
                this.setMusicLengthErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (artistesInvalides) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/errors/musicArtistError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 415, 140));
                this.setMusicArtistErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Boolean.FALSE.equals(titreInvalide) && Boolean.FALSE.equals(dureeInvalide) && Boolean.FALSE.equals(artistesInvalides)) {
            MusiqueDb musique = new MusiqueDb();
            AlbumDb albumMusique = new AlbumDb();
            List<AuteurDb> artistesMusique = new ArrayList<>();

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

            for (int i = 0; i < this.artistes.getTargetItems().size(); i++) {
                AuteurDb artiste = new AuteurDb();
                String identiteArtiste = this.artistes.getTargetItems().get(i).getText();
                Integer indexSeparateurIdentite = identiteArtiste.indexOf(" ");

                // ARTIST(S) NAME RETRIEVING (TO KNOW IF WHITESPACES CONTAINING)
                Boolean auteurHasWhitespacedName = Boolean.FALSE;
                try {
                    PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                            "auteur", "nomAuteur",
                            indexSeparateurIdentite != -1 ? identiteArtiste.substring(indexSeparateurIdentite).trim() : identiteArtiste));
                    ResultSet resultSet = statement.executeQuery();

                    if(resultSet.isClosed()) {
                        auteurHasWhitespacedName = Boolean.TRUE;
                    }
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (indexSeparateurIdentite == -1 || Boolean.TRUE.equals(auteurHasWhitespacedName)) {
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
            musiqueDao.update(musique);

            Stage stage = new Stage();

            try {
                PopUpUtils.setActionDone("modifiée");
                MusicController musicController = new MusicController();
                musicController.initialize(getClass().getResource("/views/music/musicActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/musicActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                super.setMusicActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // MUSIC EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
