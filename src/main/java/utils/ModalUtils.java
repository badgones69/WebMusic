package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModalUtils {

    private static final Logger LOG = LogManager.getLogger(ModalUtils.class);

    // CUURENT ACTION DONE (WITH A FORM OR A LIST)
    private static String actionDone = "";

    private ModalUtils() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    /**
     * GETTERS AND SETTERS
     */

    public static String getActionDone() {
        return actionDone;
    }

    public static void setActionDone(String actionDone) {
        ModalUtils.actionDone = actionDone;
    }

    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator");
    }
}
