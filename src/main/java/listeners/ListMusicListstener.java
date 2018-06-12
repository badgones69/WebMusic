package listeners;

import dto.MusiqueDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListMusicListstener implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        TableView<MusiqueDto> table = (TableView<MusiqueDto>) event.getSource();

        // récupérer la MusiqueDto sélectionnée
        MusiqueDto musiqueSelected = table.getSelectionModel().getSelectedItem();
    }
}
