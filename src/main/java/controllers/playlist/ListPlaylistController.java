package controllers.playlist;

import controllers.common.Home;
import dao.PlaylistDao;
import db.PlaylistDb;
import dto.PlaylistDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private static final String PLAYLIST_SELECTION_ERROR_FXML = "/views/playlist/errors/playlistSelectionError.fxml";

    // PLAYLIST SELECTION ERROR POP-UP STAGE
    private static Stage playlistSelectionErrorStage;

    // PLAYLIST DELETING CONFIRMATION POP-UP STAGE
    private static Stage playlistDeleteConfirmationStage;

    // PLAYLIST LISTENING POP-UP STAGE
    private static Stage playlistListenStage;

    // PLAYLIST's MUSIC(S) FILE ERROR POP-UP STAGE
    private static Stage playlistMusicFileErrorStage;

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

    public static Stage getPlaylistDeleteConfirmationStage() {
        return playlistDeleteConfirmationStage;
    }

    public static void setPlaylistDeleteConfirmationStage(Stage playlistDeleteConfirmationStage) {
        ListPlaylistController.playlistDeleteConfirmationStage = playlistDeleteConfirmationStage;
    }

    public static Stage getPlaylistListenStage() {
        return playlistListenStage;
    }

    public static void setPlaylistListenStage(Stage playlistListenStage) {
        ListPlaylistController.playlistListenStage = playlistListenStage;
    }

    public static Stage getPlaylistSelectionErrorStage() {
        return playlistSelectionErrorStage;
    }

    public static void setPlaylistSelectionErrorStage(Stage playlistSelectionErrorStage) {
        ListPlaylistController.playlistSelectionErrorStage = playlistSelectionErrorStage;
    }

    public static Stage getPlaylistMusicFileErrorStage() {
        return playlistMusicFileErrorStage;
    }

    public static void setPlaylistMusicFileErrorStage(Stage playlistMusicFileErrorStage) {
        ListPlaylistController.playlistMusicFileErrorStage = playlistMusicFileErrorStage;
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

    // PLAYLIST SELECTION ERROR POP-UP "OK" BUTTON CLICKED
    public void playlistSelectionErrorCloseButtonClicked() {
        getPlaylistSelectionErrorStage().close();
    }

    // PLAYLIST's MUSIC(S) FILE ERROR POP-UP "OK" BUTTON CLICKED
    public void playlistMusicFileErrorCloseButtonClicked(ActionEvent actionEvent) {
        getPlaylistMusicFileErrorStage().close();
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
            PlaylistDto playlistSelected = ListPlaylistSelectionListener.getPlaylistSelected();
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/errors/playlistMusicFileError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 670, 140));
                ListPlaylistController.setPlaylistMusicFileErrorStage(stage);
                stage.show();
                ListPlaylistSelectionListener.setPlaylistSelected(playlistSelected);

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
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
        if (ListPlaylistSelectionListener.getPlaylistSelected() == null) {
            this.showPlaylistSelectionErrorPopUp();
        } else {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/deletePlaylistConfirmation.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Suppression d'une playlist"));
                stage.setScene(new Scene(root, 630, 140));
                setPlaylistDeleteConfirmationStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    private void showPlaylistSelectionErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource(PLAYLIST_SELECTION_ERROR_FXML));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 455, 140));
            ListPlaylistController.setPlaylistSelectionErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

}