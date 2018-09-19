package controllers;

import dao.AlbumDao;
import dao.AuteurDao;
import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import dto.AuteurDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import listeners.listArtist.ListArtistSelectionListener;
import mapper.AuteurMapper;
import org.controlsfx.control.ListSelectionView;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;
import utils.FormUtils;
import utils.PopUpUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
     * ARTIST ADDING FORM CONTAINERS
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
        editArtistBorderPane.setPrefHeight(primaryScreenBounds.getHeight()-32);
        editArtistVBox.setPrefWidth(primaryScreenBounds.getWidth());
        editArtistVBox.setPrefHeight(editArtistBorderPane.getPrefHeight()-32);
    }

    private void initializeForm() {
        AuteurDto auteurDto = ListArtistSelectionListener.getAuteurSelected();
        AuteurDb auteurDb = AuteurMapper.toDb(auteurDto);

        // "prenom" FIELD INITIALIZATION
        this.prenom.setText(auteurDb.getPrenomAuteur());

        // "nom" FIELD INITIALIZATION
        this.nom.setText(auteurDb.getNomAuteur());
    }

    // ARTIST EDITING FORM VALIDATION AND SENDING
    public void validForm() {

        if (nom.getText() == null || "".equals(nom.getText().trim())) {
            Stage stage = new Stage();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/artistError.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 350, 140));
                this.setNameErrorStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
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
                artistController.initialize(getClass().getResource("/views/artistActionSuccess.fxml"), null);
                Parent root = FXMLLoader.load(getClass().getResource("/views/artistActionSuccess.fxml"));
                stage.setTitle(super.informationsUtils.buildStageTitleBar(stage, null));
                stage.setScene(new Scene(root, 390, 140));
                this.setArtistActionSuccessStage(stage);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ARTIST EDITING FORM CANCELING
    public void cancelForm() {
        this.initializeForm();
    }
}
