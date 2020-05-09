package controllers.playlist;

import dao.MusiqueDao;
import dao.PlaylistDao;
import db.MusiqueDb;
import db.PlaylistDb;
import dto.MusiqueDto;
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
import mapper.MusiqueMapper;
import utils.FormUtils;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPlaylistController extends PlaylistController implements Initializable {

    /**
     * PLAYLIST ADDING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField dateInsertion = new TextField();
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
     * PLAYLIST ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addPlaylistBorderPane = new BorderPane();
    @FXML
    VBox addPlaylistVBox = new VBox();
    // ALPHABETICAL SORTED LIST OF MUSIC
    private List<MusiqueDto> musiquesSorted = new LinkedList<>();

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
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<Label> musiqueSourceValues = FXCollections.observableArrayList();
        ObservableList<Label> musiqueTargetValues = FXCollections.observableArrayList();
        List<MusiqueDb> listMusiquesValues = musiqueDao.findAll();

        for (MusiqueDb musiqueDb : listMusiquesValues) {
            this.musiquesSorted.add(MusiqueMapper.toDto(musiqueDb));
        }

        this.musiquesSorted.sort((musique1, musique2) -> musique1.getTitreMusique().compareToIgnoreCase(musique2.getTitreMusique()));

        int i = 0;

        for (MusiqueDto musiqueDto : musiquesSorted) {
            musiqueSourceValues.add(new Label(musiqueDto.getTitreMusique()));
            Tooltip tooltip = new Tooltip(musiqueDto.getAuteurs());
            Tooltip.install(musiqueSourceValues.get(i), tooltip);
            musiqueSourceValues.get(i).setTooltip(tooltip);
            i++;
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

    // PLAYLIST ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        boolean titreInvalide = "".equals(titre.getText());
        boolean musiquesInvalides = this.target.getItems().isEmpty();

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (musiquesInvalides) {
            super.showMusicErrorPopUp();
        }

        if (!titreInvalide && !musiquesInvalides) {
            PlaylistDb playlist = new PlaylistDb();
            List<MusiqueDto> musiquesSelected = new LinkedList<>();

            playlist.setIntitulePlaylist(this.titre.getText());
            playlist.setDateActionPlaylist(this.dateInsertion.getText());

            for (Label label : this.target.getItems()) {
                Optional<MusiqueDto> musiqueSelected = this.musiquesSorted.stream()
                        .filter(musiqueDto -> musiqueDto.getTitreMusique().equals(label.getText()))
                        .filter(musiqueDto -> musiqueDto.getAuteurs().equals(label.getTooltip().getText()))
                        .findFirst();

                musiqueSelected.ifPresent(musiquesSelected::add);
            }

            playlist.setListeMusiques(super.setMusicsToPlaylist(musiquesSelected));

            PlaylistDao playlistDao = new PlaylistDao();
            playlistDao.insert(playlist);

            super.showSuccessPopUp(TypeAction.ADD);
        }
    }

    // PLAYLIST ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
