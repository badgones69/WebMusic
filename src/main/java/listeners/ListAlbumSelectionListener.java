package listeners;

import dto.AlbumDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListAlbumSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED ALBUM IN ALBUM LIST
    private static AlbumDto albumSelected;

    /**
     * GETTER AND SETTER
     */
    public static AlbumDto getAlbumSelected() {
        return albumSelected;
    }

    public static void setAlbumSelected(AlbumDto albumDtoSelected) {
        albumSelected = albumDtoSelected;
    }

    // METHOD TO CONSERVE THE SELECTED ALBUM IN ALBUM LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<AlbumDto> table = (TableView<AlbumDto>) event.getSource();
        setAlbumSelected(table.getSelectionModel().getSelectedItem());
    }

}
