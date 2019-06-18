package controllers.playlist;

import db.MusiqueDb;
import dto.MusiqueDto;
import enums.TypeAction;
import enums.TypeSource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mapper.MusiqueMapper;
import modal.ActionSuccessModal;
import modal.PlaylistErrorModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    private static final Logger LOG = LogManager.getLogger(PlaylistController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // PLAYLIST'S TITLE ERROR POP-UP STAGE
    protected static Stage playlistTitleErrorStage;

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    /**
     * GETTERS AND SETTERS
     */

    public static Stage getPlaylistTitleErrorStage() {
        return playlistTitleErrorStage;
    }

    public static void setPlaylistTitleErrorStage(Stage playlistTitleErrorStage) {
        PlaylistController.playlistTitleErrorStage = playlistTitleErrorStage;
    }

    // PLAYLIST'S TITLE ERROR POP-UP "OK" BUTTON CLICKED
    public void playlistTitleErrorCloseButtonClicked() {
        getPlaylistTitleErrorStage().close();
    }

    protected List<MusiqueDb> setMusicsToPlaylist(List<MusiqueDto> musiqueDtoList) {
        List<MusiqueDb> musiquesPlaylist = new ArrayList<>();

        for (MusiqueDto musiqueDto : musiqueDtoList) {
            musiquesPlaylist.add(MusiqueMapper.toDb(musiqueDto));
        }
        return musiquesPlaylist;
    }

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.PLAYLIST, action);
    }

    protected void showMusicErrorPopUp() {
        PlaylistErrorModal.getPlaylistMusicErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/errors/playlistTitleError.fxml"));
            stage.setTitle(this.informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 330, 140));
            this.setPlaylistTitleErrorStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }
}
