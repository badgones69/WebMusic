package controllers.artist;

import enums.TypeAction;
import enums.TypeSource;
import modal.error.ArtistErrorModal;
import modal.success.ActionSuccessModal;
import utils.InformationsUtils;

public class ArtistController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.ARTIST, action);
    }

    protected void showNameErrorPopUp() {
        ArtistErrorModal.getArtistNameErrorAlert();
    }
}
