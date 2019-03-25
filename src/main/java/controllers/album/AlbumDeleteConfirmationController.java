package controllers.album;

import dao.AlbumDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listeners.ListAlbumSelectionListener;
import mapper.AlbumMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;

public class AlbumDeleteConfirmationController {

    private static final Logger LOG = LogManager.getLogger(AlbumDeleteConfirmationController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    private InformationsUtils informationsUtils = new InformationsUtils();

    // ALBUM DELETING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void albumDeleteYesClicked() {
        ListAlbumController.getAlbumDeleteConfirmationStage().close();
        AlbumDao musiqueDao = new AlbumDao();
        musiqueDao.delete(AlbumMapper.toDb(ListAlbumSelectionListener.getAlbumSelected()));

        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone("supprimé");
            AlbumController albumController = new AlbumController();
            albumController.initialize(getClass().getResource("/views/album/albumActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/album/albumActionSuccess.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 420, 140));
            AlbumController.setAlbumActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
    }

    // ALBUM DELETING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void albumDeleteNoClicked() {
        ListAlbumController.getAlbumDeleteConfirmationStage().close();
    }
}
