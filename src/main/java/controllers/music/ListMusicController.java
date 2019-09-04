package controllers.music;

import controllers.common.Home;
import dao.MusiqueDao;
import db.MusiqueDb;
import dto.MusiqueDto;
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
import listeners.music.ListMusicKeySelectionListener;
import listeners.music.ListMusicMouseSelectionListener;
import listeners.music.ListMusicSelectionListener;
import mapper.MusiqueMapper;
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

public class ListMusicController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(ListMusicController.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static final String PAGE = "Page : ";
    private static final String SUR = " / ";

    // MUSIC LISTENING POP-UP STAGE
    private static Stage musicListenStage;

    private Integer currentPageCounter;
    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * SCREEN COMPONENTS
     */

    @FXML
    private TableView<MusiqueDto> listMusic = new TableView<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * MUSIC LIST COLUMNS
     */

    @FXML
    private TableColumn<MusiqueDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> artisteColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> dureeColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> albumColumn = new TableColumn<>();

    /**
     * MUSIC LIST PAGINATION COMPONENTS
     */

    @FXML
    private Label nbMusic = new Label();

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

    public static Stage getMusicListenStage() {
        return musicListenStage;
    }

    public static void setMusicListenStage(Stage musicListenStage) {
        ListMusicController.musicListenStage = musicListenStage;
    }

    /**
     * MUSIC LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeScreenComponents();
        ObservableList<MusiqueDto> table = this.initializeListData();
        this.initializeListPagination(table);
    }

    private void initializeScreenComponents() {
        ListMusicMouseSelectionListener.setMusiqueSelected(null);

        Tooltip listeningActionTooltip = new Tooltip("Écouter");
        Tooltip.install(this.listeningActionImageView, listeningActionTooltip);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(this.updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(this.deletingActionImageView, deletingActionTooltip);
    }

    private ObservableList<MusiqueDto> initializeListData() {
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<MusiqueDto> listMusics = FXCollections.observableArrayList();
        List<MusiqueDb> musiquesDb = musiqueDao.findAll();
        List<MusiqueDto> musiquesDto = new ArrayList<>();

        for (MusiqueDb musiqueDb : musiquesDb) {
            musiquesDto.add(MusiqueMapper.toDto(musiqueDb));
        }

        listMusics.addAll(musiquesDto);
        listMusics.sort((musique1, musique2) -> musique1.getTitreMusique().compareToIgnoreCase(musique2.getTitreMusique()));

        this.listMusic.getItems().clear();
        this.listMusic.setItems(listMusics);

        for (int i = 0; i < this.listMusic.getItems().size(); i++) {

            this.titreColumn.setCellValueFactory(new PropertyValueFactory<>("titreMusique"));
            this.artisteColumn.setCellValueFactory(new PropertyValueFactory<>("auteurs"));
            this.dureeColumn.setCellValueFactory(new PropertyValueFactory<>("dureeMusique"));
            this.albumColumn.setCellValueFactory(new PropertyValueFactory<>("titreAlbumMusique"));
            this.listMusic.getColumns().clear();
            this.listMusic.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.listMusic.setOnMouseClicked(new ListMusicMouseSelectionListener());
            this.listMusic.setOnKeyReleased(new ListMusicKeySelectionListener());
            this.listMusic.getColumns().addAll(this.titreColumn, this.artisteColumn, this.dureeColumn, this.albumColumn);
        }


        this.listMusic.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Platform.runLater(() -> listMusic.refresh());

        return listMusics;
    }

    private void initializeListPagination(ObservableList<MusiqueDto> listMusics) {
        Integer countMusic = listMusics.size();
        Integer countPage = ListUtils.getCountPage(countMusic);

        this.nbMusic.setText("Nombre total de musiques : " + countMusic);

        /**
         * PAGINATION INITIALIZATION
         */

        final Integer finalCountPage = countPage;

        Tooltip firstPageTooltip = new Tooltip("Première page");
        Tooltip.install(this.firstPage, firstPageTooltip);
        this.firstPage.setOnAction((ActionEvent event) -> this.goToFirstPage(finalCountPage, listMusics));

        Tooltip fifthPreviousPageTooltip = new Tooltip("5ème page précédente");
        Tooltip.install(this.fifthPreviousPage, fifthPreviousPageTooltip);
        this.fifthPreviousPage.setOnAction((ActionEvent event) -> this.goToFifthPreviousPage(finalCountPage, listMusics));

        Tooltip previousPageTooltip = new Tooltip("Page précédente");
        Tooltip.install(this.previousPage, previousPageTooltip);
        this.previousPage.setOnAction((ActionEvent event) -> this.goToPreviousPage(finalCountPage, listMusics));

        Tooltip nextPageTooltip = new Tooltip("Page suivante");
        Tooltip.install(this.nextPage, nextPageTooltip);
        this.nextPage.setOnAction((ActionEvent event) -> this.goToNextPage(finalCountPage, listMusics));

        Tooltip fifthNextPageTooltip = new Tooltip("5ème page suivante");
        Tooltip.install(this.fifthNextPage, fifthNextPageTooltip);
        this.fifthNextPage.setOnAction((ActionEvent event) -> this.goToFifthNextPage(finalCountPage, listMusics));

        Tooltip lastPageTooltip = new Tooltip("Dernière page");
        Tooltip.install(this.lastPage, lastPageTooltip);
        this.lastPage.setOnAction((ActionEvent event) -> this.goToLastPage(finalCountPage, listMusics));

        this.goToFirstPage(countPage, listMusics);
    }

    // METHOD TO GO TO THE FIRST PAGE OF MUSIC LIST
    private void goToFirstPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter = 1;

        this.firstPage.setDisable(true);
        this.fifthPreviousPage.setDisable(true);
        this.previousPage.setDisable(true);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(countPage == 1);
        this.fifthNextPage.setDisable(countPage <= 6);
        this.lastPage.setDisable(countPage == 1);

        this.listMusic.setItems(countPage == 1 ? listMusics : FXCollections.observableArrayList(listMusics.subList(0, 10)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstMusicOfPage(this.listMusic);
    }

    // METHOD TO GO TO THE FIFTH PREVIOUS PAGE OF MUSIC LIST
    private void goToFifthPreviousPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter = this.currentPageCounter - 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listMusic.setItems(FXCollections.observableArrayList(listMusics.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstMusicOfPage(this.listMusic);
    }

    // METHOD TO GO TO THE PREVIOUS PAGE OF MUSIC LIST
    private void goToPreviousPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter--;

        if (this.currentPageCounter == 1) {
            this.goToFirstPage(countPage, listMusics);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.nextPage.setDisable(false);
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
            this.lastPage.setDisable(false);

            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            this.listMusic.setItems(FXCollections.observableArrayList(listMusics.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            ListUtils.selectFirstMusicOfPage(this.listMusic);
        }
    }

    // METHOD TO GO TO THE NEXT PAGE OF MUSIC LIST
    private void goToNextPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter++;

        if (countPage.intValue() == this.currentPageCounter.intValue()) {
            this.goToLastPage(countPage, listMusics);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);

            this.listMusic.setItems(FXCollections.observableArrayList(listMusics.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            ListUtils.selectFirstMusicOfPage(this.listMusic);
        }
    }

    // METHOD TO GO TO THE FIFTH NEXT PAGE OF MUSIC LIST
    private void goToFifthNextPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter = this.currentPageCounter + 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listMusic.setItems(FXCollections.observableArrayList(listMusics.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstMusicOfPage(this.listMusic);
    }

    // METHOD TO GO TO THE LAST PAGE OF MUSIC LIST
    private void goToLastPage(Integer countPage, ObservableList<MusiqueDto> listMusics) {
        this.currentPageCounter = countPage;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(true);
        this.fifthNextPage.setDisable(true);
        this.lastPage.setDisable(true);

        this.listMusic.setItems(FXCollections.observableArrayList(listMusics.subList(((this.currentPageCounter - 1) * 10), listMusics.size())));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstMusicOfPage(this.listMusic);
    }

    // MUSIC LISTENING ICON CLICKED
    public void musicListeningButtonClicked(MouseEvent mouseEvent) {
        MusiqueDto musiqueSelected = ListMusicSelectionListener.getMusiqueSelected();
        boolean isMusicWithNoFile = false;
        boolean isMusicWithWrongFile = false;

        if (musiqueSelected != null) {
            String nomFichierMusique = musiqueSelected.getNomFichierMusique();

            isMusicWithNoFile = nomFichierMusique == null ||
                    "".equals(nomFichierMusique);

            if (!isMusicWithNoFile) {
                isMusicWithWrongFile = !(new File(nomFichierMusique).exists());
            }
        }

        if (musiqueSelected == null) {
            this.showMusicSelectionErrorPopUp();
        } else if (isMusicWithNoFile || isMusicWithWrongFile) {
            MusicErrorModal.getMusicFileErrorAlert(TypeSource.MUSIC);
        } else {
            Stage stage = new Stage();

            try {
                ListenMusicController listenMusicController = new ListenMusicController();
                listenMusicController.initialize(getClass().getResource("/views/music/listenMusic.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/listenMusic.fxml"));
                stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, "Écoute d'une musique"));
                stage.setScene(new Scene(root, 600, 260));
                setMusicListenStage(stage);

                getMusicListenStage().setOnCloseRequest(event -> ListenMusicController.getMediaPlayer().stop());

                musicListenStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // MUSIC EDITING ICON CLICKED
    public void musicEditingButtonClicked() {
        if (ListMusicSelectionListener.getMusiqueSelected() == null) {
            this.showMusicSelectionErrorPopUp();
        } else {
            Stage homeStage = new Home().getHomeStage();
            homeStage.show();

            try {
                EditMusicController editMusicController = new EditMusicController();
                editMusicController.initialize(getClass().getResource("/views/music/editMusic.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/music/editMusic.fxml"));
                homeStage.setTitle(this.informationsUtils.buildStageTitleBar(homeStage, "Modification d'une musique"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // MUSIC DELETING ICON CLICKED
    public void musicDeletingButtonClicked() {
        MusiqueDto musiqueSelected = ListMusicSelectionListener.getMusiqueSelected();

        if (musiqueSelected == null) {
            this.showMusicSelectionErrorPopUp();
        } else {
            CommonConfirmationModal.getDeleteConfirmationAlert(TypeSource.MUSIC, musiqueSelected);
        }
    }

    private void showMusicSelectionErrorPopUp() {
        CommonErrorModal.getSelectionErrorAlert(TypeSource.MUSIC);
    }
}
