package modal;

import enums.TypeSource;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class SelectionErrorModal {

    private static final Logger LOG = LogManager.getLogger(SelectionErrorModal.class);

    private SelectionErrorModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getSelectionErrorAlert(TypeSource source) {
        Alert selectionErrorAlert = ErrorModal.initAlert();

        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + "Vous n'avez sélectionné ");
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + ModalUtils.feminiseWord("aucun", source));
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + source.getLibelle() + " !");

        Stage selectionErrorStage = Modal.initStage(selectionErrorAlert);
        selectionErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(selectionErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(selectionErrorAlert);

        selectionErrorAlert.showAndWait();
    }
}
