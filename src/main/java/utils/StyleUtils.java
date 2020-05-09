package utils;


public class StyleUtils {

    private static final String STYLESHEET = "/styles/style.css";
    private static final String FORM_FIELD = "formField";
    private static final String PANEL = "panel";
    private static final String MODAL_BODY = "modalBody";

    private StyleUtils() {
        LogUtils.generateConstructorLog(StyleUtils.class);
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
