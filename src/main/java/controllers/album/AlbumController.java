package controllers.album;

import enums.TypeAction;
import enums.TypeSource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modal.ActionSuccessModal;
import modal.AlbumErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;

public class AlbumController {

    private static final Logger LOG = LogManager.getLogger(AlbumController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // ALBUM'S NAME ERROR POP-UP STAGE
    protected static Stage albumTitleErrorStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getAlbumTitleErrorStage() {
        return albumTitleErrorStage;
    }

    public static void setAlbumTitleErrorStage(Stage albumTitleErrorStage) {
        AlbumController.albumTitleErrorStage = albumTitleErrorStage;
    }

    // ALBUM'S NAME ERROR POP-UP "OK" BUTTON CLICKED
    public void albumNameErrorCloseButtonClicked() {
        getAlbumTitleErrorStage().close();
    }

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getSuccessAlert(TypeSource.ALBUM, action);
    }

    protected void showYearErrorPopUp() {
        AlbumErrorModal.getAlbumYearErrorAlert();
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
