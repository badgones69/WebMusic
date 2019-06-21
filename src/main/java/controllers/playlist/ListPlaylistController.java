package controllers.playlist;

import controllers.common.Home;
import dao.PlaylistDao;
import db.PlaylistDb;
import dto.PlaylistDto;
import enums.TypeSource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import listeners.ListPlaylistSelectionListener;
import mapper.PlaylistMapper;
import modal.DeleteConfirmationModal;
import modal.MusicErrorModal;
import modal.SelectionErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListPlaylistController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(ListPlaylistController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // PLAYLIST LISTENING POP-UP STAGE
    private static Stage playlistListenStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * PLAYLIST LIST COLUMNS
     */

    @FXML
    private TableView<PlaylistDto> listPlaylist = new TableView<>();

    @FXML
    private TableColumn<PlaylistDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<PlaylistDto, String> dateActionColumn = new TableColumn<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getPlaylistListenStage() {
        return playlistListenStage;
    }

    public static void setPlaylistListenStage(Stage playlistListenStage) {
        ListPlaylistController.playlistListenStage = playlistListenStage;
    }

    /**
     * PLAYLIST LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeList();
    }

    private void initializeSizes() {
        listPlaylist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeList() {
        ListPlaylistSelectionListener.setPlaylistSelected(null);

        Tooltip listeningActionTooltip = new Tooltip("Écouter");
        Tooltip.install(listeningActionImageView, listeningActionTooltip);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(deletingActionImageView, deletingActionTooltip);

        PlaylistDao playlistDao = new PlaylistDao();
        ObservableList<PlaylistDto> listPlaylists = FXCollections.observableArrayList();
        List<PlaylistDb> playlistsDb = playlistDao.findAll();
        List<PlaylistDto> playlistsDto = new ArrayList<>();

        for (PlaylistDb playlistDb : playlistsDb) {
            playlistsDto.add(PlaylistMapper.toDto(playlistDb));
        }

        for (PlaylistDto playlistDto : playlistsDto) {
            listPlaylists.add(playlistDto);
        }

        this.listPlaylist.getItems().clear();
        this.listPlaylist.setItems(listPlaylists);

        for (int i = 0; i < this.listPlaylist.getItems().size(); i++) {

            titreColumn.setCellValueFactory(new PropertyValueFactory<PlaylistDto, String>("intitulePlaylist"));
            dateActionColumn.setCellValueFactory(new PropertyValueFactory<PlaylistDto, String>("dateActionPlaylist"));
            listPlaylist.getColumns().clear();
            listPlaylist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listPlaylist.setOnMouseClicked(new ListPlaylistSelectionListener());
            listPlaylist.getColumns().addAll(titreColumn, dateActionColumn);
            listPlaylist.getSortOrder().add(titreColumn);
        }

        Platform.runLater(() -> listPlaylist.refresh());
    }

    // PLAYLIST LISTENING ICON CLICKED
    public void playlistListeningButtonClicked(MouseEvent mouseEvent) {
        boolean hasMusicWithNoFile = false;

        if (ListPlaylistSelectionListener.getPlaylistSelected() != null) {
            hasMusicWithNoFile = ListPlaylistSelectionListener.getPlaylistSelected().getListeMusiques()
                    .stream()
                    .anyMatch(musique -> musique.getNomFichierMusique() == null ||
                            "".equals(musique.getNomFichierMusique()));
        }

        if (ListPlaylistSelectionListener.getPlaylistSelected() == null) {
            this.showPlaylistSelectionErrorPopUp();
        } else if (hasMusicWithNoFile) {
            MusicErrorModal.getMusicFileErrorAlert(TypeSource.PLAYLIST);
        } else {
            Stage stage = new Stage();

            try {
                ListenPlaylistController listenPlaylistController = new ListenPlaylistController();
                listenPlaylistController.initialize(getClass().getResource("/views/playlist/listenPlaylist.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/listenPlaylist.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Écoute d'une playlist"));
                stage.setScene(new Scene(root, 600, 260));
                setPlaylistListenStage(stage);

                getPlaylistListenStage().setOnCloseRequest(event -> ListenPlaylistController.getMediaPlayer().stop());

                playlistListenStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // PLAYLIST EDITING ICON CLICKED
    public void playlistEditingButtonClicked() {
        if (ListPlaylistSelectionListener.getPlaylistSelected() == null) {
            this.showPlaylistSelectionErrorPopUp();
        } else {
            Stage homeStage = new Home().getHomeStage();
            homeStage.show();

            try {
                EditPlaylistController editPlaylistController = new EditPlaylistController();
                editPlaylistController.initialize(getClass().getResource("/views/playlist/editPlaylist.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/editPlaylist.fxml"));
                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Modification d'une playlist"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // PLAYLIST DELETING ICON CLICKED
    public void playlistDeletingButtonClicked() {
        PlaylistDto playlistSelected = ListPlaylistSelectionListener.getPlaylistSelected();

        if (playlistSelected == null) {
            this.showPlaylistSelectionErrorPopUp();
        } else {
            DeleteConfirmationModal.getDeleteConfirmationAlert(TypeSource.PLAYLIST, playlistSelected);
        }
    }

    private void showPlaylistSelectionErrorPopUp() {
        SelectionErrorModal.getSelectionErrorAlert(TypeSource.PLAYLIST);
    }

}
