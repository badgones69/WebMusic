package controllers.album;

import dao.AlbumDao;
import db.AlbumDb;
import enums.TypeAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import utils.FormUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAlbumController extends AlbumController implements Initializable {

    /**
     * ALBUM ADDING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField annee = new TextField();

    /**
     * ALBUM ADDING FORM CONTAINERS
     */

    @FXML
    BorderPane addAlbumBorderPane = new BorderPane();
    @FXML
    VBox addAlbumVBox = new VBox();

    /**
     * ALBUM ADDING FORM INITIALIZATION
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

    // ALBUM ADDING FORM VALIDATION AND SENDING
    public void validForm() {

        Boolean titreInvalide = "".equals(titre.getText());
        Boolean anneeInvalide = !FormUtils.anneeAlbumIsValid(annee.getText());

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (anneeInvalide) {
            super.showYearErrorPopUp();
        }

        if (Boolean.FALSE.equals(titreInvalide) && Boolean.FALSE.equals(anneeInvalide)) {
            AlbumDb albumDb = new AlbumDb();

            albumDb.setTitreAlbum(this.titre.getText());
            albumDb.setAnneeAlbum("".equals(this.annee.getText()) ? null : Integer.parseInt(this.annee.getText()));

            AlbumDao albumDao = new AlbumDao();
            albumDao.insert(albumDb);

            super.showSuccessPopUp(TypeAction.ADD);
        }
    }

    // ALBUM ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}