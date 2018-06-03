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

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ALBUM WHICH WAS INSERTED
    public static void setNumeroToAlbum(AlbumDb album) {
        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("album",
                    "titreAlbum", album.getTitreAlbum()));

            ResultSet result = statement.executeQuery();
            result.next();
            album.setNumeroAlbum(result.getInt("numeroAlbum"));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE MUSIC WHICH WAS INSERTED
    public static void setCodeToMusique(MusiqueDb musique) {
        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("musique",
                    "titreMusique", musique.getTitreMusique()));

            ResultSet result = statement.executeQuery();
            result.next();
            musique.setCodeMusique(result.getInt("codeMusique"));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE ARTIST WHICH WAS INSERTED
    public static void setIdentifiantToAuteur(AuteurDb auteur) {
        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("auteur",
                    "nomAuteur", auteur.getNomAuteur()));

            ResultSet result = statement.executeQuery();
            result.next();
            auteur.setIdentifiantAuteur(result.getInt("identifiantAuteur"));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // METHOD TO KNOW WHAT'S CODE WAS SET TO THE PLAYLIST WHICH WAS INSERTED
    public static void setIdToPlaylist(PlaylistDb playlist) {
        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn("playlist",
                    "intitulePlaylist", playlist.getIntitulePlaylist()));

            ResultSet result = statement.executeQuery();
            result.next();
            playlist.setIdPlaylist(result.getInt("idPlaylist"));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
