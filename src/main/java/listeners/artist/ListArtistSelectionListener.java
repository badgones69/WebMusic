package listeners.artist;

import dto.AuteurDto;

public class ListArtistSelectionListener {

    protected ListArtistSelectionListener() {}

    // SELECTED ARTIST IN ARTIST LIST
    private static AuteurDto auteurSelected;

    /**
     * GETTER AND SETTER
     */
    public static AuteurDto getAuteurSelected() {
        return auteurSelected;
    }

    public static void setAuteurSelected(AuteurDto auteurDtoSelected) {
        auteurSelected = auteurDtoSelected;
    }
}
