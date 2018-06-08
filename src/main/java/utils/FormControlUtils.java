package utils;

public class FormControlUtils {

    // MUSIC LENGTH FORMAT CHECKING
    public static Boolean dureeMusiqueIsValid(String duree) {
        return duree.matches("[0-9][0-9]:[0-5][0-9]:[0-5][0-9]");
    }
}