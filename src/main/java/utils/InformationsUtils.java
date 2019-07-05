package utils;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class InformationsUtils {

    private static final Logger LOG = LogManager.getLogger(InformationsUtils.class);
    private static final String IO_EXCEPTION = "IOException : ";

    // APP VERSION NUMBER RETRIEVING
    private String getVersionApplication() {
        try {
            URL manifestUrl = this.getClass().getResource("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(manifestUrl.openStream());
            Attributes attributes = manifest.getMainAttributes();
            return attributes.getValue("Implementation-Version");
        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
        return "";
    }

    // STAGE TITLE BUILDING
    public String buildStageTitleBar(Stage stage, String title) {
        Image logo = new Image("/icons/logo.png");
        stage.getIcons().add(logo);

        if (title == null) {
            return "WebMusic " + getVersionApplication();
        } else {
            return "WebMusic " + getVersionApplication() + " - " + title;
        }
    }
}
