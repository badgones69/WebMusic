package controllers;

import dao.MusiqueDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listeners.listMusic.ListMusicSelectionListener;
import mapper.MusiqueMapper;
import utils.InformationsUtils;
import utils.WindowUtils;

import java.io.IOException;

public class MusicDeleteConfirmationController {

    private InformationsUtils informationsUtils = new InformationsUtils();

    // MUSIC DELETING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void musicDeleteYesClicked(ActionEvent actionEvent) {
        ListMusicController.getMusicDeleteConfirmationStage().close();
        MusiqueDao musiqueDao = new MusiqueDao();
        musiqueDao.delete(MusiqueMapper.toDb(ListMusicSelectionListener.getMusiqueSelected()));

        Stage stage = new Stage();

        try {
            WindowUtils.setActionDone("supprimée");
            MusicController musicController = new MusicController();
            musicController.initialize(getClass().getResource("/views/musicActionSuccess.fxml"), null);
            Parent root = FXMLLoader.load(getClass().getResource("/views/musicActionSuccess.fxml"));
            stage.setTitle(informationsUtils.buildStageTitle(null));
            stage.setScene(new Scene(root, 420, 140));
            MusicController.setMusicActionSuccessStage(stage);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MUSIC DELETING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void musicDeleteNoClicked(ActionEvent actionEvent) {
        ListMusicController.getMusicDeleteConfirmationStage().close();
    }
}
