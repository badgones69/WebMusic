package listeners.listMusic;

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

    public static void setMusiqueSelected(MusiqueDto musiqueSelected) {
        ListMusicSelectionListener.musiqueSelected = musiqueSelected;
    }

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();
        musiqueSelected = table.getSelectionModel().getSelectedItem();
    }

}
