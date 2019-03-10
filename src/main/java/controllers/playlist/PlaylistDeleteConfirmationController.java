package controllers.playlist;

import dao.PlaylistDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listeners.ListPlaylistSelectionListener;
import mapper.PlaylistMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;

public class PlaylistDeleteConfirmationController {

    private static final Logger LOG = LogManager.getLogger(PlaylistDeleteConfirmationController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    private InformationsUtils informationsUtils = new InformationsUtils();

    // PLAYLIST DELETING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void playlistDeleteYesClicked() {
        ListPlaylistController.getPlaylistDeleteConfirmationStage().close();
        PlaylistDao playlistDao = new PlaylistDao();
        playlistDao.delete(PlaylistMapper.toDb(ListPlaylistSelectionListener.getPlaylistSelected()));

        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone("supprimée");
            PlaylistController playlistController = new PlaylistController();
            playlistController.initialize(getClass().getResource("/views/playlist/playlistActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/playlist/playlistActionSuccess.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 420, 140));
            PlaylistController.setPlaylistActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    // PLAYLIST DELETING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void playlistDeleteNoClicked() {
        ListPlaylistController.getPlaylistDeleteConfirmationStage().close();
    }
}
