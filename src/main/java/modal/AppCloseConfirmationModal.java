package modal;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.util.Optional;

public class AppCloseConfirmationModal {

    private static final Logger LOG = LogManager.getLogger(AppCloseConfirmationModal.class);

    private AppCloseConfirmationModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getAppCloseConfirmationAlert() {
        Alert appCloseConfirmationAlert = ConfirmationModal.initAlert();

        appCloseConfirmationAlert.setContentText(appCloseConfirmationAlert.getContentText() + "fermer l'application WebMusic ?");

        Stage appCloseConfirmationStage = Modal.initStage(appCloseConfirmationAlert);
        appCloseConfirmationStage.setTitle(new InformationsUtils().buildStageTitleBar(appCloseConfirmationStage, "Fermeture"));
        new ConfirmationModal().initPane(appCloseConfirmationAlert);

        Optional<ButtonType> result = appCloseConfirmationAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            Platform.exit();
        }
    }
}
