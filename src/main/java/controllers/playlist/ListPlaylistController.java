package controllers.playlist;

import controllers.common.Home;
import dao.PlaylistDao;
import db.PlaylistDb;
import dto.PlaylistDto;
import enums.TypeSource;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import listeners.playlist.ListPlaylistKeySelectionListener;
import listeners.playlist.ListPlaylistMouseSelectionListener;
import listeners.playlist.ListPlaylistSelectionListener;
import mapper.PlaylistMapper;
import modal.confirmation.CommonConfirmationModal;
import modal.error.CommonErrorModal;
import modal.error.MusicErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ListUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListPlaylistController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(ListPlaylistController.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static final String PAGE = "Page : ";
    private static final String SUR = " / ";

    // PLAYLIST LISTENING POP-UP STAGE
    private static Stage playlistListenStage;

    private Integer currentPageCounter;
    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * SCREEN COMPONENTS
     */

    @FXML
    private TableView<PlaylistDto> listPlaylist = new TableView<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * PLAYLIST LIST COLUMNS
     */

    @FXML
    private TableColumn<PlaylistDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<PlaylistDto, String> dateActionColumn = new TableColumn<>();

    /**
     * PLAYLIST LIST PAGINATION COMPONENTS
     */

    @FXML
    private Label nbPlaylist = new Label();

    @FXML
    public Button firstPage = new Button();

    @FXML
    public Button fifthPreviousPage = new Button();

    @FXML
    public Button previousPage = new Button();

    @FXML
    private Label currentPage = new Label();

    @FXML
    public Button nextPage = new Button();

    @FXML
    public Button fifthNextPage = new Button();

    @FXML
    public Button lastPage = new Button();

    @FXML
    private Label nbPage = new Label();

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
        this.initializeScreenComponents();
        ObservableList<PlaylistDto> table = this.initializeListData();
        this.initializeListPagination(table);
    }

    private void initializeScreenComponents() {
        Tooltip listeningActionTooltip = new Tooltip("Écouter");
        Tooltip.install(this.listeningActionImageView, listeningActionTooltip);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(this.updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(this.deletingActionImageView, deletingActionTooltip);
    }

    private ObservableList<PlaylistDto> initializeListData() {
        PlaylistDao playlistDao = new PlaylistDao();
        ObservableList<PlaylistDto> listPlaylists = FXCollections.observableArrayList();
        List<PlaylistDb> playlistsDb = playlistDao.findAll();
        List<PlaylistDto> playlistsDto = new ArrayList<>();

        for (PlaylistDb playlistDb : playlistsDb) {
            playlistsDto.add(PlaylistMapper.toDto(playlistDb));
        }

        listPlaylists.addAll(playlistsDto);
        listPlaylists.sort((playlist1, playlist2) -> playlist1.getIntitulePlaylist().compareToIgnoreCase(playlist2.getIntitulePlaylist()));

        this.listPlaylist.getItems().clear();
        this.listPlaylist.setItems(listPlaylists);

        for (int i = 0; i < this.listPlaylist.getItems().size(); i++) {

            this.titreColumn.setCellValueFactory(new PropertyValueFactory<>("intitulePlaylist"));
            this.dateActionColumn.setCellValueFactory(new PropertyValueFactory<>("dateActionPlaylist"));
            this.listPlaylist.getColumns().clear();
            this.listPlaylist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.listPlaylist.setOnMouseClicked(new ListPlaylistMouseSelectionListener());
            this.listPlaylist.setOnKeyReleased(new ListPlaylistKeySelectionListener());
            this.listPlaylist.getColumns().addAll(this.titreColumn, this.dateActionColumn);
        }

        this.listPlaylist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Platform.runLater(() -> listPlaylist.refresh());

        return listPlaylists;
    }

    private void initializeListPagination(ObservableList<PlaylistDto> listPlaylists) {
        Integer countPlaylist = listPlaylists.size();
        Integer countPage = ListUtils.getCountPage(countPlaylist);

        this.nbPlaylist.setText("Nombre total de playlists : " + countPlaylist);

        /**
         * PAGINATION INITIALIZATION
         */

        final Integer finalCountPage = countPage;

        Tooltip firstPageTooltip = new Tooltip("Première page");
        Tooltip.install(this.firstPage, firstPageTooltip);
        this.firstPage.setOnAction((ActionEvent event) -> this.goToFirstPage(finalCountPage, listPlaylists));

        Tooltip fifthPreviousPageTooltip = new Tooltip("5ème page précédente");
        Tooltip.install(this.fifthPreviousPage, fifthPreviousPageTooltip);
        this.fifthPreviousPage.setOnAction((ActionEvent event) -> this.goToFifthPreviousPage(finalCountPage, listPlaylists));

        Tooltip previousPageTooltip = new Tooltip("Page précédente");
        Tooltip.install(this.previousPage, previousPageTooltip);
        this.previousPage.setOnAction((ActionEvent event) -> this.goToPreviousPage(finalCountPage, listPlaylists));

        Tooltip nextPageTooltip = new Tooltip("Page suivante");
        Tooltip.install(this.nextPage, nextPageTooltip);
        this.nextPage.setOnAction((ActionEvent event) -> this.goToNextPage(finalCountPage, listPlaylists));

        Tooltip fifthNextPageTooltip = new Tooltip("5ème page suivante");
        Tooltip.install(this.fifthNextPage, fifthNextPageTooltip);
        this.fifthNextPage.setOnAction((ActionEvent event) -> this.goToFifthNextPage(finalCountPage, listPlaylists));

        Tooltip lastPageTooltip = new Tooltip("Dernière page");
        Tooltip.install(this.lastPage, lastPageTooltip);
        this.lastPage.setOnAction((ActionEvent event) -> this.goToLastPage(finalCountPage, listPlaylists));

        this.goToFirstPage(countPage, listPlaylists);
    }

    // METHOD TO GO TO THE FIRST PAGE OF PLAYLIST LIST
    private void goToFirstPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter = 1;

        this.firstPage.setDisable(true);
        this.fifthPreviousPage.setDisable(true);
        this.previousPage.setDisable(true);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(countPage == 1);
        this.fifthNextPage.setDisable(countPage <= 6);
        this.lastPage.setDisable(countPage == 1);

        this.listPlaylist.setItems(countPage == 1 ? listPlaylists : FXCollections.observableArrayList(listPlaylists.subList(0, 10)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
    }

    // METHOD TO GO TO THE FIFTH PREVIOUS PAGE OF PLAYLIST LIST
    private void goToFifthPreviousPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter = this.currentPageCounter - 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listPlaylist.setItems(FXCollections.observableArrayList(listPlaylists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
    }

    // METHOD TO GO TO THE PREVIOUS PAGE OF PLAYLIST LIST
    private void goToPreviousPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter--;

        if (this.currentPageCounter == 1) {
            this.goToFirstPage(countPage, listPlaylists);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.nextPage.setDisable(false);
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
            this.lastPage.setDisable(false);

            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            this.listPlaylist.setItems(FXCollections.observableArrayList(listPlaylists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
        }
    }

    // METHOD TO GO TO THE NEXT PAGE OF PLAYLIST LIST
    private void goToNextPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter++;

        if (countPage.intValue() == this.currentPageCounter.intValue()) {
            this.goToLastPage(countPage, listPlaylists);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);

            this.listPlaylist.setItems(FXCollections.observableArrayList(listPlaylists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
        }
    }

    // METHOD TO GO TO THE FIFTH NEXT PAGE OF PLAYLIST LIST
    private void goToFifthNextPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter = this.currentPageCounter + 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listPlaylist.setItems(FXCollections.observableArrayList(listPlaylists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
    }

    // METHOD TO GO TO THE LAST PAGE OF PLAYLIST LIST
    private void goToLastPage(Integer countPage, ObservableList<PlaylistDto> listPlaylists) {
        this.currentPageCounter = countPage;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(true);
        this.fifthNextPage.setDisable(true);
        this.lastPage.setDisable(true);

        this.listPlaylist.setItems(FXCollections.observableArrayList(listPlaylists.subList(((this.currentPageCounter - 1) * 10), listPlaylists.size())));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstPlaylistOfPage(this.listPlaylist);
    }

    // PLAYLIST LISTENING ICON CLICKED
    public void playlistListeningButtonClicked(MouseEvent mouseEvent) {
        PlaylistDto playlistSelected = ListPlaylistSelectionListener.getPlaylistSelected();
        boolean hasMusicWithNoFile = false;
        boolean hasMusicWithWrongFile = false;

        if (playlistSelected != null) {
            hasMusicWithNoFile = playlistSelected.getListeMusiques()
                    .stream()
                    .anyMatch(musique -> musique.getNomFichierMusique() == null ||
                            "".equals(musique.getNomFichierMusique()));

            hasMusicWithWrongFile = Boolean.TRUE.equals(playlistSelected.getListeMusiques()
                    .stream()
                    .map(musique -> !(new File(musique.getNomFichierMusique()).exists()))
                    .reduce((wrongFile1, wrongFile2) -> wrongFile1 || wrongFile2)
                    .orElse(Boolean.FALSE));
        }

        if (playlistSelected == null) {
            this.showPlaylistSelectionErrorPopUp();
        } else if (hasMusicWithNoFile || hasMusicWithWrongFile) {
            MusicErrorModal.getMusicFileErrorAlert(TypeSource.PLAYLIST);
        } else {
            Stage stage = new Stage();

            try {
                ListenPlaylistController listenPlaylistController = new ListenPlaylistController();
                listenPlaylistController.initialize(getClass().getResource("/views/playlist/listenPlaylist.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/listenPlaylist.fxml"));
                stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, "Écoute d'une playlist"));
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
                homeStage.setTitle(this.informationsUtils.buildStageTitleBar(homeStage, "Modification d'une playlist"));
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
            CommonConfirmationModal.getDeleteConfirmationAlert(TypeSource.PLAYLIST, playlistSelected);
        }
    }

    private void showPlaylistSelectionErrorPopUp() {
        CommonErrorModal.getSelectionErrorAlert(TypeSource.PLAYLIST);
    }

}
