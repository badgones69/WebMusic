package listeners;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListMusicSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED MUSIC IN MUSIC LIST
    private MusiqueDto musiqueSelected;

    /**
     * GETTER AND SETTER
     */
    public MusiqueDto getMusiqueSelected() {
        return this.musiqueSelected;
    }

    public void setMusiqueSelected(MusiqueDto musiqueSelected) {
        this.musiqueSelected = musiqueSelected;
    }

    // METHOD TO CONSERVE THE SELECTED MUSIC IN MUSIC LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();
        musiqueSelected = table.getSelectionModel().getSelectedItem();
    }

}
