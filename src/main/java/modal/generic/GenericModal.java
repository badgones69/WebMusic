package modal.generic;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import utils.LogUtils;
import utils.StyleUtils;

public class GenericModal {

    private GenericModal() {
        LogUtils.generateConstructorLog(GenericModal.class);
    }

    public static Stage initStage(Alert alert) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setWidth(450);
        alertStage.setHeight(140);

        return alertStage;
    }

    public static DialogPane initPane(Alert alert) {
        DialogPane alertPane = alert.getDialogPane();
        alertPane.getStylesheets().add(StyleUtils.getStylesheet());
        alertPane.getStyleClass().addAll(StyleUtils.getPanelClass(), StyleUtils.getModalBodyClass());
        return alertPane;
    }
}
