package listeners.album;

import dto.AlbumDto;

public class ListAlbumSelectionListener {

    protected ListAlbumSelectionListener() {}

    // SELECTED ALBUM IN ALBUM LIST
    private static AlbumDto albumSelected;

    /**
     * GETTER AND SETTER
     */
    public static AlbumDto getAlbumSelected() {
        return albumSelected;
    }

    public static void setAlbumSelected(AlbumDto albumDtoSelected) {
        albumSelected = albumDtoSelected;
    }
}