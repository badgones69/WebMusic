package controllers;

import controllers.home.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class AppCloseConfirmationController {

    // APP CLOSING CONFIRMATION POP-UP "YES" BUTTON CLICKED
    public void appCloseYesClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    // APP CLOSING CONFIRMATION POP-UP "NO" BUTTON CLICKED
    public void appCloseNoClicked(ActionEvent actionEvent) {
        Main.getAppCloseConfirmationStage().close();
    }
}
