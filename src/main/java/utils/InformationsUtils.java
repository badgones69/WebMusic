package utils;

import enums.TypeVersion;
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

    // VERSION NUMBER RETRIEVING
    public String getVersion(TypeVersion typeVersion) {
        try {
            URL manifestUrl = this.getClass().getResource("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(manifestUrl.openStream());
            Attributes attributes = manifest.getMainAttributes();
            return attributes.getValue(typeVersion.getLibelle());
        } catch (IOException e) {
            LOG.error(IO_EXCEPTION + e.getMessage(), e);
        }
        return "";
    }

    // STAGE TITLE BUILDING
    public String buildStageTitleBar(Stage stage, String title) {
        Image logo = new Image("/icons/logo.png");
        stage.getIcons().add(logo);

        String stageTitleBar = "WebMusic " + getVersion(TypeVersion.APPLICATION);

        if (title != null) {
            stageTitleBar += " - " + title;
        }
        return stageTitleBar;
    }
}
