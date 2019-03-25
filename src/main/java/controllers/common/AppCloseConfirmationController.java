package controllers.common;

import javafx.application.Platform;

public class AppCloseConfirmationController {

    // APP CLOSING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void appCloseYesClicked() {
        Platform.exit();
    }

    // APP CLOSING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void appCloseNoClicked() {
        new Home().getAppCloseConfirmationStage().close();
    }
}
