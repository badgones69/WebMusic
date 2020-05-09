package utils;

import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import db.PlaylistDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoTestsUtils {

    private DaoTestsUtils() {
        LogUtils.generateConstructorLog(DaoTestsUtils.class);
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ALBUM WHICH WAS INSERTED
    public static void setNumeroToAlbum(AlbumDb album) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(
                    DaoQueryUtils.findBySpecificsColumns("album", album))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    album.setNumeroAlbum(result.getInt("numeroAlbum"));
                }
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(DaoTestsUtils.class, e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE MUSIC WHICH WAS INSERTED
    public static void setCodeToMusique(MusiqueDb musique) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(
                    DaoQueryUtils.findBySpecificsColumns("musique", musique))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    musique.setCodeMusique(result.getInt("codeMusique"));
                }
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(DaoTestsUtils.class, e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ARTIST WHICH WAS INSERTED
    public static void setIdentifiantToAuteur(AuteurDb auteur) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(
                    DaoQueryUtils.findBySpecificsColumns("auteur", auteur))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    auteur.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
                }
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(DaoTestsUtils.class, e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE PLAYLIST WHICH WAS INSERTED
    public static void setIdToPlaylist(PlaylistDb playlist) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(
                    DaoQueryUtils.findBySpecificsColumns("playlist", playlist))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    playlist.setIdPlaylist(result.getInt("idPlaylist"));
                }
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(DaoTestsUtils.class, e);
        }
    }
}
