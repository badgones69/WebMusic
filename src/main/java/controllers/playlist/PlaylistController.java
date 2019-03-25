package controllers.playlist;

import controllers.common.Home;
import dao.MusiqueDao;
import db.MusiqueDb;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoTestsUtils;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(PlaylistController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // PLAYLIST'S TITLE ERROR POP-UP STAGE
    protected static Stage playlistTitleErrorStage;

    // PLAYLIST'S MUSIC(S) ERROR POP-UP STAGE
    protected static Stage playlistMusicErrorStage;

    // PLAYLIST ACTION SUCCESSFUL POP-UP STAGE
    protected static Stage playlistActionSuccessStage;

    // PLAYLIST LIST PAGE STAGE
    protected static Stage listPlaylistStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    // PLAYLIST ACTION SUCCESSFUL POP-UP LABEL
    @FXML
    private Label playlistActionSuccessLabel = new Label();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getPlaylistTitleErrorStage() {
        return playlistTitleErrorStage;
    }

    public static void setPlaylistTitleErrorStage(Stage playlistTitleErrorStage) {
        PlaylistController.playlistTitleErrorStage = playlistTitleErrorStage;
    }

    public static Stage getPlaylistMusicErrorStage() {
        return playlistMusicErrorStage;
    }

    public static void setPlaylistMusicErrorStage(Stage playlistMusicErrorStage) {
        PlaylistController.playlistMusicErrorStage = playlistMusicErrorStage;
    }

    public static Stage getPlaylistActionSuccessStage() {
        return playlistActionSuccessStage;
    }

    public static void setPlaylistActionSuccessStage(Stage playlistActionSuccessStage) {
        PlaylistController.playlistActionSuccessStage = playlistActionSuccessStage;
    }

    public static Stage getListPlaylistStage() {
        return listPlaylistStage;
    }

    public static void setListPlaylistStage(Stage listPlaylistStage) {
        PlaylistController.listPlaylistStage = listPlaylistStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.playlistActionSuccessLabel.setText("Votre playlist a bien été " + PopUpUtils.getActionDone() + ".");
    }

    // PLAYLIST'S TITLE ERROR POP-UP "OK" BUTTON CLICKED
    public void playlistTitleErrorCloseButtonClicked(ActionEvent actionEvent) {
        getPlaylistTitleErrorStage().close();
    }

    // PLAYLIST'S MUSIC(S) ERROR POP-UP "OK" BUTTON CLICKED
    public void playlistMusicErrorCloseButtonClicked(ActionEvent actionEvent) {
        getPlaylistMusicErrorStage().close();
    }

    // PLAYLIST ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void playlistActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getPlaylistActionSuccessStage().close();

        Stage homeStage = new Home().getHomeStage();

        try {
            ListPlaylistController listPlaylistController = new ListPlaylistController();
            listPlaylistController.initialize(getClass().getResource("/views/playlist/listPlaylist.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/listPlaylist.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des playlists"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected List<MusiqueDb> setMusicsToPlaylist(ObservableList<Label> listMusics) {
        List<MusiqueDb> musiquesPlaylist = new ArrayList<>();
        MusiqueDao musiqueDao = new MusiqueDao();

        for (int i = 0; i < listMusics.size(); i++) {
            MusiqueDb musique = new MusiqueDb();

            String titreMusique = listMusics.get(i).getText();
            musique.setTitreMusique(titreMusique);
            DaoTestsUtils.setCodeToMusique(musique);
            musique = musiqueDao.find(musique.getCodeMusique());

            musiquesPlaylist.add(musique);
        }
        return musiquesPlaylist;
    }

    protected void showSuccessPopUp(String action) {
        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone(action);
            PlaylistController playlistController = new PlaylistController();
            playlistController.initialize(getClass().getResource("/views/playlist/playlistActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/playlistActionSuccess.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 390, 140));
            this.setPlaylistActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showMusicErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/errors/playlistMusicError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 415, 140));
            this.setPlaylistMusicErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showTitleErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/errors/playlistTitleError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 330, 140));
            this.setPlaylistTitleErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
