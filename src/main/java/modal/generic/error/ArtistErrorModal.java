package modal.generic.error;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.generic.GenericModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class ArtistErrorModal {

    private static final Logger LOG = LogManager.getLogger(ArtistErrorModal.class);

    private ArtistErrorModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getArtistNameErrorAlert() {
        Alert artistNameErrorAlert = ErrorModal.initAlert();

        artistNameErrorAlert.setContentText(artistNameErrorAlert.getContentText() + "Vous n'avez renseigné aucun nom ou");
        artistNameErrorAlert.setContentText(artistNameErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        artistNameErrorAlert.setContentText(artistNameErrorAlert.getContentText() + "pseudo pour cet(te) artiste ! ");

        Stage artistNameErrorStage = GenericModal.initStage(artistNameErrorAlert);
        artistNameErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(artistNameErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(artistNameErrorAlert);

        artistNameErrorAlert.showAndWait();
    }
}
