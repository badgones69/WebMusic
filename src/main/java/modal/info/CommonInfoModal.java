package modal.info;

import enums.TypeVersion;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.Modal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class CommonInfoModal {

    private static final Logger LOG = LogManager.getLogger(CommonInfoModal.class);
    private static InformationsUtils informationsUtils = new InformationsUtils();

    private CommonInfoModal() {
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

    public static void getCreditsAlert() {
        Alert creditsAlert = InfoModal.initAlert();

        creditsAlert.setContentText("SQLite : version " + informationsUtils.getVersion(TypeVersion.SQLITE));
        creditsAlert.setContentText(creditsAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        creditsAlert.setContentText(creditsAlert.getContentText() + "Java EE API : version " + informationsUtils.getVersion(TypeVersion.JAVA_EE_API));
        creditsAlert.setContentText(creditsAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        creditsAlert.setContentText(creditsAlert.getContentText() + "JUnit : version " + informationsUtils.getVersion(TypeVersion.JUNIT));
        creditsAlert.setContentText(creditsAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        creditsAlert.setContentText(creditsAlert.getContentText() + "Font Awesome : version " + informationsUtils.getVersion(TypeVersion.FONT_AWESOME));
        creditsAlert.setContentText(creditsAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        creditsAlert.setContentText(creditsAlert.getContentText() + "Log4j : version " + informationsUtils.getVersion(TypeVersion.LOG4J));

        Stage creditsStage = Modal.initStage(creditsAlert);
        creditsStage.setTitle(informationsUtils.buildStageTitleBar(creditsStage, "Crédits"));
        new InfoModal().initPane(creditsAlert);

        creditsAlert.showAndWait();
    }
}
