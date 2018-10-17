package controllers.album;

import dao.AlbumDao;
import db.AlbumDb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FormUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAlbumController extends AlbumController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(AddAlbumController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    /**
     * ARTIST ADDING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField annee = new TextField();

    /**
     * ARTIST ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addAlbumBorderPane = new BorderPane();
    @FXML
    VBox addAlbumVBox = new VBox();

    /**
     * ARTIST ADDING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        addAlbumBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        addAlbumBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        addAlbumVBox.setPrefWidth(primaryScreenBounds.getWidth());
        addAlbumVBox.setPrefHeight(addAlbumBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {
        // "titre" FIELD INITIALIZATION
        this.titre.clear();

        // "annee" FIELD INITIALIZATION
        this.annee.clear();
    }

    // ARTIST ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        Boolean nomInvalide = "".equals(titre.getText());
        Boolean anneeInvalide = !FormUtils.anneeAlbumIsValid(annee.getText());

        if (nomInvalide) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/errors/albumNameError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 350, 140));
                this.setAlbumTitleErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }

        if (anneeInvalide) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/errors/albumYearError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 330, 140));
                this.setAlbumYearErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }

        if (Boolean.FALSE.equals(nomInvalide) && Boolean.FALSE.equals(anneeInvalide)) {
            AlbumDb albumDb = new AlbumDb();

            albumDb.setTitreAlbum(this.titre.getText());
            albumDb.setAnneeAlbum("".equals(this.annee.getText()) ? null : Integer.parseInt(this.annee.getText()));

            AlbumDao albumDao = new AlbumDao();
            albumDao.insert(albumDb);

            Stage stage = new Stage();

            try {
                PopUpUtils.setActionDone("ajouté");
                AlbumController albumController = new AlbumController();
                albumController.initialize(getClass().getResource("/views/album/albumActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/album/albumActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                this.setAlbumActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ARTIST ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}