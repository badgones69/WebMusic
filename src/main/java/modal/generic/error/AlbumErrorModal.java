package modal.generic.error;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.generic.GenericModal;
import utils.LogUtils;
import utils.InformationsUtils;
import utils.ModalUtils;

public class AlbumErrorModal {

    private AlbumErrorModal() {
        LogUtils.generateConstructorLog(AlbumErrorModal.class);
    }

    public static void getAlbumYearErrorAlert() {
        Alert albumYearErrorAlert = ErrorModal.initAlert();

        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + "Vous avez renseigné une année invalide");
        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        albumYearErrorAlert.setContentText(albumYearErrorAlert.getContentText() + "pour cet album ! ");

        Stage albumYearErrorStage = GenericModal.initStage(albumYearErrorAlert);
        albumYearErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(albumYearErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(albumYearErrorAlert);

        albumYearErrorAlert.showAndWait();
    }
}
