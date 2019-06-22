package modal.success;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import modal.Modal;

public class SuccessModal {
    public static final String SUCCESS_MODAL_TITLE = "Succès";

    public void initPane(Alert alert) {
        DialogPane pane = Modal.initPane(alert);

        ImageView infoImageView = new ImageView(getClass().getResource("/icons/success.png").toString());
        infoImageView.setFitHeight(60);
        infoImageView.setFitWidth(120);
        infoImageView.setPreserveRatio(true);

        pane.setGraphic(infoImageView);
    }

    public static Alert initAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        return alert;
    }
}
