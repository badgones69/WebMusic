package utils;

import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import db.PlaylistDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoTestsUtils {

    private static final Logger LOG = LogManager.getLogger(DaoTestsUtils.class);
    private static final String SQL_EXCEPTION = "SQLException : ";

    private DaoTestsUtils() {
        LOG.error("This class cannot be instantiated because it's an 'Utility class'");
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ALBUM WHICH WAS INSERTED
    public static void setNumeroToAlbum(AlbumDb album) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("album",
                    "titreAlbum", album.getTitreAlbum()))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    album.setNumeroAlbum(result.getInt("numeroAlbum"));
                }
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE MUSIC WHICH WAS INSERTED
    public static void setCodeToMusique(MusiqueDb musique) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("musique",
                    "titreMusique", musique.getTitreMusique()))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    musique.setCodeMusique(result.getInt("codeMusique"));
                }
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ARTIST WHICH WAS INSERTED
    public static void setIdentifiantToAuteur(AuteurDb auteur) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("auteur",
                    "nomAuteur", auteur.getNomAuteur()))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    auteur.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
                }
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE PLAYLIST WHICH WAS INSERTED
    public static void setIdToPlaylist(PlaylistDb playlist) {
        try {
            try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("playlist",
                    "intitulePlaylist", playlist.getIntitulePlaylist()))) {

                try (ResultSet result = statement.executeQuery()) {
                    result.next();
                    playlist.setIdPlaylist(result.getInt("idPlaylist"));
                }
            }

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }
}
