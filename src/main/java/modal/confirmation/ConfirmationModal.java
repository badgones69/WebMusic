package modal.confirmation;

import enums.TypeSource;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modal.Modal;
import utils.ModalUtils;

public class ConfirmationModal {

    public static Alert initAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous " + ModalUtils.feminiseWord("sûr", TypeSource.COMMON) + " de bien vouloir");
        alert.setContentText(alert.getContentText() + ModalUtils.getSystemLineSeparator());

        return alert;
    }

    public void initPane(Alert alert) {
        DialogPane pane = Modal.initPane(alert);

        ImageView infoImageView = new ImageView(getClass().getResource("/icons/question.png").toString());
        infoImageView.setFitHeight(60);
        infoImageView.setFitWidth(120);
        infoImageView.setPreserveRatio(true);

        pane.setGraphic(infoImageView);

        pane.getButtonTypes().clear();
        pane.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        EventHandler<KeyEvent> fireOnEnter = event -> {
            if (KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button) {
                ((Button) event.getTarget()).fire();
            }
        };

        pane.getButtonTypes()
                .stream()
                .map(pane::lookupButton)
                .forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, fireOnEnter));
    }
}
