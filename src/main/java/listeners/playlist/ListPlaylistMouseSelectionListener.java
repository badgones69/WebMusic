package listeners.playlist;

import dto.PlaylistDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListPlaylistMouseSelectionListener extends ListPlaylistSelectionListener implements EventHandler<MouseEvent> {

    public ListPlaylistMouseSelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED PLAYLIST IN PLAYLIST LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<PlaylistDto> table = (TableView<PlaylistDto>) event.getSource();
        setPlaylistSelected(table.getSelectionModel().getSelectedItem());
    }

}
