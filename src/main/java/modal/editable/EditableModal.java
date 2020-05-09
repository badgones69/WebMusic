package modal.editable;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utils.LogUtils;
import utils.StyleUtils;

public class EditableModal {

    private EditableModal() {
        LogUtils.generateConstructorLog(EditableModal.class);
    }

    public static Stage initStage(Alert alert) {
        return (Stage) alert.getDialogPane().getScene().getWindow();
    }

    public static void initPane(Alert alert) {
        DialogPane alertPane = alert.getDialogPane();
        alertPane.getStylesheets().add(StyleUtils.getStylesheet());
        alertPane.getStyleClass().addAll(StyleUtils.getPanelClass(), StyleUtils.getModalBodyClass());
        alertPane.setGraphic(null);

        alertPane.getButtonTypes().clear();
        alertPane.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        EventHandler<KeyEvent> fireOnEnter = event -> {
            if (KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button) {
                ((Button) event.getTarget()).fire();
            }
        };

        alertPane.getButtonTypes()
                .stream()
                .map(alertPane::lookupButton)
                .forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, fireOnEnter));
    }
}
