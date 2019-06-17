package modal;

import controllers.album.ListAlbumController;
import controllers.artist.ListArtistController;
import controllers.common.Home;
import controllers.music.ListMusicController;
import controllers.playlist.ListPlaylistController;
import enums.TypeAction;
import enums.TypeSource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.InformationsUtils;
import utils.ModalUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ActionSuccessModal {

    private static final Logger LOG = LogManager.getLogger(ActionSuccessModal.class);
    private static final String IO_EXCEPTION = "IOException : ";
    private static InformationsUtils informationsUtils = new InformationsUtils();

    private ActionSuccessModal() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static void getSuccessAlert(TypeSource source, TypeAction action) {
        Alert actionSuccessAlert = SuccessModal.initAlert();

        actionSuccessAlert.setContentText("Votre ");
        actionSuccessAlert.setContentText(actionSuccessAlert.getContentText() + source.getLibelle());
        actionSuccessAlert.setContentText(actionSuccessAlert.getContentText() + " a bien été ");
        actionSuccessAlert.setContentText(actionSuccessAlert.getContentText() + ModalUtils.feminiseWord(action.getLibelle(), source));
        actionSuccessAlert.setContentText(actionSuccessAlert.getContentText() + ".");

        Stage actionSuccessStage = Modal.initStage(actionSuccessAlert);
        actionSuccessStage.setTitle(informationsUtils.buildStageTitleBar(actionSuccessStage, SuccessModal.SUCCESS_MODAL_TITLE));
        new SuccessModal().initPane(actionSuccessAlert);

        Optional<ButtonType> result = actionSuccessAlert.showAndWait();

        if (result.isPresent() && ButtonType.OK.equals(result.get())) {
            String stageTitle = "Liste des " + source.getLibelle() + "s";
            Stage homeStage = new Home().getHomeStage();
            Parent root = null;

            try {

                switch (source) {
                    case PLAYLIST:
                        ListPlaylistController listPlaylistController = new ListPlaylistController();
                        listPlaylistController.initialize(ModalUtils.class.getResource("/views/playlist/listPlaylist.fxml"), null);
                        root = FXMLLoader.load(ModalUtils.class.getResource("/views/playlist/listPlaylist.fxml"));
                        break;
                    case MUSIC:
                        ListMusicController listMusicController = new ListMusicController();
                        listMusicController.initialize(ModalUtils.class.getResource("/views/music/listMusic.fxml"), null);
                        root = FXMLLoader.load(ModalUtils.class.getResource("/views/music/listMusic.fxml"));
                        break;
                    case ALBUM:
                        ListAlbumController listAlbumController = new ListAlbumController();
                        listAlbumController.initialize(ModalUtils.class.getResource("/views/album/listAlbum.fxml"), null);
                        root = FXMLLoader.load(ModalUtils.class.getResource("/views/album/listAlbum.fxml"));
                        break;
                    case ARTIST:
                        ListArtistController listArtistController = new ListArtistController();
                        listArtistController.initialize(ModalUtils.class.getResource("/views/artist/listArtist.fxml"), null);
                        root = FXMLLoader.load(ModalUtils.class.getResource("/views/artist/listArtist.fxml"));
                        break;
                    default:
                        break;
                }

                homeStage.setTitle(informationsUtils.buildStageTitleBar(homeStage, stageTitle));
                homeStage.setScene(new Scene(Objects.requireNonNull(root), homeStage.getScene().getWidth(), homeStage.getScene().getHeight()));
                homeStage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);

            }
        }
    }
}
