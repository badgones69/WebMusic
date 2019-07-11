package controllers.playlist;

import dao.MusiqueDao;
import dao.PlaylistDao;
import db.MusiqueDb;
import db.PlaylistDb;
import dto.MusiqueDto;
import dto.PlaylistDto;
import enums.TypeAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import listeners.ListPlaylistSelectionListener;
import mapper.MusiqueMapper;
import mapper.PlaylistMapper;
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
    protected ListView<Label> source = new ListView<>();
    @FXML
    protected ListView<Label> target = new ListView<>();
    @FXML
    protected Button addMusic = new Button();
    @FXML
    protected Button removeMusic = new Button();
    @FXML
    protected Button addAllMusic = new Button();
    @FXML
    protected Button removeAllMusic = new Button();
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
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<Label> musiqueSourceValues = FXCollections.observableArrayList();
        ObservableList<Label> musiqueTargetValues = FXCollections.observableArrayList();
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

        this.source.getItems().addAll(musiqueSourceValues);
        this.target.getItems().addAll(musiqueTargetValues);

        this.source.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.target.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.addMusic.setOnAction((ActionEvent event) -> {
            musiqueSourceValues.removeAll(source.getSelectionModel().getSelectedItems());
            musiqueTargetValues.addAll(source.getSelectionModel().getSelectedItems());
            musiqueTargetValues.sort((musique1, musique2) -> musique1.getText().compareToIgnoreCase(musique2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(musiqueSourceValues);
            this.target.getItems().clear();
            this.target.getItems().addAll(musiqueTargetValues);
        });

        this.addAllMusic.setOnAction((ActionEvent event) -> {
            musiqueTargetValues.addAll(musiqueSourceValues);
            musiqueSourceValues.clear();
            musiqueTargetValues.sort((musique1, musique2) -> musique1.getText().compareToIgnoreCase(musique2.getText()));
            this.source.getItems().clear();
            this.target.getItems().clear();
            this.target.getItems().addAll(musiqueTargetValues);
        });

        this.removeMusic.setOnAction((ActionEvent event) -> {
            musiqueTargetValues.removeAll(target.getSelectionModel().getSelectedItems());
            musiqueSourceValues.addAll(target.getSelectionModel().getSelectedItems());
            musiqueSourceValues.sort((musique1, musique2) -> musique1.getText().compareToIgnoreCase(musique2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(musiqueSourceValues);
            this.target.getItems().clear();
            this.target.getItems().addAll(musiqueTargetValues);
        });

        this.removeAllMusic.setOnAction((ActionEvent event) -> {
            musiqueSourceValues.addAll(musiqueTargetValues);
            musiqueTargetValues.clear();
            musiqueSourceValues.sort((musique1, musique2) -> musique1.getText().compareToIgnoreCase(musique2.getText()));
            this.source.getItems().clear();
            this.source.getItems().addAll(musiqueSourceValues);
            this.target.getItems().clear();
        });
    }

    // PLAYLIST EDITING FORM VALIDATION AND SENDING
    public void validForm() {

        Boolean titreInvalide = "".equals(titre.getText());
        Boolean musiquesInvalides = this.target.getItems().size() == 0;

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

            for (Label label : this.target.getItems()) {
                Optional<MusiqueDto> musiqueSelected = this.listMusiquesValues.stream()
                        .filter(musiqueDto -> musiqueDto.getTitreMusique().equals(label.getText()))
                        .filter(musiqueDto -> musiqueDto.getAuteurs().equals(label.getTooltip().getText()))
                        .findFirst();

                musiqueSelected.ifPresent(musiquesSelected::add);
            }

            playlist.setListeMusiques(super.setMusicsToPlaylist(musiquesSelected));

            PlaylistDao playlistDao = new PlaylistDao();
            playlistDao.update(playlist);

            super.showSuccessPopUp(TypeAction.EDIT);
        }
    }

    // PLAYLIST EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
