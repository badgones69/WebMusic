package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormUtils {

    // METHOD TO GET THE CURRENT DATE FORMATTED
    public static String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(currentDate);
    }

    // MUSIC LENGTH FORMAT CHECKING
    public static Boolean dureeMusiqueIsValid(String duree) {
        return duree.matches("[0-9][0-9]:[0-5][0-9]:[0-5][0-9]");
    }

    // ALBUM YEAR CHECKING
    public static Boolean anneeAlbumIsValid(String annee) {
        Boolean isValid = annee.matches("[0-9][0-9][0-9][0-9]");
        if(isValid) {
            Integer year = Integer.parseInt(annee);
            isValid = year <= Integer.parseInt(getCurrentDate().substring(6));
        }
        return isValid;
    }
}
