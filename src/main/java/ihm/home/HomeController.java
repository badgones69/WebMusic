package ihm.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    private static Stage homeControllerStage;

    public void aboutItemClicked(ActionEvent actionEvent) {

        Stage stage = new Stage();
        this.setPrimaryStage(stage);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ihm/help/about.fxml"));
            stage.setTitle("À propos");
            stage.setScene(new Scene(root, 280, 140));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPrimaryStage(Stage stage) {
        HomeController.homeControllerStage = stage;
    }

    public static Stage getPrimaryStage() {
        return HomeController.homeControllerStage;
    }
}
