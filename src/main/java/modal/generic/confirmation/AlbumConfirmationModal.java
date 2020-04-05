package modal.generic.confirmation;

import db.AlbumDb;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import modal.editable.AlbumModal;
import modal.generic.GenericModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

import java.util.Optional;

public class AlbumConfirmationModal {

    private static final Logger LOG = LogManager.getLogger(AlbumConfirmationModal.class);

    private AlbumConfirmationModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static AlbumDb getAlbumSelectChoiceConfirmationAlert(AlbumDb albumPreSelected) {
        AlbumModal albumModal = new AlbumModal();

        Alert albumSelectChoiceConfirmationAlert = ConfirmationModal.initAlert();

        albumSelectChoiceConfirmationAlert.setContentText("Comment souhaitez-vous remplacer");
        albumSelectChoiceConfirmationAlert.setContentText(albumSelectChoiceConfirmationAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        albumSelectChoiceConfirmationAlert.setContentText(albumSelectChoiceConfirmationAlert.getContentText() + "l'abum actuellement sélectionné ?");

        Stage albumSelectChoiceConfirmationStage = GenericModal.initStage(albumSelectChoiceConfirmationAlert);
        albumSelectChoiceConfirmationStage.setTitle(new InformationsUtils().buildStageTitleBar(albumSelectChoiceConfirmationStage, "Sélection d'un album"));
        new ConfirmationModal().initPane(albumSelectChoiceConfirmationAlert);

        Button addNewAlbumButton = (Button) albumSelectChoiceConfirmationAlert.getDialogPane().lookupButton(ButtonType.YES);
        addNewAlbumButton.setText("Nouveau");
        FontAwesomeIconView addIcon = new FontAwesomeIconView();
        addIcon.setGlyphName("PLUS");
        addIcon.setSize("15.0");
        addNewAlbumButton.setGraphic(addIcon);


        Button searchOtherExistingAlbumButton = (Button) albumSelectChoiceConfirmationAlert.getDialogPane().lookupButton(ButtonType.NO);
        searchOtherExistingAlbumButton.setText("Autre existant");
        FontAwesomeIconView searchIcon = new FontAwesomeIconView();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setSize("15.0");
        searchOtherExistingAlbumButton.setGraphic(searchIcon);

        Optional<ButtonType> result = albumSelectChoiceConfirmationAlert.showAndWait();

        if (result.isPresent() && ButtonType.NO.equals(result.get())) {
            AlbumModal.setCurrentAlbumPreSelected(albumPreSelected);
            return albumModal.getSelectAlbumAlert();
        } else {
            AlbumModal.setCurrentAlbumPreSelected(albumPreSelected);
            return albumModal.getAddNewAlbumAlert();
        }
    }
}
