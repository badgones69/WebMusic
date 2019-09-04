package controllers.artist;

import dao.AuteurDao;
import db.AuteurDb;
import dto.AuteurDto;
import enums.TypeAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import listeners.artist.ListArtistSelectionListener;
import mapper.AuteurMapper;

import java.net.URL;
import java.util.ResourceBundle;

public class EditArtistController extends ArtistController implements Initializable {

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
            super.showNameErrorPopUp();
        } else {
            AuteurDb auteurDb = new AuteurDb();

            auteurDb.setIdentifiantAuteur(ListArtistSelectionListener.getAuteurSelected().getIdentifiantAuteur());
            auteurDb.setPrenomAuteur("".equals(this.prenom.getText()) ? null : this.prenom.getText());
            auteurDb.setNomAuteur(this.nom.getText());

            AuteurDao auteurDao = new AuteurDao();
            auteurDao.update(auteurDb);

            super.showSuccessPopUp(TypeAction.EDIT);
        }
    }

    // ARTIST EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
