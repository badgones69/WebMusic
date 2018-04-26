package utils;

import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import db.PlaylistDb;

public class DaoQueryUtils {

    /**
     * METHODS TO INSERT A NEW RECORD IN ANY TABLE
     */

    public static String generateInsertingQuery(String tableName, Object entity) {
        String query = "INSERT INTO ";
        query += tableName;
        query += " VALUES (null, ";
        query += bindInsertedValues(entity);

        return query;
    }

    private static String bindInsertedValues(Object entity) {
        if(entity instanceof AlbumDb) {
            return bindAlbumToInsert((AlbumDb) entity);
        } else if(entity instanceof AuteurDb) {
            return bindAuteurToInsert((AuteurDb) entity);
        } else if(entity instanceof MusiqueDb) {
            return bindMusiqueToInsert((MusiqueDb) entity);
        } else if(entity instanceof PlaylistDb) {
            return bindPlaylistToInsert((PlaylistDb) entity);
        }
        return "";
    }

    private static String bindAlbumToInsert(AlbumDb albumDb) {
        return "'" + albumDb.getTitreAlbum() + "')";
    }

    private static String bindAuteurToInsert(AuteurDb auteurDb) {
        String values = "'";

        values += auteurDb.getNomAuteur() + "' , '";
        values += auteurDb.getPrenomAuteur();
        values += "')";

        return values;
    }

    private static String bindMusiqueToInsert(MusiqueDb musiqueDb) {
        String values = "'";

        values += musiqueDb.getTitreMusique() + "' , '";
        values += musiqueDb.getDureeMusique() + "' , '";
        values += musiqueDb.getDateInsertionMusique() + "' , '";
        values += musiqueDb.getNomFichierMusique() + "' , ";
        values += musiqueDb.getAlbumMusique().getNumeroAlbum();
        values += ")";

        return values;
    }

    private static String bindPlaylistToInsert(PlaylistDb playlistDb) {
        return "'" + playlistDb.getIntitulePlaylist() + "')";
    }

    /**
     * METHODS TO UPDATE A RECORD IN ANY TABLE
     */
    public static String generateUpdatingQuery(String tableName, Object entity) {
        String query = "UPDATE ";
        query += tableName;
        query += " SET ";
        query += bindUpdatedValues(tableName, entity);

        return query;
    }

    private static String bindUpdatedValues(String tableName, Object entity) {
        if(entity instanceof AlbumDb) {
            return bindAlbumToUpdate(tableName, (AlbumDb) entity);
        } else if(entity instanceof AuteurDb) {
            return bindAuteurToUpdate(tableName, (AuteurDb) entity);
        } else if(entity instanceof MusiqueDb) {
            return bindMusiqueToUpdate(tableName, (MusiqueDb) entity);
        } else if(entity instanceof PlaylistDb) {
            return bindPlaylistToUpdate(tableName, (PlaylistDb) entity);
        }
        return "";
    }

    private static String bindAlbumToUpdate(String tableName, AlbumDb albumDb) {
        return "titreAlbum = '" + albumDb.getTitreAlbum() + "'" +
               " WHERE " + getIdColumnName(tableName) + " = " + albumDb.getNumeroAlbum();
    }

    private static String bindAuteurToUpdate(String tableName, AuteurDb auteurDb) {
        String values = "";

        values += "nomAuteur = '" + auteurDb.getNomAuteur() + "', ";
        values += "prenomAuteur = '" + auteurDb.getPrenomAuteur() + "'";
        values += " WHERE ";
        values += getIdColumnName(tableName);
        values += " = ";
        values += auteurDb.getIdentifiantAuteur();

        return values;
    }

    private static String bindMusiqueToUpdate(String tableName, MusiqueDb musiqueDb) {
        String values = "";

        values += "titreMusique = '" + musiqueDb.getTitreMusique() + "', ";
        values += "dureeMusique = '" + musiqueDb.getDureeMusique() + "', ";
        values += "dateInsertionMusique = '" + musiqueDb.getDateInsertionMusique() + "', ";
        values += "nomFichierMusique = '" + musiqueDb.getNomFichierMusique() + "', ";
        values += "albumMusique = " + musiqueDb.getAlbumMusique().getNumeroAlbum();
        values += " WHERE ";
        values += getIdColumnName(tableName);
        values += " = ";
        values += musiqueDb.getCodeMusique();

        return values;
    }

    private static String bindPlaylistToUpdate(String tableName, PlaylistDb playlistDb) {
        return "intitulePlaylist = '" + playlistDb.getIntitulePlaylist() + "'" +
               " WHERE " + getIdColumnName(tableName) + " = " + playlistDb.getIdPlaylist();
    }

    /**
     * METHOD TO DELETE A RECORD IN ANY TABLE
     */
    public static String generateDeletingQuery(String tableName, Integer id) {
        String query = "DELETE FROM ";
        query += tableName;
        query += " WHERE ";
        query += getIdColumnName(tableName);
        query += " = ";
        query += id;

        return query;
    }

    /**
     * METHODS TO FIND A RECORD IN ANY TABLE
     */
    public static String generateFindingByIdQuery(String tableName, Integer id) {
        String query = "SELECT * FROM ";
        query += tableName;
        query += " WHERE ";
        query += getIdColumnName(tableName);
        query += " = ";
        query += id;

        return query;
    }

    public static String generateFindingAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String findBySpecificColumn(String tableName, String columnName, String searchedValue) {
        String query = "SELECT * FROM ";
        query += tableName;
        query += " WHERE ";
        query += columnName;
        query += " = ";
        query += "'" + searchedValue + "'";

        return query;
    }

    private static String getIdColumnName(String tableName) {
        String idColumnName = null;

        switch(tableName) {
            case "album" :
                idColumnName = "numeroAlbum";
                break;
            case "auteur" :
                idColumnName = "identifiantAuteur";
                break;
            case "musique" :
                idColumnName = "codeMusique";
                break;
            case "playlist" :
                idColumnName = "idPlaylist";
                break;
            default:
                break;
        }
        return idColumnName;
    }

}