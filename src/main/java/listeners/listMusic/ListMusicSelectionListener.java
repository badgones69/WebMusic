package listeners.listMusic;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListMusicSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED MUSIC IN MUSIC LIST
    private static MusiqueDto musiqueSelected;

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();
        musiqueSelected = table.getSelectionModel().getSelectedItem();
    }

    // GETTER
    public static MusiqueDto getMusiqueSelected() {
        return musiqueSelected;
    }

}
