package modal.generic.error;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.generic.GenericModal;
import utils.LogUtils;
import utils.InformationsUtils;
import utils.ModalUtils;

public class PlaylistErrorModal {

    private PlaylistErrorModal() {
        LogUtils.generateConstructorLog(PlaylistErrorModal.class);
    }

    public static void getPlaylistMusicErrorAlert() {
        Alert playlistMusicErrorAlert = ErrorModal.initAlert();

        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + "Vous n'avez renseigné aucune musique");
        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + "pour cette playlist ! ");

        Stage playlistMusicErrorStage = GenericModal.initStage(playlistMusicErrorAlert);
        playlistMusicErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(playlistMusicErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(playlistMusicErrorAlert);

        playlistMusicErrorAlert.showAndWait();
    }
}
