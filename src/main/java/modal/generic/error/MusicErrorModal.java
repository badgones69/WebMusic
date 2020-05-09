package modal.generic.error;

import enums.TypeSource;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.generic.GenericModal;
import utils.LogUtils;
import utils.InformationsUtils;
import utils.ModalUtils;

public class MusicErrorModal {

    private MusicErrorModal() {
        LogUtils.generateConstructorLog(MusicErrorModal.class);
    }

    public static void getMusicArtistErrorAlert() {
        Alert musicArtistErrorAlert = ErrorModal.initAlert();

        musicArtistErrorAlert.setContentText(musicArtistErrorAlert.getContentText() + "Vous n'avez renseigné aucun artiste pour");
        musicArtistErrorAlert.setContentText(musicArtistErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        musicArtistErrorAlert.setContentText(musicArtistErrorAlert.getContentText() + "cette musique ! ");

        Stage musicArtistErrorStage = GenericModal.initStage(musicArtistErrorAlert);
        musicArtistErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(musicArtistErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(musicArtistErrorAlert);

        musicArtistErrorAlert.showAndWait();
    }

    public static void getMusicLengthErrorAlert() {
        Alert musicLengthErrorAlert = ErrorModal.initAlert();

        musicLengthErrorAlert.setContentText(musicLengthErrorAlert.getContentText() + "Vous avez renseigné une durée invalide");
        musicLengthErrorAlert.setContentText(musicLengthErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        musicLengthErrorAlert.setContentText(musicLengthErrorAlert.getContentText() + "pour cette musique ! ");

        Stage musicLengthErrorStage = GenericModal.initStage(musicLengthErrorAlert);
        musicLengthErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(musicLengthErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(musicLengthErrorAlert);

        musicLengthErrorAlert.showAndWait();
    }

    public static void getMusicFileErrorAlert(TypeSource source) {
        Alert musicFileErrorAlert = ErrorModal.initAlert();

        if (source.equals(TypeSource.MUSIC)) {
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + "Votre musique possède un fichier");
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + "introuvable !");
        } else {
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + "Une des musiques de votre ");
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + source.getLibelle());
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
            musicFileErrorAlert.setContentText(musicFileErrorAlert.getContentText() + "possède un fichier introuvable !");
        }

        Stage musicFileErrorStage = GenericModal.initStage(musicFileErrorAlert);
        musicFileErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(musicFileErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(musicFileErrorAlert);

        musicFileErrorAlert.showAndWait();
    }
}
