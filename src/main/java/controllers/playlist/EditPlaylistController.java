package controllers.playlist;

import dao.MusiqueDao;
import dao.PlaylistDao;
import db.MusiqueDb;
import db.PlaylistDb;
import dto.MusiqueDto;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
    // LIST OF ALL MUSIC
    private List<MusiqueDto> listMusiquesValues = new LinkedList<>();
    // ALPHABETICAL SORTED LIST OF "SOURCE" MUSIC
    private List<MusiqueDto> musiquesSourceSorted = new LinkedList<>();
    // ALPHABETICAL SORTED LIST OF "TARGET" MUSIC
    private List<MusiqueDto> musiquesTargetSorted = new LinkedList<>();

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

        for (MusiqueDb musiqueDb : listMusiquesSourceValues) {
            this.listMusiquesValues.add(MusiqueMapper.toDto(musiqueDb));
        }

        listMusiquesSourceValues.removeAll(playlistDb.getListeMusiques());

        for (MusiqueDb musiqueDb : listMusiquesSourceValues) {
            this.musiquesSourceSorted.add(MusiqueMapper.toDto(musiqueDb));
        }

        this.musiquesSourceSorted.sort((musique1, musique2) -> musique1.getTitreMusique().compareToIgnoreCase(musique2.getTitreMusique()));

        int i = 0;

        for (MusiqueDto musiqueDto : this.musiquesSourceSorted) {
            musiqueSourceValues.add(new Label(musiqueDto.getTitreMusique()));
            Tooltip tooltip = new Tooltip(musiqueDto.getAuteurs());
            Tooltip.install(musiqueSourceValues.get(i), tooltip);
            musiqueSourceValues.get(i).setTooltip(tooltip);
            i++;
        }

        this.musiques.getSourceItems().clear();
        this.musiques.getSourceItems().addAll(musiqueSourceValues);

        Label targetLabel = new Label("Musique(s) de la playlist");
        targetLabel.setStyle("-fx-font-weight:bold");
        this.musiques.setTargetHeader(targetLabel);
        ObservableList<Label> musiqueTargetValues = FXCollections.observableArrayList();
        List<MusiqueDb> listMusiquesTargetValues = playlistDb.getListeMusiques();

        for (MusiqueDb musiqueDb : listMusiquesTargetValues) {
            this.musiquesTargetSorted.add(MusiqueMapper.toDto(musiqueDb));
        }

        this.musiquesTargetSorted.sort((musique1, musique2) -> musique1.getTitreMusique().compareToIgnoreCase(musique2.getTitreMusique()));

        int j = 0;

        for (MusiqueDto musiqueDto : musiquesTargetSorted) {
            musiqueTargetValues.add(new Label(musiqueDto.getTitreMusique()));
            Tooltip tooltip = new Tooltip(musiqueDto.getAuteurs());
            Tooltip.install(musiqueTargetValues.get(j), tooltip);
            musiqueTargetValues.get(j).setTooltip(tooltip);
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
            List<MusiqueDto> musiquesSelected = new LinkedList<>();

            playlist.setIdPlaylist(ListPlaylistSelectionListener.getPlaylistSelected().getIdPlaylist());
            playlist.setIntitulePlaylist(this.titre.getText());
            playlist.setDateActionPlaylist(this.dateModification.getText());

            for (Label label : this.musiques.getTargetItems()) {
                Optional<MusiqueDto> musiqueSelected = this.listMusiquesValues.stream()
                        .filter(musiqueDto -> musiqueDto.getTitreMusique().equals(label.getText()))
                        .filter(musiqueDto -> musiqueDto.getAuteurs().equals(label.getTooltip().getText()))
                        .findFirst();

                musiqueSelected.ifPresent(musiquesSelected::add);
            }

            playlist.setListeMusiques(super.setMusicsToPlaylist(musiquesSelected));

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
