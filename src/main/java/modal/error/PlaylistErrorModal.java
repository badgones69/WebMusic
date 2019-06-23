package modal.error;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modal.Modal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

public class PlaylistErrorModal {

    private static final Logger LOG = LogManager.getLogger(PlaylistErrorModal.class);

    private PlaylistErrorModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getPlaylistMusicErrorAlert() {
        Alert playlistMusicErrorAlert = ErrorModal.initAlert();

        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + "Vous n'avez renseigné aucune musique");
        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + ModalUtils.getSystemLineSeparator());
        playlistMusicErrorAlert.setContentText(playlistMusicErrorAlert.getContentText() + "pour cette playlist ! ");

        Stage playlistMusicErrorStage = Modal.initStage(playlistMusicErrorAlert);
        playlistMusicErrorStage.setTitle(new InformationsUtils().buildStageTitleBar(playlistMusicErrorStage, ErrorModal.ERROR_MODAL_TITLE));
        new ErrorModal().initPane(playlistMusicErrorAlert);

        playlistMusicErrorAlert.showAndWait();
    }
}
