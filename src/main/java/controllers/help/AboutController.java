package controllers.help;

import controllers.common.MenuController;

public class AboutController {

    // ABOUT POP-UP "OK" BUTTON CLICKED
    public void aboutCloseButtonClicked() {
        MenuController.getAboutStage().close();
    }
}
