package controllers.album;

import enums.TypeAction;
import enums.TypeSource;
import modal.error.CommonErrorModal;
import modal.success.ActionSuccessModal;
import modal.error.AlbumErrorModal;
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
        CommonErrorModal.getTitleErrorAlert(TypeSource.ALBUM);
    }
}
