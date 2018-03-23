package utils;

import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class InformationsUtils {

    public String getVersionApplication() {
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
}
