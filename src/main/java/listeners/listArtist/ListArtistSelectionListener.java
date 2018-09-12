package listeners.listArtist;

import dto.AuteurDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListArtistSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED ARTIST IN ARTIST LIST
    private static AuteurDto auteurSelected;

    /**
     * GETTER AND SETTER
     */
    public static AuteurDto getAuteurSelected() {
        return auteurSelected;
    }

    public static void setAuteurSelected(AuteurDto auteurSelected) {
        ListArtistSelectionListener.auteurSelected = auteurSelected;
    }

    // METHOD TO CONSERVE THE SELECTED ARTIST IN ARTIST LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<AuteurDto> table = (TableView<AuteurDto>) event.getSource();
        auteurSelected = table.getSelectionModel().getSelectedItem();
    }

}
