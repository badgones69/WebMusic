package listeners.music;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListMusicMouseSelectionListener extends ListMusicSelectionListener implements EventHandler<MouseEvent> {

    public ListMusicMouseSelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();
        setMusiqueSelected(table.getSelectionModel().getSelectedItem());
    }

}
