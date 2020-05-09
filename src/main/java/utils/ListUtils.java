package utils;

import dto.AlbumDto;
import dto.AuteurDto;
import dto.MusiqueDto;
import dto.PlaylistDto;
import javafx.scene.control.TableView;
import listeners.artist.ListArtistSelectionListener;
import listeners.music.ListMusicSelectionListener;
import listeners.playlist.ListPlaylistSelectionListener;
import listeners.album.ListAlbumSelectionListener;

public class ListUtils {

    private ListUtils() {
        LogUtils.generateConstructorLog(ListUtils.class);
    }

    public static Integer getCountPage(Integer nbElements) {
        return nbElements % 10 > 0 ? (nbElements / 10) + 1 : nbElements / 10;
    }

    /**
     * METHODS TO SELECT BY DEFAULT THE FIRST ELEMENT OF LIST PAGE
     */

    public static void selectFirstMusicOfPage(TableView<MusiqueDto> list) {
        list.getSortOrder().add(list.getColumns().get(0));
        ListMusicSelectionListener.setMusiqueSelected(list.getItems().get(0));
        list.getSelectionModel().selectFirst();
    }

    public static void selectFirstAlbumOfPage(TableView<AlbumDto> list) {
        list.getSortOrder().add(list.getColumns().get(0));
        ListAlbumSelectionListener.setAlbumSelected(list.getItems().get(0));
        list.getSelectionModel().selectFirst();
    }

    public static void selectFirstArtistOfPage(TableView<AuteurDto> list) {
        list.getSortOrder().add(list.getColumns().get(0));
        ListArtistSelectionListener.setAuteurSelected(list.getItems().get(0));
        list.getSelectionModel().selectFirst();
    }

    public static void selectFirstPlaylistOfPage(TableView<PlaylistDto> list) {
        list.getSortOrder().add(list.getColumns().get(0));
        ListPlaylistSelectionListener.setPlaylistSelected(list.getItems().get(0));
        list.getSelectionModel().selectFirst();
    }
}
