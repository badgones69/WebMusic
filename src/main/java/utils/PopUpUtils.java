package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PopUpUtils {

    private static final Logger LOG = LogManager.getLogger(PopUpUtils.class);

    // CUURENT ACTION DONE (WITH A FORM OR A LIST)
    private static String actionDone = "";

    private PopUpUtils() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    /**
     * GETTERS AND SETTERS
     */

    public static String getActionDone() {
        return actionDone;
    }

    public static void setActionDone(String actionDone) {
        PopUpUtils.actionDone = actionDone;
    }
}
