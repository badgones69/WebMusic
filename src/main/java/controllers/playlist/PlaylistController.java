package controllers.playlist;

import db.MusiqueDb;
import dto.MusiqueDto;
import enums.TypeAction;
import enums.TypeSource;
import mapper.MusiqueMapper;
import modal.ActionSuccessModal;
import modal.PlaylistErrorModal;
import modal.TitleErrorModal;
import utils.InformationsUtils;

import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    protected List<MusiqueDb> setMusicsToPlaylist(List<MusiqueDto> musiqueDtoList) {
        List<MusiqueDb> musiquesPlaylist = new ArrayList<>();

        for (MusiqueDto musiqueDto : musiqueDtoList) {
            musiquesPlaylist.add(MusiqueMapper.toDb(musiqueDto));
        }
        return musiquesPlaylist;
    }

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.PLAYLIST, action);
    }

    protected void showMusicErrorPopUp() {
        PlaylistErrorModal.getPlaylistMusicErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        TitleErrorModal.getTitleErrorAlert(TypeSource.PLAYLIST);
    }
}
