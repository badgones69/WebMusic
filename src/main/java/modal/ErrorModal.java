package modal;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;

public class ErrorModal {
    public static final String ERROR_MODAL_TITLE = "Erreur";

    public void initPane(Alert alert) {
        DialogPane pane = Modal.initPane(alert);

        ImageView infoImageView = new ImageView(getClass().getResource("/icons/error.png").toString());
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
