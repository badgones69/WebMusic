package controllers.artist;

import controllers.common.Home;
import dao.AuteurDao;
import db.AuteurDb;
import dto.AuteurDto;
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
import listeners.listArtist.ListArtistSelectionListener;
import mapper.AuteurMapper;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListArtistController implements Initializable {

    // ARTIST SELECTION ERROR POP-UP STAGE
    private static Stage artistSelectionErrorStage;

    // ARTIST DELETING CONFIRMATION POP-UP STAGE
    private static Stage artistDeleteConfirmationStage;

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
     * GETTERS AND SETTERS
     */

    public static Stage getArtistDeleteConfirmationStage() {
        return artistDeleteConfirmationStage;
    }

    public static void setArtistDeleteConfirmationStage(Stage artistDeleteConfirmationStage) {
        ListArtistController.artistDeleteConfirmationStage = artistDeleteConfirmationStage;
    }

    public static Stage getArtistSelectionErrorStage() {
        return artistSelectionErrorStage;
    }

    public static void setArtistSelectionErrorStage(Stage artistSelectionErrorStage) {
        ListArtistController.artistSelectionErrorStage = artistSelectionErrorStage;
    }

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

    // ARTIST SELECTION ERROR POP-UP "OK" BUTTON CLICKED
    public void artistSelectionErrorCloseButtonClicked(ActionEvent actionEvent) {
        getArtistSelectionErrorStage().close();
    }
    
    // ARTIST EDITING ICON CLICKED
    public void artistEditingButtonClicked(MouseEvent mouseEvent) {
        if(ListArtistSelectionListener.getAuteurSelected() == null) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/errors/artistSelectionError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 455, 140));
                ListArtistController.setArtistSelectionErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Stage homeStage = Home.getHomeStage();
            homeStage.show();

            try {
                EditArtistController editArtistController = new EditArtistController();
                editArtistController.initialize(getClass().getResource("/views/artist/editArtist.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/editArtist.fxml"));
                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Modification d'un(e) artiste"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ARTIST DELETING ICON CLICKED
    public void artistDeletingButtonClicked(MouseEvent mouseEvent) {
        if(ListArtistSelectionListener.getAuteurSelected() == null) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/errors/artistSelectionError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 455, 140));
                ListArtistController.setArtistSelectionErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/deleteArtistConfirmation.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Suppression d'un(e) artiste"));
                stage.setScene(new Scene(root, 630, 140));
                setArtistDeleteConfirmationStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}