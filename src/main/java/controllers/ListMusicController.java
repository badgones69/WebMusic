package controllers;

import controllers.home.Main;
import dao.MusiqueDao;
import db.MusiqueDb;
import dto.MusiqueDto;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import listeners.listMusic.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListMusicController implements Initializable {

    // MUSIC SELECTION ERROR POP-UP STAGE
    private static Stage musicSelectionErrorStage;

    // MUSIC DELETING CONFIRMATION POP-UP STAGE
    private static Stage musicDeleteConfirmationStage;

    // MUSIC LISTENING POP-UP STAGE
    private static Stage musicListenStage;

    private InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * MUSIC LIST COLUMNS
     */

    @FXML
    private TableView<MusiqueDto> listMusic = new TableView<>();

    @FXML
    private TableColumn<MusiqueDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> artisteColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> dureeColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> albumColumn = new TableColumn<>();

    @FXML
    private ImageView listeningActionImageView = new ImageView();

    @FXML
    private ImageView updatingActionImageView = new ImageView();

    @FXML
    private ImageView deletingActionImageView = new ImageView();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getMusicDeleteConfirmationStage() {
        return musicDeleteConfirmationStage;
    }

    public static void setMusicDeleteConfirmationStage(Stage musicDeleteConfirmationStage) {
        ListMusicController.musicDeleteConfirmationStage = musicDeleteConfirmationStage;
    }

    public static Stage getMusicListenStage() {
        return musicListenStage;
    }

    public static void setMusicListenStage(Stage musicListenStage) {
        ListMusicController.musicListenStage = musicListenStage;
    }

    public static Stage getMusicSelectionErrorStage() {
        return musicSelectionErrorStage;
    }

    public static void setMusicSelectionErrorStage(Stage musicSelectionErrorStage) {
        ListMusicController.musicSelectionErrorStage = musicSelectionErrorStage;
    }

    /**
     * MUSIC LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeList();
    }

    private void initializeSizes() {
        listMusic.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeList() {
        ListMusicSelectionListener.setMusiqueSelected(null);

        Tooltip listeningActionTooltip = new Tooltip("Écouter");
        Tooltip.install(listeningActionImageView, listeningActionTooltip);

        Tooltip updatingActionTooltip = new Tooltip("Modifier");
        Tooltip.install(updatingActionImageView, updatingActionTooltip);

        Tooltip deletingActionTooltip = new Tooltip("Supprimer");
        Tooltip.install(deletingActionImageView, deletingActionTooltip);

        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<MusiqueDto> listMusiques = FXCollections.observableArrayList();
        List<MusiqueDb> musiquesDb = musiqueDao.findAll();
        List<MusiqueDto> musiquesDto = new ArrayList<>();

        for (MusiqueDb musiqueDb : musiquesDb) {
            musiquesDto.add(MusiqueMapper.toDto(musiqueDb));
        }

        for (MusiqueDto musiqueDto : musiquesDto) {
            listMusiques.add(musiqueDto);
        }

        this.listMusic.getItems().clear();
        this.listMusic.setItems(listMusiques);

        for (int i = 0; i < this.listMusic.getItems().size(); i++) {

            MusiqueDto musiqueDto = this.listMusic.getItems().get(i);

            titreColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("titreMusique"));
            artisteColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("auteurs"));
            dureeColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("dureeMusique"));
            albumColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("titreAlbumMusique"));
            listMusic.getColumns().clear();
            listMusic.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listMusic.setOnMouseClicked(new ListMusicSelectionListener());
            listMusic.getColumns().addAll(titreColumn, artisteColumn, dureeColumn, albumColumn);
            listMusic.getSortOrder().add(titreColumn);
        }
    }

    // MUSIC SELECTION ERROR POP-UP "OK" BUTTON CLICKED
    public void musicSelectionErrorCloseButtonClicked(ActionEvent actionEvent) {
        getMusicSelectionErrorStage().close();
    }

    // MUSIC LISTENING ICON CLICKED
    public void musicListeningButtonClicked(MouseEvent mouseEvent) {
        if(ListMusicSelectionListener.getMusiqueSelected() == null) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicSelectionError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 455, 140));
                ListMusicController.setMusicSelectionErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Stage stage = new Stage();

            try {
                ListenMusicController listenMusicController = new ListenMusicController();
                listenMusicController.initialize(getClass().getResource("/views/listenMusic.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/listenMusic.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Écoute d'une musique"));
                stage.setScene(new Scene(root, 600, 260));
                setMusicListenStage(stage);

                getMusicListenStage().setOnCloseRequest(event -> {
                    ListenMusicController.getMediaPlayer().dispose();
                });

                musicListenStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    // MUSIC EDITING ICON CLICKED
    public void musicEditingButtonClicked(MouseEvent mouseEvent) {
        if(ListMusicSelectionListener.getMusiqueSelected() == null) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicSelectionError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 455, 140));
                ListMusicController.setMusicSelectionErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Stage homeStage = Main.getHomeStage();
            homeStage.show();

            try {
                EditMusicController editMusicController = new EditMusicController();
                editMusicController.initialize(getClass().getResource("/views/editMusic.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/editMusic.fxml"));
                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Modification d'une musique"));
                homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // MUSIC DELETING ICON CLICKED
    public void musicDeletingButtonClicked(MouseEvent mouseEvent) {
        if(ListMusicSelectionListener.getMusiqueSelected() == null) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/musicSelectionError.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 455, 140));
                ListMusicController.setMusicSelectionErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/deleteMusicConfirmation.fxml"));
                stage.setTitle(informationsUtils.buildStageTitleBar(stage, "Suppression d'une musique"));
                stage.setScene(new Scene(root, 630, 140));
                setMusicDeleteConfirmationStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}