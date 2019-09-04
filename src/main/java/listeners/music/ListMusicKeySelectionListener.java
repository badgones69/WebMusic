package listeners.music;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ListMusicKeySelectionListener extends ListMusicSelectionListener implements EventHandler<KeyEvent> {

    public ListMusicKeySelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(KeyEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();

        if (event.getCode().equals(KeyCode.UP) ||
                event.getCode().equals(KeyCode.KP_UP)  ||
                event.getCode().equals(KeyCode.DOWN) ||
                event.getCode().equals(KeyCode.KP_DOWN)){
            setMusiqueSelected(table.getSelectionModel().getSelectedItem());
        }
    }
}
