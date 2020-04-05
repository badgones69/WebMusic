package modal.generic.confirmation;

import dao.AlbumDao;
import dao.AuteurDao;
import dao.MusiqueDao;
import dao.PlaylistDao;
import dto.AlbumDto;
import dto.AuteurDto;
import dto.MusiqueDto;
import dto.PlaylistDto;
import enums.TypeAction;
import enums.TypeSource;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import mapper.AlbumMapper;
import mapper.AuteurMapper;
import mapper.MusiqueMapper;
import mapper.PlaylistMapper;
import modal.generic.GenericModal;
import modal.generic.success.ActionSuccessModal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

import java.util.Optional;

public class CommonConfirmationModal {

    private static final Logger LOG = LogManager.getLogger(CommonConfirmationModal.class);

    private CommonConfirmationModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getDeleteConfirmationAlert(TypeSource source, Object objectSelected) {
        Alert deleteConfirmationAlert = ConfirmationModal.initAlert();

        deleteConfirmationAlert.setContentText(deleteConfirmationAlert.getContentText() + "supprimer ");
        deleteConfirmationAlert.setContentText(deleteConfirmationAlert.getContentText() + ModalUtils.getWordThisFeminised(source));
        deleteConfirmationAlert.setContentText(deleteConfirmationAlert.getContentText() + " " + source.getLibelle() + " ?");

        Stage deleteConfirmationStage = GenericModal.initStage(deleteConfirmationAlert);
        String title = "Suppression d'";
        title += ModalUtils.feminiseWord("un", source);
        title += " " + source.getLibelle();
        deleteConfirmationStage.setTitle(new InformationsUtils().buildStageTitleBar(deleteConfirmationStage, title));
        new ConfirmationModal().initPane(deleteConfirmationAlert);

        Optional<ButtonType> result = deleteConfirmationAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            if (objectSelected instanceof MusiqueDto) {
                MusiqueDao musiqueDao = new MusiqueDao();
                musiqueDao.delete(MusiqueMapper.toDb((MusiqueDto) objectSelected));
            } else if (objectSelected instanceof AlbumDto) {
                AlbumDao albumDao = new AlbumDao();
                albumDao.delete(AlbumMapper.toDb((AlbumDto) objectSelected));
            } else if (objectSelected instanceof AuteurDto) {
                AuteurDao auteurDao = new AuteurDao();
                auteurDao.delete(AuteurMapper.toDb((AuteurDto) objectSelected));
            } else if (objectSelected instanceof PlaylistDto) {
                PlaylistDao playlistDao = new PlaylistDao();
                playlistDao.delete(PlaylistMapper.toDb((PlaylistDto) objectSelected));
            }

            ActionSuccessModal.getActionSuccessAlert(source, TypeAction.DELETE, true);
        }
    }

    public static void getAppCloseConfirmationAlert() {
        Alert appCloseConfirmationAlert = ConfirmationModal.initAlert();

        appCloseConfirmationAlert.setContentText(appCloseConfirmationAlert.getContentText() + "fermer l'application WebMusic ?");

        Stage appCloseConfirmationStage = GenericModal.initStage(appCloseConfirmationAlert);
        appCloseConfirmationStage.setTitle(new InformationsUtils().buildStageTitleBar(appCloseConfirmationStage, "Fermeture"));
        new ConfirmationModal().initPane(appCloseConfirmationAlert);

        Optional<ButtonType> result = appCloseConfirmationAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            Platform.exit();
        }
    }
}
