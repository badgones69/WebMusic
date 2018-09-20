package controllers.artist;

import dao.AuteurDao;
import db.AuteurDb;
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
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddArtistController extends ArtistController implements Initializable {

    /**
     * ARTIST ADDING FORM FIELDS
     */

    @FXML
    protected TextField prenom = new TextField();
    @FXML
    protected TextField nom = new TextField();

    /**
     * ARTIST ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addArtistBorderPane = new BorderPane();
    @FXML
    VBox addArtistVBox = new VBox();

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
        addArtistBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        addArtistBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        addArtistVBox.setPrefWidth(primaryScreenBounds.getWidth());
        addArtistVBox.setPrefHeight(addArtistBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {

        // "prenom" FIELD INITIALIZATION
        this.prenom.clear();

        // "nom" FIELD INITIALIZATION
        this.nom.clear();
    }

    // ARTIST ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        if (nom.getText() == null || "".equals(nom.getText().trim())) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/errors/artistNameError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 350, 140));
                this.setNameErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AuteurDb auteurDb = new AuteurDb();

            auteurDb.setPrenomAuteur("".equals(this.prenom.getText()) ? null : this.prenom.getText());
            auteurDb.setNomAuteur(this.nom.getText());

            AuteurDao auteurDao = new AuteurDao();
            auteurDao.insert(auteurDb);

            Stage stage = new Stage();

            try {
                PopUpUtils.setActionDone("ajouté(e)");
                ArtistController artistController = new ArtistController();
                artistController.initialize(getClass().getResource("/views/artist/artistActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/artist/artistActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                this.setArtistActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ARTIST ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}