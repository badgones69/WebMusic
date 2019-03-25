package controllers.album;

import controllers.common.Home;
import dao.AlbumDao;
import db.AlbumDb;
import dto.AlbumDto;
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
import javafx.stage.Stage;
import listeners.ListAlbumSelectionListener;
import mapper.AlbumMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListAlbumController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(ListAlbumController.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static final String ALBUM_SELECTION_ERROR_FXML = "/views/album/errors/albumSelectionError.fxml";

    // ALBUM SELECTION ERROR POP-UP STAGE
    private static Stage albumSelectionErrorStage;

    // ALBUM's MUSIC(S) FILE ERROR POP-UP STAGE
    private static Stage albumMusicFileErrorStage;

    // ALBUM DELETING CONFIRMATION POP-UP STAGE
    private static Stage albumDeleteConfirmationStage;

    // ALBUM LISTENING POP-UP STAGE
    private static Stage albumListenStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * ALBUM LIST COLUMNS
     */

    @FXML
    private TableView<AlbumDto> listAlbum = new TableView<>();

    @FXML
    private TableColumn<AlbumDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<AlbumDto, String> anneeColumn = new TableColumn<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAlbumDeleteConfirmationStage() {
        return albumDeleteConfirmationStage;
    }

    public static void setAlbumDeleteConfirmationStage(Stage albumDeleteConfirmationStage) {
        ListAlbumController.albumDeleteConfirmationStage = albumDeleteConfirmationStage;
    }

    public static Stage getAlbumListenStage() {
        return albumListenStage;
    }

    public static void setAlbumListenStage(Stage albumListenStage) {
        ListAlbumController.albumListenStage = albumListenStage;
    }

    public static Stage getAlbumSelectionErrorStage() {
        return albumSelectionErrorStage;
    }

    public static void setAlbumSelectionErrorStage(Stage albumSelectionErrorStage) {
        ListAlbumController.albumSelectionErrorStage = albumSelectionErrorStage;
    }

    public static Stage getAlbumMusicFileErrorStage() {
        return albumMusicFileErrorStage;
    }

    public static void setAlbumMusicFileErrorStage(Stage albumMusicFileErrorStage) {
        ListAlbumController.albumMusicFileErrorStage = albumMusicFileErrorStage;
    }

    /**
     * ALBUM LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeList();
    }

    private void initializeSizes() {
        listAlbum.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeList() {
        ListAlbumSelectionListener.setAlbumSelected(null);

        Tooltip listeningActionTooltip = new Tooltip("Écouter");
        Tooltip.install(listeningActionImageView, listeningActionTooltip);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(deletingActionImageView, deletingActionTooltip);

        AlbumDao albumDao = new AlbumDao();
        ObservableList<AlbumDto> listAlbums = FXCollections.observableArrayList();
        List<AlbumDb> albumsDb = albumDao.findAll();
        List<AlbumDto> albumsDto = new ArrayList<>();

        for (AlbumDb albumDb : albumsDb) {
            albumsDto.add(AlbumMapper.toDto(albumDb));
        }

        for (AlbumDto albumDto : albumsDto) {
            listAlbums.add(albumDto);
        }

        this.listAlbum.getItems().clear();
        this.listAlbum.setItems(listAlbums);

        for (int i = 0; i < this.listAlbum.getItems().size(); i++) {

            titreColumn.setCellValueFactory(new PropertyValueFactory<AlbumDto, String>("titreAlbum"));
            anneeColumn.setCellValueFactory(new PropertyValueFactory<AlbumDto, String>("anneeAlbum"));
            listAlbum.getColumns().clear();
            listAlbum.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listAlbum.setOnMouseClicked(new ListAlbumSelectionListener());
            listAlbum.getColumns().addAll(titreColumn, anneeColumn);
            listAlbum.getSortOrder().add(titreColumn);
        }

        Platform.runLater(() -> listAlbum.refresh());
    }

    // ALBUM SELECTION ERROR POP-UP "OK" BUTTON CLICKED
    public void albumSelectionErrorCloseButtonClicked() {
        getAlbumSelectionErrorStage().close();
    }

    // ALBUM FILE ERROR POP-UP "OK" BUTTON CLICKED
    public void albumMusicFileErrorCloseButtonClicked() {
        getAlbumMusicFileErrorStage().close();
    }

    // ALBUM LISTENING ICON CLICKED
    public void albumListeningButtonClicked() {
        boolean hasMusicWithNoFile = false;

        if (ListAlbumSelectionListener.getAlbumSelected() != null) {
            AlbumDao albumDao = new AlbumDao();
            AlbumDb albumSelectedToDb = AlbumMapper.toDb(ListAlbumSelectionListener.getAlbumSelected());

            hasMusicWithNoFile = albumDao.getMusiques(albumSelectedToDb)
                    .stream()
                    .anyMatch(musique -> musique.getNomFichierMusique() == null ||
                            "".equals(musique.getNomFichierMusique()));
        }

        if (ListAlbumSelectionListener.getAlbumSelected() == null) {
            this.showAlbumSelectionErrorPopUp();
        } else if (hasMusicWithNoFile) {
            AlbumDto albumSelected = ListAlbumSelectionListener.getAlbumSelected();
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/errors/albumMusicFileError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 670, 140));
                ListAlbumController.setAlbumMusicFileErrorStage(stage);
                stage.show();
                ListAlbumSelectionListener.setAlbumSelected(albumSelected);

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        } else {
            Stage stage = new Stage();

            try {
                ListenAlbumController listenAlbumController = new ListenAlbumController();
                listenAlbumController.initialize(getClass().getResource("/views/album/listenAlbum.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/listenAlbum.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Écoute d'un album"));
                stage.setScene(new Scene(root, 600, 260));
                setAlbumListenStage(stage);

                getAlbumListenStage().setOnCloseRequest(event -> ListenAlbumController.getMediaPlayer().stop());

                albumListenStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ALBUM EDITING ICON CLICKED
    public void albumEditingButtonClicked() {
        if (ListAlbumSelectionListener.getAlbumSelected() == null) {
            this.showAlbumSelectionErrorPopUp();
        } else {
            Stage homeStage = new Home().getHomeStage();
            homeStage.show();

            try {
                EditAlbumController editAlbumController = new EditAlbumController();
                editAlbumController.initialize(getClass().getResource("/views/album/editAlbum.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/editAlbum.fxml"));
                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Modification d'un album"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ALBUM DELETING ICON CLICKED
    public void albumDeletingButtonClicked() {
        if (ListAlbumSelectionListener.getAlbumSelected() == null) {
            this.showAlbumSelectionErrorPopUp();
        } else {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/deleteAlbumConfirmation.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Suppression d'un album"));
                stage.setScene(new Scene(root, 630, 140));
                setAlbumDeleteConfirmationStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    private void showAlbumSelectionErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource(ALBUM_SELECTION_ERROR_FXML));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 455, 140));
            ListAlbumController.setAlbumSelectionErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
