package controllers.common;

import controllers.album.AddAlbumController;
import controllers.artist.AddArtistController;
import controllers.artist.ListArtistController;
import controllers.music.AddMusicController;
import controllers.music.ListMusicController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(MenuController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // ABOUT POP-UP STAGE
    private static Stage aboutStage;

    private final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * HOME PAGE CONTAINERS
     */

    @FXML
    BorderPane homeBorderPane = new BorderPane();
    @FXML
    Pane homePane = new Pane();
    @FXML
    VBox homeVBox = new VBox();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAboutStage() {
        return aboutStage;
    }

    private static void setAboutStage(Stage stage) {
        aboutStage = stage;
    }

    /**
     * HOME PAGE CONTAINERS INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSizes();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        homeBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        homeBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        homePane.setPrefWidth(primaryScreenBounds.getWidth());
        homePane.setPrefHeight(homeBorderPane.getPrefHeight() - 32);
        homeVBox.setPrefWidth(primaryScreenBounds.getWidth());
        homeVBox.setPrefHeight(homeBorderPane.getPrefHeight() - 32);
    }


    /**
     * ITEMS LISTENERS OF "WebMusic" MENU
     */

    public void appHomeItemClicked(ActionEvent actionEvent) {
        Stage homeStage = new Home().getHomeStage();
        homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Accueil"));
        try {
            homeStage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/views/common/home.fxml")));
        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
        homeStage.show();
    }

    public void appCloseItemClicked(ActionEvent actionEvent) {
        Stage appCloseConfirmationStage = new Home().getAppCloseConfirmationStage();
        appCloseConfirmationStage.show();
    }

    /**
     * ITEMS LISTENERS OF "Musique" MENU
     */

    public void listMusicItemClicked(ActionEvent actionEvent) {
        Stage homeStage = new Home().getHomeStage();

        try {
            ListMusicController listMusicController = new ListMusicController();
            listMusicController.initialize(getClass().getResource("/views/music/listMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/listMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des musiques"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    public void addMusicItemClicked(ActionEvent actionEvent) {
        Stage homeStage = new Home().getHomeStage();
        homeStage.show();

        try {
            AddMusicController addMusicController = new AddMusicController();
            addMusicController.initialize(getClass().getResource("/views/music/addMusic.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/addMusic.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Ajout d'une musique"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    /**
     * ITEMS LISTENERS OF "Album" MENU
     */

    /*public void listAlbumItemClicked() {
        Stage homeStage = new Home(.getHomeStage();
        homeStage.show();

        try {
            ListAlbumController listAlbumController = new ListAlbumController();
            listAlbumController.initialize(getClass().getResource("/views/album/listAlbum.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/listAlbum.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des albums"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }*/

    public void addAlbumItemClicked() {
        Stage homeStage = new Home().getHomeStage();
        homeStage.show();

        try {
            AddAlbumController addAlbumController = new AddAlbumController();
            addAlbumController.initialize(getClass().getResource("/views/album/addAlbum.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/addAlbum.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Ajout d'un album"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    /**
     * ITEMS LISTENERS OF "Artiste" MENU
     */

    public void listArtistItemClicked() {
        Stage homeStage = new Home().getHomeStage();
        homeStage.show();

        try {
            ListArtistController listArtistController = new ListArtistController();
            listArtistController.initialize(getClass().getResource("/views/artist/listArtist.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/artist/listArtist.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des artistes"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    public void addArtistItemClicked() {
        Stage homeStage = new Home().getHomeStage();
        homeStage.show();

        try {
            AddArtistController addArtistController = new AddArtistController();
            addArtistController.initialize(getClass().getResource("/views/artist/addArtist.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/artist/addArtist.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Ajout d'un(e) artiste"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    /**
     * ITEM LISTENER OF "Aide" MENU
     */

    public void aboutItemClicked(ActionEvent actionEvent) {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/help/about.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, "À propos"));
            stage.setScene(new Scene(root, 450, 140));
            this.setAboutStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
