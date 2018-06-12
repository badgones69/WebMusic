package controllers;

import dao.MusiqueDao;
import db.MusiqueDb;
import dto.MusiqueDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import listeners.ListMusicListstener;
import mapper.MusiqueMapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListMusicController implements Initializable {

    /**
     * MUSIC LIST COLUMNS
     */

    @FXML
    private TableView<MusiqueDto> listMusic = new TableView<>();

    @FXML
    private TableColumn<MusiqueDto, String> titreColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> artisteColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> dureeColumn = new TableColumn<>();

    @FXML
    private TableColumn<MusiqueDto, String> albumColumn = new TableColumn<>();

    /**
     * MUSIC LIST INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeList();
    }

    private void initializeList() {
        MusiqueDao musiqueDao = new MusiqueDao();
        ObservableList<MusiqueDto> listMusiques = FXCollections.observableArrayList();
        List<MusiqueDb> musiquesDb = musiqueDao.findAll();
        List<MusiqueDto> musiquesDto = new ArrayList<>();

        for (MusiqueDb musiqueDb : musiquesDb) {
            musiquesDto.add(MusiqueMapper.toDto(musiqueDb));
        }

        for (MusiqueDto musiqueDto : musiquesDto) {
            listMusiques.add(musiqueDto);
        }

        this.listMusic.getItems().clear();
        this.listMusic.setItems(listMusiques);

        for (int i = 0; i < this.listMusic.getItems().size(); i++) {

            MusiqueDto musiqueDto = this.listMusic.getItems().get(i);



            titreColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("titreMusique"));
            artisteColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("auteurs"));
            dureeColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("dureeMusique"));
            albumColumn.setCellValueFactory(new PropertyValueFactory<MusiqueDto, String>("titreAlbumMusique"));
            listMusic.getColumns().clear();
            listMusic.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listMusic.setOnMouseClicked(new ListMusicListstener());
            listMusic.getColumns().addAll(titreColumn, artisteColumn, dureeColumn, albumColumn);
            listMusic.getSortOrder().add(titreColumn);
        }
    }
}
