package modal.editable;

import controllers.album.AddAlbumController;
import dao.AlbumDao;
import db.AlbumDb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FormUtils;
import utils.InformationsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AlbumModal extends AddAlbumController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(AlbumModal.class);
    private static final String IO_EXCEPTION = "IOException : ";

    private static AlbumDb currentAlbumPreSelected;

    /**
     * ALBUM ADDING FORM FIELDS
     */

    @FXML
    protected TextField titreModal = new TextField();
    @FXML
    protected TextField anneeModal = new TextField();

    /**
     * ALBUM SELECTING FORM FIELDS
     */

    @FXML
    private ComboBox<AlbumDb> albumSelect = new ComboBox<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeModalForm();
    }

    public void initializeModalForm() {
        /*
         * ALBUM ADDING FORM INITIALIZATION
         */

        // "titreModal" FIELD INITIALIZATION
        this.titreModal.clear();

        // "anneeModal" FIELD INITIALIZATION
        this.anneeModal.clear();

        /*
         * ALBUM SELECTING FORM INITIALIZATION
         */

        AlbumDao albumDao = new AlbumDao();
        List<AlbumDb> listAlbums = new ArrayList<>(albumDao.findAll());

        Collections.sort(listAlbums, (o1, o2) -> FormUtils.getFrenchCollator().compare(o1.getTitreAlbum(), o2.getTitreAlbum()));

        this.albumSelect.getItems().clear();
        this.albumSelect.getItems().add(new AlbumDb());
        this.albumSelect.getItems().addAll(listAlbums);
        this.albumSelect.setValue(currentAlbumPreSelected);
    }

    public AlbumDb getSelectAlbumAlert() {
        Alert selectAlbumAlert = new Alert(Alert.AlertType.CONFIRMATION);
        selectAlbumAlert.setHeaderText(null);
        DialogPane dialogPane = new DialogPane();

        try {
            dialogPane.setContent(FXMLLoader.load(getClass().getResource("/views/music/selectExistingAlbum.fxml")));
        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
        selectAlbumAlert.setDialogPane(dialogPane);

        Stage selectAlbumStage = EditableModal.initStage(selectAlbumAlert);
        selectAlbumStage.setTitle(new InformationsUtils().buildStageTitleBar(selectAlbumStage, "Sélection d'un album"));
        EditableModal.initPane(selectAlbumAlert);

        Button selectButton = (Button) selectAlbumAlert.getDialogPane().lookupButton(ButtonType.YES);
        selectButton.setText("Sélectionner");

        Button cancelButton = (Button) selectAlbumAlert.getDialogPane().lookupButton(ButtonType.NO);
        cancelButton.setText("Annuler");

        Optional<ButtonType> result = selectAlbumAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            BorderPane borderPane = (BorderPane) selectAlbumAlert.getDialogPane().getContent();
            VBox vBox = (VBox) borderPane.getCenter();
            Optional<Node> optionalGridPane = vBox.getChildren().stream().filter(c -> c instanceof GridPane).findFirst();

            if(optionalGridPane.isPresent()) {
                GridPane gridPane = (GridPane) optionalGridPane.get();
                Optional<Node> optionalComboBox = gridPane.getChildren().stream().filter(c -> c instanceof ComboBox).findFirst();

                if(optionalComboBox.isPresent() && optionalComboBox.get() instanceof ComboBox) {
                    ComboBox<AlbumDb> comboBox = (ComboBox<AlbumDb>) optionalComboBox.get();
                    return comboBox.getSelectionModel().getSelectedItem();
                }
            }
        } else if(currentAlbumPreSelected.getNumeroAlbum() != null) {
            return currentAlbumPreSelected;
        }
        return new AlbumDb();
    }

    public AlbumDb getAddNewAlbumAlert() {
        Alert addNewAlbumAlert = new Alert(Alert.AlertType.CONFIRMATION);
        addNewAlbumAlert.setHeaderText(null);
        DialogPane dialogPane = new DialogPane();

        try {
            dialogPane.setContent(FXMLLoader.load(getClass().getResource("/views/music/addNewAlbum.fxml")));
        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
        addNewAlbumAlert.setDialogPane(dialogPane);

        Stage selectAlbumStage = EditableModal.initStage(addNewAlbumAlert);
        selectAlbumStage.setTitle(new InformationsUtils().buildStageTitleBar(selectAlbumStage, "Ajout d'un album"));
        EditableModal.initPane(addNewAlbumAlert);

        Button validateButton = (Button) addNewAlbumAlert.getDialogPane().lookupButton(ButtonType.YES);
        validateButton.setText("Valider");

        Button cancelButton = (Button) addNewAlbumAlert.getDialogPane().lookupButton(ButtonType.NO);
        cancelButton.setText("Annuler");

        Optional<ButtonType> result = addNewAlbumAlert.showAndWait();

        if (result.isPresent() && ButtonType.YES.equals(result.get())) {
            BorderPane borderPane = (BorderPane) addNewAlbumAlert.getDialogPane().getContent();
            VBox vBox = (VBox) borderPane.getCenter();
            Optional<Node> optionalGridPane = vBox.getChildren().stream().filter(c -> c instanceof GridPane).findFirst();

            if(optionalGridPane.isPresent()) {
                GridPane gridPane = (GridPane) optionalGridPane.get();
                List<Node> textFields = gridPane.getChildren().stream().filter(c -> c instanceof TextField).collect(Collectors.toList());

                if(textFields.size() == 2) {
                    this.titreModal.setText(((TextField) textFields.get(0)).getText());
                    this.anneeModal.setText(((TextField) textFields.get(1)).getText());
                }
            }
            return super.validatingForm(this.titreModal, this.anneeModal, false);
        } else if(currentAlbumPreSelected.getNumeroAlbum() != null) {
            return currentAlbumPreSelected;
        }
        return new AlbumDb();
    }

    public static void setCurrentAlbumPreSelected(AlbumDb currentAlbumPreSelected) {
        AlbumModal.currentAlbumPreSelected = currentAlbumPreSelected;
    }
}
