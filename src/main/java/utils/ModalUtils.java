package utils;

import enums.TypeSource;

public class ModalUtils {

    private ModalUtils() {
        LogUtils.generateConstructorLog(ModalUtils.class);
    }

    public static String getSystemLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String getWordThisFeminised(TypeSource source) {
        switch (source) {
            case MUSIC:
            case PLAYLIST:
                return "cette";
            case ARTIST:
                return "cet(te)";
            default:
                return "cet";
        }
    }

    public static String feminiseWord(String word, TypeSource source) {
        if (TypeSource.ARTIST.equals(source) || TypeSource.COMMON.equals(source)) {
            return word + "(e)";
        } else if (TypeSource.MUSIC.equals(source) || TypeSource.PLAYLIST.equals(source)) {
            return word + "e";
        } else {
            return word;
        }
    }
}
