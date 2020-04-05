package controllers.artist;

import enums.TypeAction;
import enums.TypeSource;
import modal.generic.error.ArtistErrorModal;
import modal.generic.success.ActionSuccessModal;
import utils.InformationsUtils;

public class ArtistController {

    protected final InformationsUtils informationsUtils = new InformationsUtils();

    protected void showSuccessPopUp(TypeAction action) {
        ActionSuccessModal.getActionSuccessAlert(TypeSource.ARTIST, action, true);
    }

    protected void showNameErrorPopUp() {
        ArtistErrorModal.getArtistNameErrorAlert();
    }
}
