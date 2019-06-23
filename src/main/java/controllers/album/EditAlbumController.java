package controllers.album;

import dao.AlbumDao;
import db.AlbumDb;
import dto.AlbumDto;
import enums.TypeAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import listeners.ListAlbumSelectionListener;
import mapper.AlbumMapper;
import utils.FormUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAlbumController extends AlbumController implements Initializable {

    /**
     * ALBUM EDITING FORM FIELDS
     */

    @FXML
    protected TextField titre = new TextField();
    @FXML
    protected TextField annee = new TextField();

    /**
     * ALBUM EDITING FORM CONTAINERS
     */

    @FXML
    BorderPane editAlbumBorderPane = new BorderPane();
    @FXML
    VBox editAlbumVBox = new VBox();

    /**
     * ALBUM EDITING FORM INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeSizes();
        this.initializeForm();
    }

    private void initializeSizes() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        editAlbumBorderPane.setPrefWidth(primaryScreenBounds.getWidth());
        editAlbumBorderPane.setPrefHeight(primaryScreenBounds.getHeight() - 32);
        editAlbumVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editAlbumVBox.setPrefHeight(editAlbumBorderPane.getPrefHeight() - 32);
    }

    private void initializeForm() {
        AlbumDto albumDto = ListAlbumSelectionListener.getAlbumSelected();
        AlbumDb albumDb = AlbumMapper.toDb(albumDto);

        // "titre" FIELD INITIALIZATION
        this.titre.setText(albumDb.getTitreAlbum());

        // "annee" FIELD INITIALIZATION
        this.annee.setText(albumDb.getAnneeAlbum() == null ? "" : String.valueOf(albumDb.getAnneeAlbum()));
    }

    // ALBUM EDITING FORM VALIDATION AND SENDING
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
            AlbumDb album = new AlbumDb();

            album.setNumeroAlbum(ListAlbumSelectionListener.getAlbumSelected().getNumeroAlbum());
            album.setTitreAlbum(this.titre.getText());
            album.setAnneeAlbum("".equals(this.annee.getText()) ? null : Integer.parseInt(this.annee.getText()));

            AlbumDao albumDao = new AlbumDao();
            albumDao.update(album);

            super.showSuccessPopUp(TypeAction.EDIT);
        }
    }

    // ALBUM EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}