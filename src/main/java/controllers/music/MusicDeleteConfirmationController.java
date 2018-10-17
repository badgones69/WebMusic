package controllers.music;

import dao.MusiqueDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listeners.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;

public class MusicDeleteConfirmationController {

    private static final Logger LOG = LogManager.getLogger(MusicDeleteConfirmationController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    private InformationsUtils informationsUtils = new InformationsUtils();

    // MUSIC DELETING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void musicDeleteYesClicked() {
        ListMusicController.getMusicDeleteConfirmationStage().close();
        MusiqueDao musiqueDao = new MusiqueDao();
        musiqueDao.delete(MusiqueMapper.toDb(new ListMusicSelectionListener().getMusiqueSelected()));

        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone("supprimée");
            MusicController musicController = new MusicController();
            musicController.initialize(getClass().getResource("/views/music/musicActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/music/musicActionSuccess.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 420, 140));
            MusicController.setMusicActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    // MUSIC DELETING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void musicDeleteNoClicked() {
        ListMusicController.getMusicDeleteConfirmationStage().close();
    }
}
