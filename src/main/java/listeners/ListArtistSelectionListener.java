package listeners;

import dto.AuteurDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListArtistSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED ARTIST IN ARTIST LIST
    private AuteurDto auteurSelected;

    /**
     * GETTER AND SETTER
     */
    public AuteurDto getAuteurSelected() {
        return this.auteurSelected;
    }

    public void setAuteurSelected(AuteurDto auteurSelected) {
        this.auteurSelected = auteurSelected;
    }

    // METHOD TO CONSERVE THE SELECTED ARTIST IN ARTIST LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<AuteurDto> table = (TableView<AuteurDto>) event.getSource();
        auteurSelected = table.getSelectionModel().getSelectedItem();
    }

}
