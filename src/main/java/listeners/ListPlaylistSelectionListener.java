package listeners;

import dto.PlaylistDto;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class ListPlaylistSelectionListener implements EventHandler<MouseEvent> {

    // SELECTED PLAYLIST IN PLAYLIST LIST
    private static PlaylistDto playlistSelected;

    /**
     * GETTER AND SETTER
     */
    public static PlaylistDto getPlaylistSelected() {
        return playlistSelected;
    }

    public static void setPlaylistSelected(PlaylistDto musicSelected) {
        playlistSelected = musicSelected;
    }

    // METHOD TO CONSERVE THE SELECTED PLAYLIST IN PLAYLIST LIST
    @Override
    public void handle(MouseEvent event) {
        TableView<PlaylistDto> table = (TableView<PlaylistDto>) event.getSource();
        setPlaylistSelected(table.getSelectionModel().getSelectedItem());
    }

}
