package utils;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class InformationsUtils {

    // APP VERSION NUMBER RETRIEVING
    private String getVersionApplication() {
        URL manifestUrl = this.getClass().getResource("/META-INF/MANIFEST.MF");
        Manifest manifest = null;
        try {
            manifest = new Manifest(manifestUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Attributes attributes = manifest.getMainAttributes();

        return attributes.getValue("Implementation-Version");
    }

    // STAGE TITLE BUILDING
    public String buildStageTitle(String title) {
        if (title == null) {
            return "WebMusic " + getVersionApplication();
        } else {
            return "WebMusic " + getVersionApplication() + " - " + title;
        }
    }
}
