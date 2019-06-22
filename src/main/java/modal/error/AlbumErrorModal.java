package modal.error;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.Modal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class AlbumErrorModal {

    private static final Logger LOG = LogManager.getLogger(AlbumErrorModal.class);

    private AlbumErrorModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getAlbumYearErrorAlert() {
        Alert albumYearErrorAlert = ErrorModal.initAlert();

        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + "Vous avez renseigné une année invalide");
        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + "pour cet album ! ");

        Stage albumYearErrorStage = Modal.initStage(albumYearErrorAlert);
        albumYearErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(albumYearErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(albumYearErrorAlert);

        albumYearErrorAlert.showAndWait();
    }
}
