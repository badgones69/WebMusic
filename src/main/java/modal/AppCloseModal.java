package modal;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

import java.util.Optional;

public class AppCloseModal {

    private static final Logger LOG = LogManager.getLogger(AppCloseModal.class);

    private AppCloseModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getAppCloseAlert() {
        Alert appCloseAlert = ConfirmationModal.initAlert();

        appCloseAlert.setContentText("Êtes-vous sûr(e) de bien vouloir fermer");
        appCloseAlert.setContentText(appCloseAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        appCloseAlert.setContentText(appCloseAlert.getContentText() + "l'application WebMusic ?");

        Stage aboutStage = Modal.initStage(appCloseAlert);
        aboutStage.setTitle(new InformationsUtils().buildStageTitleBar(aboutStage, "Fermeture"));
        new ConfirmationModal().initPane(appCloseAlert);

        Optional<ButtonType> result = appCloseAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            Platform.exit();
        }
    }
}
