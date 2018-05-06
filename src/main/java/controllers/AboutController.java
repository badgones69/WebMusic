package controllers;

import javafx.event.ActionEvent;

public class AboutController {

    public void aboutCloseButtonClicked(ActionEvent actionEvent) {
        HomeController.getAboutStage().close();
    }
}
