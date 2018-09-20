package controllers.help;

import controllers.common.MenuController;
import javafx.event.ActionEvent;

public class AboutController {

    // ABOUT POP-UP "OK" BUTTON CLICKED
    public void aboutCloseButtonClicked(ActionEvent actionEvent) {
        MenuController.getAboutStage().close();
    }
}
