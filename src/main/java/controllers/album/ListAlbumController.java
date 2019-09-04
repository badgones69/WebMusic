package controllers.album;

import controllers.common.Home;
import dao.AlbumDao;
import db.AlbumDb;
import dto.AlbumDto;
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
import javafx.stage.Stage;
import listeners.album.ListAlbumKeySelectionListener;
import listeners.album.ListAlbumMouseSelectionListener;
import listeners.album.ListAlbumSelectionListener;
import mapper.AlbumMapper;
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

public class ListAlbumController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(ListAlbumController.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static final String PAGE = "Page : ";
    private static final String SUR = " / ";

    // ALBUM LISTENING POP-UP STAGE
    private static Stage albumListenStage;

    private Integer currentPageCounter;
    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * SCREEN COMPONENTS
     */

    @FXML
    private TableView<AlbumDto> listAlbum = new TableView<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * ALBUM LIST COLUMNS
     */

    @FXML
    private TableColumn<AlbumDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<AlbumDto, String> anneeColumn = new TableColumn<>();

    /**
     * ALBUM LIST PAGINATION COMPONENTS
     */

    @FXML
    private Label nbAlbum = new Label();

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

    public static Stage getAlbumListenStage() {
        return albumListenStage;
    }

    public static void setAlbumListenStage(Stage albumListenStage) {
        ListAlbumController.albumListenStage = albumListenStage;
    }

    /**
     * ALBUM LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeScreenComponents();
        ObservableList<AlbumDto> table = this.initializeListData();
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

    private ObservableList<AlbumDto> initializeListData() {
        AlbumDao albumDao = new AlbumDao();
        List<AlbumDb> albumsDb = albumDao.findAll();
        List<AlbumDto> albumsDto = new ArrayList<>();
        ObservableList<AlbumDto> listAlbums = FXCollections.observableArrayList();

        for (AlbumDb albumDb : albumsDb) {
            albumsDto.add(AlbumMapper.toDto(albumDb));
        }

        listAlbums.addAll(albumsDto);
        listAlbums.sort((album1, album2) -> album1.getTitreAlbum().compareToIgnoreCase(album2.getTitreAlbum()));

        this.listAlbum.getItems().clear();
        this.listAlbum.setItems(listAlbums);

        for (int i = 0; i < this.listAlbum.getItems().size(); i++) {

            this.titreColumn.setCellValueFactory(new PropertyValueFactory<>("titreAlbum"));
            this.anneeColumn.setCellValueFactory(new PropertyValueFactory<>("anneeAlbum"));
            this.listAlbum.getColumns().clear();
            this.listAlbum.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.listAlbum.setOnMouseClicked(new ListAlbumMouseSelectionListener());
            this.listAlbum.setOnKeyReleased(new ListAlbumKeySelectionListener());
            this.listAlbum.getColumns().addAll(this.titreColumn, this.anneeColumn);
        }

        this.listAlbum.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Platform.runLater(() -> this.listAlbum.refresh());

        return listAlbums;
    }

    private void initializeListPagination(ObservableList<AlbumDto> listAlbums) {
        Integer countAlbum = listAlbums.size();
        Integer countPage = ListUtils.getCountPage(countAlbum);

        this.nbAlbum.setText("Nombre total d'albums : " + countAlbum);

        /**
         * PAGINATION INITIALIZATION
         */

        final Integer finalCountPage = countPage;

        Tooltip firstPageTooltip = new Tooltip("Première page");
        Tooltip.install(this.firstPage, firstPageTooltip);
        this.firstPage.setOnAction((ActionEvent event) -> this.goToFirstPage(finalCountPage, listAlbums));

        Tooltip fifthPreviousPageTooltip = new Tooltip("5ème page précédente");
        Tooltip.install(this.fifthPreviousPage, fifthPreviousPageTooltip);
        this.fifthPreviousPage.setOnAction((ActionEvent event) -> this.goToFifthPreviousPage(finalCountPage, listAlbums));

        Tooltip previousPageTooltip = new Tooltip("Page précédente");
        Tooltip.install(this.previousPage, previousPageTooltip);
        this.previousPage.setOnAction((ActionEvent event) -> this.goToPreviousPage(finalCountPage, listAlbums));

        Tooltip nextPageTooltip = new Tooltip("Page suivante");
        Tooltip.install(this.nextPage, nextPageTooltip);
        this.nextPage.setOnAction((ActionEvent event) -> this.goToNextPage(finalCountPage, listAlbums));

        Tooltip fifthNextPageTooltip = new Tooltip("5ème page suivante");
        Tooltip.install(this.fifthNextPage, fifthNextPageTooltip);
        this.fifthNextPage.setOnAction((ActionEvent event) -> this.goToFifthNextPage(finalCountPage, listAlbums));

        Tooltip lastPageTooltip = new Tooltip("Dernière page");
        Tooltip.install(this.lastPage, lastPageTooltip);
        this.lastPage.setOnAction((ActionEvent event) -> this.goToLastPage(finalCountPage, listAlbums));

        this.goToFirstPage(countPage, listAlbums);
    }

    // METHOD TO GO TO THE FIRST PAGE OF ALBUM LIST
    private void goToFirstPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter = 1;

        this.firstPage.setDisable(true);
        this.fifthPreviousPage.setDisable(true);
        this.previousPage.setDisable(true);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(countPage == 1);
        this.fifthNextPage.setDisable(countPage <= 6);
        this.lastPage.setDisable(countPage == 1);

        this.listAlbum.setItems(countPage == 1 ? listAlbums : FXCollections.observableArrayList(listAlbums.subList(0, 10)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstAlbumOfPage(this.listAlbum);
    }

    // METHOD TO GO TO THE FIFTH PREVIOUS PAGE OF ALBUM LIST
    private void goToFifthPreviousPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter = this.currentPageCounter - 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listAlbum.setItems(FXCollections.observableArrayList(listAlbums.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstAlbumOfPage(this.listAlbum);
    }

    // METHOD TO GO TO THE PREVIOUS PAGE OF ALBUM LIST
    private void goToPreviousPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter--;

        if (this.currentPageCounter == 1) {
            this.goToFirstPage(countPage, listAlbums);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.nextPage.setDisable(false);
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
            this.lastPage.setDisable(false);

            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            this.listAlbum.setItems(FXCollections.observableArrayList(listAlbums.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            ListUtils.selectFirstAlbumOfPage(this.listAlbum);
        }
    }

    // METHOD TO GO TO THE NEXT PAGE OF ALBUM LIST
    private void goToNextPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter++;

        if (countPage.intValue() == this.currentPageCounter.intValue()) {
            this.goToLastPage(countPage, listAlbums);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);

            this.listAlbum.setItems(FXCollections.observableArrayList(listAlbums.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            ListUtils.selectFirstAlbumOfPage(this.listAlbum);
        }
    }

    // METHOD TO GO TO THE FIFTH NEXT PAGE OF ALBUM LIST
    private void goToFifthNextPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter = this.currentPageCounter + 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listAlbum.setItems(FXCollections.observableArrayList(listAlbums.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstAlbumOfPage(this.listAlbum);
    }

    // METHOD TO GO TO THE LAST PAGE OF ALBUM LIST
    private void goToLastPage(Integer countPage, ObservableList<AlbumDto> listAlbums) {
        this.currentPageCounter = countPage;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(true);
        this.fifthNextPage.setDisable(true);
        this.lastPage.setDisable(true);

        this.listAlbum.setItems(FXCollections.observableArrayList(listAlbums.subList(((this.currentPageCounter - 1) * 10), listAlbums.size())));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstAlbumOfPage(this.listAlbum);
    }

    // ALBUM LISTENING ICON CLICKED
    public void albumListeningButtonClicked() {
        AlbumDto albumSelected = ListAlbumSelectionListener.getAlbumSelected();
        boolean hasMusicWithNoFile = false;
        boolean hasMusicWithWrongFile = false;

        if (albumSelected != null) {
            AlbumDao albumDao = new AlbumDao();
            AlbumDb albumSelectedToDb = AlbumMapper.toDb(albumSelected);

            hasMusicWithNoFile = albumDao.getMusiques(albumSelectedToDb)
                    .stream()
                    .anyMatch(musique -> musique.getNomFichierMusique() == null ||
                            "".equals(musique.getNomFichierMusique()));

            hasMusicWithWrongFile = Boolean.TRUE.equals(albumDao.getMusiques(albumSelectedToDb)
                    .stream()
                    .map(musique -> !(new File(musique.getNomFichierMusique()).exists()))
                    .reduce((wrongFile1, wrongFile2) -> wrongFile1 || wrongFile2)
                    .orElse(Boolean.FALSE));
        }

        if (albumSelected == null) {
            this.showAlbumSelectionErrorPopUp();
        } else if (hasMusicWithNoFile || hasMusicWithWrongFile) {
            MusicErrorModal.getMusicFileErrorAlert(TypeSource.ALBUM);
        } else {
            Stage stage = new Stage();

            try {
                ListenAlbumController listenAlbumController = new ListenAlbumController();
                listenAlbumController.initialize(getClass().getResource("/views/album/listenAlbum.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/listenAlbum.fxml"));
                stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, "Écoute d'un album"));
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
                homeStage.setTitle(this.informationsUtils.buildStageTitleBar(homeStage, "Modification d'un album"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ALBUM DELETING ICON CLICKED
    public void albumDeletingButtonClicked() {
        AlbumDto albumSelected = ListAlbumSelectionListener.getAlbumSelected();

        if (albumSelected == null) {
            this.showAlbumSelectionErrorPopUp();
        } else {
            CommonConfirmationModal.getDeleteConfirmationAlert(TypeSource.ALBUM, albumSelected);
        }
    }

    private void showAlbumSelectionErrorPopUp() {
        CommonErrorModal.getSelectionErrorAlert(TypeSource.ALBUM);
    }
}
