package ihm.help;

import ihm.home.HomeController;
import javafx.event.ActionEvent;

public class AboutController {

    public void aboutCloseButtonClicked(ActionEvent actionEvent) {
        HomeController.getPrimaryStage().close();
    }
}
