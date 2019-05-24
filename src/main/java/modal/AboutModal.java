package modal;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class AboutModal {

    private static final Logger LOG = LogManager.getLogger(AboutModal.class);

    private AboutModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getAboutAlert() {
        Alert aboutAlert = InfoModal.initAlert();

        aboutAlert.setContentText("Auteur : Florian RENARD (France)");
        aboutAlert.setContentText(aboutAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        aboutAlert.setContentText(aboutAlert.getContentText() + "Licence : GNU General Public License v3.0");

        Stage aboutStage = Modal.initStage(aboutAlert);
        aboutStage.setTitle(new InformationsUtils().buildStageTitleBar(aboutStage, "À propos"));
        new InfoModal().initPane(aboutAlert);

        aboutAlert.showAndWait();
    }
}
