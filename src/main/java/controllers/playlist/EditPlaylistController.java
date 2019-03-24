package controllers.playlist;

import dao.MusiqueDao;
import dao.PlaylistDao;
import db.MusiqueDb;
import db.PlaylistDb;
import dto.PlaylistDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import listeners.ListPlaylistSelectionListener;
import mapper.MusiqueMapper;
import mapper.PlaylistMapper;
import org.controlsfx.control.ListSelectionView;
import utils.FormUtils;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class EditPlaylistController extends PlaylistController implements Initializable {

    /**
     * PLAYLIST EDITING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField dateModification = new TextField();
    @FXML
    protected ListSelectionView<Label> musiques = new ListSelectionView<>();

    /**
     * PLAYLIST EDITING FORM CONTAINERS
     */

    @FXML
    BorderPane editPlaylistBorderPane = new BorderPane();
    @FXML
    VBox editPlaylistVBox = new VBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        editPlaylistBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        editPlaylistBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        editPlaylistVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editPlaylistVBox.setPrefHeight(editPlaylistBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {
        PlaylistDto playlistDto = ListPlaylistSelectionListener.getPlaylistSelected();
        PlaylistDb playlistDb = PlaylistMapper.toDb(playlistDto);

        // "titre" FIELD INITIALIZATION
        this.titre.setText(playlistDb.getIntitulePlaylist());

        // "dateModification" FIELD INITIALIZATION
        this.dateModification.setText(FormUtils.getCurrentDate());

        // "musiques" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des musiques");
        sourceLabel.setStyle("-fx-font-weight:bold");
        this.musiques.setSourceHeader(sourceLabel);
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<Label> musiqueSourceValues = FXCollections.observableArrayList();
        List<MusiqueDb> listMusiquesSourceValues = musiqueDao.findAll();
        listMusiquesSourceValues.removeAll(playlistDb.getListeMusiques());

        Map<String, String> musiquesSourceSorted = new TreeMap<>();

        for (MusiqueDb musiqueDb : listMusiquesSourceValues) {
            musiquesSourceSorted.put(musiqueDb.getTitreMusique(), MusiqueMapper.toDto(musiqueDb).getAuteurs());
        }

        int i = 0;

        for (Map.Entry<String, String> musique : musiquesSourceSorted.entrySet()) {
            musiqueSourceValues.add(new Label(musique.getKey()));
            Tooltip tooltip = new Tooltip(musique.getValue());
            Tooltip.install(musiqueSourceValues.get(i), tooltip);
            i++;
        }

        this.musiques.getSourceItems().clear();
        this.musiques.getSourceItems().addAll(musiqueSourceValues);

        Label targetLabel = new Label("Musique(s) de la playlist");
        targetLabel.setStyle("-fx-font-weight:bold");
        this.musiques.setTargetHeader(targetLabel);
        ObservableList<Label> musiqueTargetValues = FXCollections.observableArrayList();
        List<MusiqueDb> listMusiquesTargetValues = playlistDb.getListeMusiques();

        Map<String, String> musiquesTargetSorted = new TreeMap<>();

        for (MusiqueDb musiqueDb : listMusiquesTargetValues) {
            musiquesTargetSorted.put(musiqueDb.getTitreMusique(), MusiqueMapper.toDto(musiqueDb).getAuteurs());
        }

        int j = 0;

        for (Map.Entry<String, String> musique : musiquesTargetSorted.entrySet()) {
            musiqueTargetValues.add(new Label(musique.getKey()));
            Tooltip tooltip = new Tooltip(musique.getValue());
            Tooltip.install(musiqueTargetValues.get(j), tooltip);
            j++;
        }

        this.musiques.getTargetItems().clear();
        this.musiques.getTargetItems().addAll(musiqueTargetValues);
    }

    // PLAYLIST EDITING FORM VALIDATION AND SENDING
    public void validForm() {

        Boolean titreInvalide = "".equals(titre.getText());
        Boolean musiquesInvalides = musiques.getTargetItems().size() == 0;

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (musiquesInvalides) {
            super.showMusicErrorPopUp();
        }

        if (Boolean.FALSE.equals(titreInvalide) && Boolean.FALSE.equals(musiquesInvalides)) {
            PlaylistDb playlist = new PlaylistDb();

            playlist.setIdPlaylist(ListPlaylistSelectionListener.getPlaylistSelected().getIdPlaylist());
            playlist.setIntitulePlaylist(this.titre.getText());
            playlist.setDateActionPlaylist(this.dateModification.getText());
            playlist.setListeMusiques(super.setMusicsToPlaylist(this.musiques.getTargetItems()));

            PlaylistDao playlistDao = new PlaylistDao();
            playlistDao.update(playlist);

            super.showSuccessPopUp("modifiée");
        }
    }

    // PLAYLIST EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
