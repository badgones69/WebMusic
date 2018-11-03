package controllers.artist;

import dao.AuteurDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listeners.ListArtistSelectionListener;
import mapper.AuteurMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.PopUpUtils;

import java.io.IOException;

public class ArtistDeleteConfirmationController {

    private static final Logger LOG = LogManager.getLogger(ArtistDeleteConfirmationController.class);

    private InformationsUtils informationsUtils = new InformationsUtils();

    // ARTIST DELETING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void artistDeleteYesClicked() {
        ListArtistController.getArtistDeleteConfirmationStage().close();
        AuteurDao auteurDao = new AuteurDao();
        auteurDao.delete(AuteurMapper.toDb(ListArtistSelectionListener.getAuteurSelected()));

        Stage stage = new Stage();

        try {
            PopUpUtils.setActionDone("supprimé(e)");
            ArtistController artistController = new ArtistController();
            artistController.initialize(getClass().getResource("/views/artist/artistActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/artist/artistActionSuccess.fxml"));
            stage.setTitle(informationsUtils.buildStageTitleBar(stage, null));
            stage.setScene(new Scene(root, 420, 140));
            ArtistController.setArtistActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            LOG.error("IOException : " + e.getMessage(), e);
        }
    }

    // ARTIST DELETING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void artistDeleteNoClicked() {
        ListArtistController.getArtistDeleteConfirmationStage().close();
    }
}
