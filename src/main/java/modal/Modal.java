package modal;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Modal {

    private static final Logger LOG = LogManager.getLogger(Modal.class);

    private Modal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static Stage initStage(Alert alert) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setWidth(450);
        alertStage.setHeight(140);

        return alertStage;
    }

    public static DialogPane initPane(Alert alert) {
        DialogPane aboutAlertPane = alert.getDialogPane();
        aboutAlertPane.getStylesheets().add("/styles/style.css");
        aboutAlertPane.getStyleClass().add("panel");
        aboutAlertPane.getStyleClass().add("modalBody");
        return aboutAlertPane;
    }
}
