package controllers.artist;

import controllers.common.Home;
import dao.AuteurDao;
import db.AuteurDb;
import dto.AuteurDto;
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
import listeners.ListArtistSelectionListener;
import mapper.AuteurMapper;
import modal.confirmation.CommonConfirmationModal;
import modal.error.CommonErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListArtistController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(ListArtistController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * ARTIST LIST COLUMNS
     */

    @FXML
    private TableView<AuteurDto> listArtist = new TableView<>();

    @FXML
    private TableColumn<AuteurDto, String> nomColumn = new TableColumn<>();

    @FXML
    private TableColumn<AuteurDto, String> prenomColumn = new TableColumn<>();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * ARTIST LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeList();
    }

    private void initializeSizes() {
        listArtist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeList() {
        ListArtistSelectionListener.setAuteurSelected(null);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(deletingActionImageView, deletingActionTooltip);

        AuteurDao artisteDao = new AuteurDao();
        ObservableList<AuteurDto> listArtistes = FXCollections.observableArrayList();
        List<AuteurDb> artistesDb = artisteDao.findAll();
        List<AuteurDto> artistesDto = new ArrayList<>();

        for (AuteurDb artisteDb : artistesDb) {
            artistesDto.add(AuteurMapper.toDto(artisteDb));
        }

        for (AuteurDto artisteDto : artistesDto) {
            listArtistes.add(artisteDto);
        }

        this.listArtist.getItems().clear();
        this.listArtist.setItems(listArtistes);

        for (int i = 0; i < this.listArtist.getItems().size(); i++) {

            nomColumn.setCellValueFactory(new PropertyValueFactory<AuteurDto, String>("nomAuteur"));
            prenomColumn.setCellValueFactory(new PropertyValueFactory<AuteurDto, String>("prenomAuteur"));
            listArtist.getColumns().clear();
            listArtist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listArtist.setOnMouseClicked(new ListArtistSelectionListener());
            listArtist.getColumns().addAll(nomColumn, prenomColumn);
            listArtist.getSortOrder().add(nomColumn);
        }

        Platform.runLater(() -> listArtist.refresh());
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
                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Modification d'un(e) artiste"));
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