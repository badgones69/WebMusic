package listeners.album;

import dto.AlbumDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ListAlbumKeySelectionListener extends ListAlbumSelectionListener implements EventHandler<KeyEvent> {

    public ListAlbumKeySelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED ALBUM IN ALBUM LIST
    @Override
    public void handle(KeyEvent event) {
        TableView<AlbumDto> table = (TableView<AlbumDto>) event.getSource();

        if (event.getCode().equals(KeyCode.UP) ||
                event.getCode().equals(KeyCode.KP_UP)  ||
                event.getCode().equals(KeyCode.DOWN) ||
                event.getCode().equals(KeyCode.KP_DOWN)){
            setAlbumSelected(table.getSelectionModel().getSelectedItem());
        }
    }
}
