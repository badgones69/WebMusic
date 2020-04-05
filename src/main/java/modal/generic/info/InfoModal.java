package modal.generic.info;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import modal.generic.GenericModal;

public class InfoModal {
    public static Alert initAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);

        return alert;
    }

    public void initPane(Alert alert) {
        DialogPane pane = GenericModal.initPane(alert);

        ImageView infoImageView = new ImageView(getClass().getResource("/icons/info.png").toString());
        infoImageView.setFitHeight(60);
        infoImageView.setFitWidth(120);
        infoImageView.setPreserveRatio(true);

        pane.setGraphic(infoImageView);
    }
}
