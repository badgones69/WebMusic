package controllers.album;

import enums.TypeAction;
import enums.TypeSource;
import modal.ActionSuccessModal;
import modal.AlbumErrorModal;
import modal.TitleErrorModal;
import utils.InformationsUtils;

public class AlbumController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.ALBUM, action);
    }

    protected void showYearErrorPopUp() {
        AlbumErrorModal.getAlbumYearErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        TitleErrorModal.getTitleErrorAlert(TypeSource.ALBUM);
    }
}
