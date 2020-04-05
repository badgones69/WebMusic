package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StyleUtils {

    private static final Logger LOG = LogManager.getLogger(StyleUtils.class);

    private static final String STYLESHEET = "/styles/style.css";
    private static final String FORM_FIELD = "formField";
    private static final String PANEL = "panel";
    private static final String MODAL_BODY = "modalBody";

    private StyleUtils() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    public static String getStylesheet() {
        return STYLESHEET;
    }

    public static String getFormFieldClass() {
        return FORM_FIELD;
    }

    public static String getPanelClass() {
        return PANEL;
    }

    public static String getModalBodyClass() {
        return MODAL_BODY;
    }
}
