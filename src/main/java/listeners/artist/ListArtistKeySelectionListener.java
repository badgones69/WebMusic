package listeners.artist;

import dto.AuteurDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ListArtistKeySelectionListener extends ListArtistSelectionListener implements EventHandler<KeyEvent> {

    public ListArtistKeySelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED ARTIST IN ARTIST LIST
    @Override
    public void handle(KeyEvent event) {
        TableView<AuteurDto> table = (TableView<AuteurDto>) event.getSource();

        if (event.getCode().equals(KeyCode.UP) ||
                event.getCode().equals(KeyCode.KP_UP)  ||
                event.getCode().equals(KeyCode.DOWN) ||
                event.getCode().equals(KeyCode.KP_DOWN)){
            setAuteurSelected(table.getSelectionModel().getSelectedItem());
        }
    }
}
