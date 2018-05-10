package controllers;

import controllers.home.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class AppCloseConfirmationController {

    public void appCloseYesClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void appCloseNoClicked(ActionEvent actionEvent) {
        Main.getAppCloseConfirmationStage().close();
    }
}
