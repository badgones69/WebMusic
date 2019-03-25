package listeners;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListMusicSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED MUSIC IN MUSIC LIST
    private static MusiqueDto musiqueSelected;

    /**
     * GETTER AND SETTER
     */
    public static MusiqueDto getMusiqueSelected() {
        return musiqueSelected;
    }

    public static void setMusiqueSelected(MusiqueDto musicSelected) {
        musiqueSelected = musicSelected;
    }

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();
        setMusiqueSelected(table.getSelectionModel().getSelectedItem());
    }

}
