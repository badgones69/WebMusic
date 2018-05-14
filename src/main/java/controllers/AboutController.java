package controllers;

import javafx.event.ActionEvent;

public class AboutController {

    public void aboutCloseButtonClicked(ActionEvent actionEvent) {
        MenuController.getAboutStage().close();
    }
}
