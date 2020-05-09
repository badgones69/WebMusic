package modal.generic.error;

import enums.TypeSource;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.generic.GenericModal;
import utils.LogUtils;
import utils.InformationsUtils;
import utils.ModalUtils;

public class CommonErrorModal {

    private CommonErrorModal() {
        LogUtils.generateConstructorLog(CommonErrorModal.class);
    }

    public static void getSelectionErrorAlert(TypeSource source) {
        Alert selectionErrorAlert = ErrorModal.initAlert();

        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + "Vous n'avez sélectionné ");
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + ModalUtils.feminiseWord("aucun", source));
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        selectionErrorAlert.setContentText(selectionErrorAlert.getContentText() + source.getLibelle() + " !");

        Stage selectionErrorStage = GenericModal.initStage(selectionErrorAlert);
        selectionErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(selectionErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(selectionErrorAlert);

        selectionErrorAlert.showAndWait();
    }

    public static void getTitleErrorAlert(TypeSource source) {
        Alert titleErrorAlert = ErrorModal.initAlert();

        titleErrorAlert.setContentText(titleErrorAlert.getContentText() + "Vous n'avez renseigné aucun titre");
        titleErrorAlert.setContentText(titleErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        titleErrorAlert.setContentText(titleErrorAlert.getContentText() + "pour ");
        titleErrorAlert.setContentText(titleErrorAlert.getContentText() + ModalUtils.getWordThisFeminised(source));
        titleErrorAlert.setContentText(titleErrorAlert.getContentText() + " " + source.getLibelle() + " !");

        Stage titleErrorStage = GenericModal.initStage(titleErrorAlert);
        titleErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(titleErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(titleErrorAlert);

        titleErrorAlert.showAndWait();
    }
}
