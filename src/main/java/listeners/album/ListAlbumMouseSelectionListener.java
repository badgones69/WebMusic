package listeners.album;

import dto.AlbumDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListAlbumMouseSelectionListener extends ListAlbumSelectionListener implements EventHandler<MouseEvent> {

    public ListAlbumMouseSelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED ALBUM IN ALBUM LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<AlbumDto> table = (TableView<AlbumDto>) event.getSource();
        setAlbumSelected(table.getSelectionModel().getSelectedItem());
    }

}
