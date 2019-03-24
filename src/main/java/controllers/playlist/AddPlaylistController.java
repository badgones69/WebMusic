package controllers.playlist;

import dao.MusiqueDao;
import dao.PlaylistDao;
import db.MusiqueDb;
import db.PlaylistDb;
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
import mapper.MusiqueMapper;
import org.controlsfx.control.ListSelectionView;
import utils.FormUtils;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class AddPlaylistController extends PlaylistController implements Initializable {

    /**
     * PLAYLIST ADDING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField dateInsertion = new TextField();
    @FXML
    protected ListSelectionView<Label> musiques = new ListSelectionView<>();

    /**
     * PLAYLIST ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addPlaylistBorderPane = new BorderPane();
    @FXML
    VBox addPlaylistVBox = new VBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        addPlaylistBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        addPlaylistBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        addPlaylistVBox.setPrefWidth(primaryScreenBounds.getWidth());
        addPlaylistVBox.setPrefHeight(addPlaylistBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {

        // "titre" FIELD INITIALIZATION
        this.titre.clear();

        // "dateInsertion" FIELD INITIALIZATION
        this.dateInsertion.setText(FormUtils.getCurrentDate());

        // "musiques" PICKLIST INITIALIZATION
        Label sourceLabel = new Label("Liste des musiques");
        sourceLabel.setStyle("-fx-font-weight:bold");
        this.musiques.setSourceHeader(sourceLabel);
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<Label> musiqueSourceValues = FXCollections.observableArrayList();
        List<MusiqueDb> listMusiquesValues = musiqueDao.findAll();
        Map<String, String> musiquesSorted = new TreeMap<>();
        for (MusiqueDb musiqueDb : listMusiquesValues) {
            musiquesSorted.put(musiqueDb.getTitreMusique(), MusiqueMapper.toDto(musiqueDb).getAuteurs());
        }

        int i = 0;

        for (Map.Entry<String, String> musique : musiquesSorted.entrySet()) {
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
        this.musiques.getTargetItems().clear();
    }

    // PLAYLIST ADDING FORM VALIDATION AND SENDING
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

            playlist.setIntitulePlaylist(this.titre.getText());
            playlist.setDateActionPlaylist(this.dateInsertion.getText());
            playlist.setListeMusiques(super.setMusicsToPlaylist(this.musiques.getTargetItems()));

            PlaylistDao playlistDao = new PlaylistDao();
            playlistDao.insert(playlist);

            super.showSuccessPopUp("ajoutée");
        }
    }

    // PLAYLIST ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
