package controllers.artist;

import controllers.common.Home;
import dao.AuteurDao;
import db.AuteurDb;
import dto.AuteurDto;
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
import listeners.artist.ListArtistKeySelectionListener;
import listeners.artist.ListArtistMouseSelectionListener;
import listeners.artist.ListArtistSelectionListener;
import mapper.AuteurMapper;
import modal.generic.confirmation.CommonConfirmationModal;
import modal.generic.error.CommonErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ListUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListArtistController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(ListArtistController.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static final String PAGE = "Page : ";
    private static final String SUR = " / ";

    private Integer currentPageCounter;
    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * SCREEN COMPONENTS
     */

    @FXML
    private TableView<AuteurDto> listArtist = new TableView<>();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * ARTIST LIST COLUMNS
     */

    @FXML
    private TableColumn<AuteurDto, String> nomColumn = new TableColumn<>();

    @FXML
    private TableColumn<AuteurDto, String> prenomColumn = new TableColumn<>();

    /**
     * ARTIST LIST PAGINATION COMPONENTS
     */

    @FXML
    private Label nbArtist = new Label();

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
     * ARTIST LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeScreenComponents();
        ObservableList<AuteurDto> table = this.initializeListData();
        this.initializeListPagination(table);
    }

    private void initializeScreenComponents() {
        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(this.updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(this.deletingActionImageView, deletingActionTooltip);
    }

    private ObservableList<AuteurDto> initializeListData() {
        AuteurDao artisteDao = new AuteurDao();
        List<AuteurDb> artistesDb = artisteDao.findAll();
        List<AuteurDto> artistesDto = new ArrayList<>();
        ObservableList<AuteurDto> listArtistes = FXCollections.observableArrayList();

        for (AuteurDb artisteDb : artistesDb) {
            artistesDto.add(AuteurMapper.toDto(artisteDb));
        }

        listArtistes.addAll(artistesDto);
        listArtistes.sort((artist1, artist2) -> artist1.getNomAuteur().compareToIgnoreCase(artist2.getNomAuteur()));

        this.listArtist.getItems().clear();
        this.listArtist.setItems(listArtistes);

        for (int i = 0; i < this.listArtist.getItems().size(); i++) {

            this.nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomAuteur"));
            this.prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenomAuteur"));
            this.listArtist.getColumns().clear();
            this.listArtist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.listArtist.setOnMouseClicked(new ListArtistMouseSelectionListener());
            this.listArtist.setOnKeyReleased(new ListArtistKeySelectionListener());
            this.listArtist.getColumns().addAll(this.nomColumn, this.prenomColumn);
        }

        this.listArtist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Platform.runLater(() -> this.listArtist.refresh());

        return listArtistes;
    }

    private void initializeListPagination(ObservableList<AuteurDto> listArtists) {
        Integer countArtist = listArtists.size();
        Integer countPage = ListUtils.getCountPage(countArtist);

        this.nbArtist.setText("Nombre total d'artistes : " + countArtist);

        /**
         * PAGINATION INITIALIZATION
         */

        final Integer finalCountPage = countPage;

        Tooltip firstPageTooltip = new Tooltip("Première page");
        Tooltip.install(this.firstPage, firstPageTooltip);
        this.firstPage.setOnAction((ActionEvent event) -> this.goToFirstPage(finalCountPage, listArtists));

        Tooltip fifthPreviousPageTooltip = new Tooltip("5ème page précédente");
        Tooltip.install(this.fifthPreviousPage, fifthPreviousPageTooltip);
        this.fifthPreviousPage.setOnAction((ActionEvent event) -> this.goToFifthPreviousPage(finalCountPage, listArtists));

        Tooltip previousPageTooltip = new Tooltip("Page précédente");
        Tooltip.install(this.previousPage, previousPageTooltip);
        this.previousPage.setOnAction((ActionEvent event) -> this.goToPreviousPage(finalCountPage, listArtists));

        Tooltip nextPageTooltip = new Tooltip("Page suivante");
        Tooltip.install(this.nextPage, nextPageTooltip);
        this.nextPage.setOnAction((ActionEvent event) -> this.goToNextPage(finalCountPage, listArtists));

        Tooltip fifthNextPageTooltip = new Tooltip("5ème page suivante");
        Tooltip.install(this.fifthNextPage, fifthNextPageTooltip);
        this.fifthNextPage.setOnAction((ActionEvent event) -> this.goToFifthNextPage(finalCountPage, listArtists));

        Tooltip lastPageTooltip = new Tooltip("Dernière page");
        Tooltip.install(this.lastPage, lastPageTooltip);
        this.lastPage.setOnAction((ActionEvent event) -> this.goToLastPage(finalCountPage, listArtists));

        this.goToFirstPage(countPage, listArtists);
    }

    // METHOD TO GO TO THE FIRST PAGE OF ARTIST LIST
    private void goToFirstPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter = 1;

        this.firstPage.setDisable(true);
        this.fifthPreviousPage.setDisable(true);
        this.previousPage.setDisable(true);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(countPage == 1);
        this.fifthNextPage.setDisable(countPage <= 6);
        this.lastPage.setDisable(countPage == 1);

        this.listArtist.setItems(countPage == 1 ? listArtists : FXCollections.observableArrayList(listArtists.subList(0, 10)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstArtistOfPage(this.listArtist);
    }

    // METHOD TO GO TO THE FIFTH PREVIOUS PAGE OF ARTIST LIST
    private void goToFifthPreviousPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter = this.currentPageCounter - 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listArtist.setItems(FXCollections.observableArrayList(listArtists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstArtistOfPage(this.listArtist);
    }

    // METHOD TO GO TO THE PREVIOUS PAGE OF ARTIST LIST
    private void goToPreviousPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter--;

        if (this.currentPageCounter == 1) {
            this.goToFirstPage(countPage, listArtists);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.nextPage.setDisable(false);
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
            this.lastPage.setDisable(false);

            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            this.listArtist.setItems(FXCollections.observableArrayList(listArtists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            ListUtils.selectFirstArtistOfPage(this.listArtist);
        }
    }

    // METHOD TO GO TO THE NEXT PAGE OF ARTIST LIST
    private void goToNextPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter++;

        if (countPage.intValue() == this.currentPageCounter.intValue()) {
            this.goToLastPage(countPage, listArtists);
        } else {
            this.firstPage.setDisable(false);
            this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
            this.previousPage.setDisable(false);
            this.currentPage.setText(this.currentPageCounter.toString());
            this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);

            this.listArtist.setItems(FXCollections.observableArrayList(listArtists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
            this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
            ListUtils.selectFirstArtistOfPage(this.listArtist);
        }
    }

    // METHOD TO GO TO THE FIFTH NEXT PAGE OF ARTIST LIST
    private void goToFifthNextPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter = this.currentPageCounter + 5;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(false);
        this.fifthNextPage.setDisable(countPage - this.currentPageCounter <= 5);
        this.lastPage.setDisable(false);

        this.listArtist.setItems(FXCollections.observableArrayList(listArtists.subList(((this.currentPageCounter - 1) * 10), 10*this.currentPageCounter)));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstArtistOfPage(this.listArtist);
    }

    // METHOD TO GO TO THE LAST PAGE OF ARTIST LIST
    private void goToLastPage(Integer countPage, ObservableList<AuteurDto> listArtists) {
        this.currentPageCounter = countPage;

        this.firstPage.setDisable(false);
        this.fifthPreviousPage.setDisable(this.currentPageCounter <= 6);
        this.previousPage.setDisable(false);
        this.currentPage.setText(this.currentPageCounter.toString());
        this.nextPage.setDisable(true);
        this.fifthNextPage.setDisable(true);
        this.lastPage.setDisable(true);

        this.listArtist.setItems(FXCollections.observableArrayList(listArtists.subList(((this.currentPageCounter - 1) * 10), listArtists.size())));
        this.nbPage.setText(PAGE + this.currentPage.getText() + SUR + countPage);
        ListUtils.selectFirstArtistOfPage(this.listArtist);
    }

    // ARTIST EDITING ICON CLICKED
    public void artistEditingButtonClicked(MouseEvent mouseEvent) {
        if (ListArtistSelectionListener.getAuteurSelected() == null) {
            this.showArtistSelectionErrorPopUp();
        } else {
            Stage homeStage = new Home().getHomeStage();
            homeStage.show();

            try {
                EditArtistController editArtistController = new EditArtistController();
                editArtistController.initialize(getClass().getResource("/views/artist/editArtist.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/editArtist.fxml"));
                homeStage.setTitle(this.informationsUtils.buildStageTitleBar(homeStage, "Modification d'un(e) artiste"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ARTIST DELETING ICON CLICKED
    public void artistDeletingButtonClicked(MouseEvent mouseEvent) {
        AuteurDto auteurSelected = ListArtistSelectionListener.getAuteurSelected();

        if (auteurSelected == null) {
            this.showArtistSelectionErrorPopUp();
        } else {
            CommonConfirmationModal.getDeleteConfirmationAlert(TypeSource.ARTIST, auteurSelected);
        }
    }

    private void showArtistSelectionErrorPopUp() {
        CommonErrorModal.getSelectionErrorAlert(TypeSource.ARTIST);
    }
}