package controllers.album;

import dao.AlbumDao;
import db.AlbumDb;
import enums.TypeAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import utils.DaoTestsUtils;
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
     * ALBUM ADDING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeForm();
    }

    private void initializeForm() {
        // "titre" FIELD INITIALIZATION
        this.titre.clear();

        // "annee" FIELD INITIALIZATION
        this.annee.clear();
    }

    // ALBUM ADDING FORM VALIDATION AND SENDING
    public void validForm() {
        this.validatingForm(this.titre, this.annee, true);
    }

    public AlbumDb validatingForm(TextField titre, TextField annee, boolean needRedirection) {
        boolean titreInvalide = "".equals(titre.getText());
        boolean anneeInvalide = !FormUtils.anneeAlbumIsValid(annee.getText());

        if (titreInvalide) {
            super.showTitleErrorPopUp();
        }

        if (anneeInvalide) {
            super.showYearErrorPopUp();
        }

        if (Boolean.FALSE.equals(titreInvalide) && Boolean.FALSE.equals(anneeInvalide)) {
            AlbumDb albumDb = new AlbumDb();

            albumDb.setTitreAlbum(titre.getText());
            albumDb.setAnneeAlbum("".equals(annee.getText()) ? null : Integer.parseInt(annee.getText()));

            AlbumDao albumDao = new AlbumDao();
            albumDao.insert(albumDb);

            super.showSuccessPopUp(TypeAction.ADD, needRedirection);
            DaoTestsUtils.setNumeroToAlbum(albumDb);
            return albumDb;
        }
        return new AlbumDb();
    }

    // ALBUM ADDING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}