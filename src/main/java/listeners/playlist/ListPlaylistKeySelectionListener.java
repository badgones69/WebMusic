package listeners.playlist;

import dto.PlaylistDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ListPlaylistKeySelectionListener extends ListPlaylistSelectionListener implements EventHandler<KeyEvent> {

    public ListPlaylistKeySelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED PLAYLIST IN PLAYLIST LIST
    @Override
    public void handle(KeyEvent event) {
        TableView<PlaylistDto> table = (TableView<PlaylistDto>) event.getSource();

        if (event.getCode().equals(KeyCode.UP) ||
                event.getCode().equals(KeyCode.KP_UP)  ||
                event.getCode().equals(KeyCode.DOWN) ||
                event.getCode().equals(KeyCode.KP_DOWN)){
            setPlaylistSelected(table.getSelectionModel().getSelectedItem());
        }
    }
}
