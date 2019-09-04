package listeners.playlist;

import dto.PlaylistDto;

public class ListPlaylistSelectionListener {

    protected ListPlaylistSelectionListener() {}

    // SELECTED PLAYLIST IN PLAYLIST LIST
    private static PlaylistDto playlistSelected;

    /**
     * GETTER AND SETTER
     */
    public static PlaylistDto getPlaylistSelected() {
        return playlistSelected;
    }

    public static void setPlaylistSelected(PlaylistDto playlistDtoSelected) {
        playlistSelected = playlistDtoSelected;
    }
}
