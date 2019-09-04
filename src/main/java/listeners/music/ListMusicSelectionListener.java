package listeners.music;

import dto.MusiqueDto;

public class ListMusicSelectionListener {

    protected ListMusicSelectionListener() {}

    // SELECTED MUSIC IN MUSIC LIST
    private static MusiqueDto musiqueSelected;

    /**
     * GETTER AND SETTER
     */
    public static MusiqueDto getMusiqueSelected() {
        return musiqueSelected;
    }

    public static void setMusiqueSelected(MusiqueDto musiqueDtoSelected) {
        musiqueSelected = musiqueDtoSelected;
    }
}
