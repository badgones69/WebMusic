package controllers.album;

import controllers.common.Home;
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
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlbumController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(AlbumController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // ALBUM'S NAME ERROR POP-UP STAGE
    protected static Stage albumTitleErrorStage;

    // ALBUM'S YEAR ERROR POP-UP STAGE
    protected static Stage albumYearErrorStage;

    // ALBUM ACTION SUCCESSFUL POP-UP STAGE
    protected static Stage albumActionSuccessStage;

    // ALBUM LIST PAGE STAGE
    protected static Stage listAlbumStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    // ALBUM ACTION SUCCESSFUL POP-UP LABEL
    @FXML
    private Label albumActionSuccessLabel = new Label();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAlbumTitleErrorStage() {
        return albumTitleErrorStage;
    }

    public static void setAlbumTitleErrorStage(Stage albumTitleErrorStage) {
        AlbumController.albumTitleErrorStage = albumTitleErrorStage;
    }

    public static Stage getAlbumYearErrorStage() {
        return albumYearErrorStage;
    }

    public static void setAlbumYearErrorStage(Stage albumYearErrorStage) {
        AlbumController.albumYearErrorStage = albumYearErrorStage;
    }

    public static Stage getAlbumActionSuccessStage() {
        return albumActionSuccessStage;
    }

    public static void setAlbumActionSuccessStage(Stage albumActionSuccessStage) {
        AlbumController.albumActionSuccessStage = albumActionSuccessStage;
    }

    public static Stage getListAlbumStage() {
        return listAlbumStage;
    }

    public static void setListAlbumStage(Stage listAlbumStage) {
        AlbumController.listAlbumStage = listAlbumStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.albumActionSuccessLabel.setText("Votre album a bien été " + PopUpUtils.getActionDone() + ".");
    }

    // ALBUM'S NAME ERROR POP-UP "OK" BUTTON CLICKED
    public void albumNameErrorCloseButtonClicked(ActionEvent actionEvent) {
        getAlbumTitleErrorStage().close();
    }

    // ALBUM'S YEAR ERROR POP-UP "OK" BUTTON CLICKED
    public void albumYearErrorCloseButtonClicked(ActionEvent actionEvent) {
        getAlbumYearErrorStage().close();
    }

    // ALBUM ACTION SUCCESSFUL POP-UP "OK" BUTTON CLICKED
    public void albumActionSuccessCloseButtonClicked(ActionEvent actionEvent) {
        getAlbumActionSuccessStage().close();

        Stage homeStage = new Home().getHomeStage();

        try {
            ListAlbumController listMusicController = new ListAlbumController();
            listMusicController.initialize(getClass().getResource("/views/album/listAlbum.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/listAlbum.fxml"));
            homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, "Liste des albums"));
            homeStage.setScene(new Scene(root, homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
            homeStage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showSuccessPopUp(String action) {
        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone(action);
            AlbumController albumController = new AlbumController();
            albumController.initialize(getClass().getResource("/views/album/albumActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/albumActionSuccess.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 390, 140));
            this.setAlbumActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showYearErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/errors/albumYearError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 330, 140));
            this.setAlbumYearErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    protected void showTitleErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/errors/albumTitleError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 350, 140));
            this.setAlbumTitleErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
