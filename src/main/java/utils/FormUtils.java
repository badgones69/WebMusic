package utils;

import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormUtils {

    private FormUtils() {
        LogUtils.generateConstructorLog(FormUtils.class);
    }

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
        boolean isValid = annee.matches("[0-9][0-9][0-9][0-9]");
        if (isValid) {
            Integer year = Integer.parseInt(annee);
            isValid = year <= Integer.parseInt(getCurrentDate().substring(6));
        }
        return isValid;
    }

    // METHOD TO GET THE FRENCH COLLATOR
    public static Collator getFrenchCollator() {
        Collator frenchCollator = Collator.getInstance(Locale.FRANCE);
        frenchCollator.setStrength(Collator.SECONDARY);

        return frenchCollator;
    }
}
