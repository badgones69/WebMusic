package listeners.artist;

import dto.AuteurDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListArtistMouseSelectionListener extends ListArtistSelectionListener implements EventHandler<MouseEvent> {

    public ListArtistMouseSelectionListener() {
        super();
    }

    // METHOD TO CONSERVE THE SELECTED ARTIST IN ARTIST LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<AuteurDto> table = (TableView<AuteurDto>) event.getSource();
        setAuteurSelected(table.getSelectionModel().getSelectedItem());
    }

}
