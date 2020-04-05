package controllers.album;

import enums.TypeAction;
import enums.TypeSource;
import modal.generic.error.AlbumErrorModal;
import modal.generic.error.CommonErrorModal;
import modal.generic.success.ActionSuccessModal;
import utils.InformationsUtils;

public class AlbumController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    protected void showSuccessPopUp(TypeAction action, boolean needRedirection) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.ALBUM, action, needRedirection);
    }

    protected void showYearErrorPopUp() {
        AlbumErrorModal.getAlbumYearErrorAlert();
    }

    protected void showTitleErrorPopUp() {
        CommonErrorModal.getTitleErrorAlert(TypeSource.ALBUM);
    }
}
