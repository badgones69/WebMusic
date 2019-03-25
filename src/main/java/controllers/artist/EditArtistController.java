package controllers.artist;

import dao.AuteurDao;
import db.AuteurDb;
import dto.AuteurDto;
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
import listeners.ListArtistSelectionListener;
import mapper.AuteurMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditArtistController extends ArtistController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(EditArtistController.class);
    private static final String IO_EXCEPTION = "IOException : ";

    /**
     * ARTIST EDITING FORM FIELDS
     */

    @FXML
    protected TextField prenom = new TextField();
    @FXML
    protected TextField nom = new TextField();

    /**
     * ARTIST EDITING FORM CONTAINERS
     */

    @FXML
    BorderPane editArtistBorderPane = new BorderPane();
    @FXML
    VBox editArtistVBox = new VBox();

    /**
     * ARTIST EDITING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        editArtistBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        editArtistBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        editArtistVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editArtistVBox.setPrefHeight(editArtistBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {
        AuteurDto auteurDto = ListArtistSelectionListener.getAuteurSelected();
        AuteurDb auteurDb = AuteurMapper.toDb(auteurDto);

        // "prenom" FIELD INITIALIZATION
        this.prenom.setText(auteurDb.getPrenomAuteur());

        // "titre" FIELD INITIALIZATION
        this.nom.setText(auteurDb.getNomAuteur());
    }

    // ARTIST EDITING FORM VALIDATION AND SENDING
    public void validForm() {

        if (nom.getText() == null || "".equals(nom.getText().trim())) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/errors/artistNameError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 350, 140));
                this.setArtistNameErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        } else {
            AuteurDb auteurDb = new AuteurDb();

            auteurDb.setIdentifiantAuteur(ListArtistSelectionListener.getAuteurSelected().getIdentifiantAuteur());
            auteurDb.setPrenomAuteur("".equals(this.prenom.getText()) ? null : this.prenom.getText());
            auteurDb.setNomAuteur(this.nom.getText());

            AuteurDao auteurDao = new AuteurDao();
            auteurDao.update(auteurDb);

            Stage stage = new Stage();

            try {
                PopUpUtils.setActionDone("modifié(e)");
                ArtistController artistController = new ArtistController();
                artistController.initialize(getClass().getResource("/views/artist/artistActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/artistActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                this.setArtistActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                LOG.error(IO_EXCEPTION + e.getMessage(), e);
            }
        }
    }

    // ARTIST EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
