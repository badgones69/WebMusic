package utils;

import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import db.PlaylistDb;

public class DaoQueryUtils {

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String SELECT_FROM = "SELECT * FROM ";
    private static final String COMA_SEPARATOR = "' , '";

    private DaoQueryUtils() {
        LogUtils.generateConstructorLog(DaoQueryUtils.class);
    }

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
        if (entity instanceof AlbumDb) {
            return bindAlbumToInsert((AlbumDb) entity);
        } else if (entity instanceof AuteurDb) {
            return bindAuteurToInsert((AuteurDb) entity);
        } else if (entity instanceof MusiqueDb) {
            return bindMusiqueToInsert((MusiqueDb) entity);
        } else if (entity instanceof PlaylistDb) {
            return bindPlaylistToInsert((PlaylistDb) entity);
        }
        return "";
    }

    private static String bindAlbumToInsert(AlbumDb albumDb) {
        String values = "'";

        values += albumDb.getTitreAlbum().replace("'", "''") + "',";

        if (albumDb.getAnneeAlbum() == null) {
            values += "null";
        } else {
            values += albumDb.getAnneeAlbum();
        }

        values += ")";

        return values;
    }

    private static String bindAuteurToInsert(AuteurDb auteurDb) {
        String values = "'";

        values += auteurDb.getNomAuteur().replace("'", "''") + "' , ";

        if (auteurDb.getPrenomAuteur() == null) {
            values += "null";
        } else {
            values += "'";
            values += auteurDb.getPrenomAuteur().replace("'", "''");
            values += "'";
        }

        values += ")";

        return values;
    }

    private static String bindMusiqueToInsert(MusiqueDb musiqueDb) {
        String values = "'";

        values += musiqueDb.getTitreMusique().replace("'", "''") + COMA_SEPARATOR;
        values += musiqueDb.getDureeMusique() + COMA_SEPARATOR;
        values += musiqueDb.getDateActionMusique() + COMA_SEPARATOR;
        values += musiqueDb.getNomFichierMusique().replace("'", "''") + "' , ";
        values += musiqueDb.getAlbumMusique().getNumeroAlbum();
        values += ")";

        return values;
    }

    private static String bindPlaylistToInsert(PlaylistDb playlistDb) {
        String values = "'";

        values += playlistDb.getIntitulePlaylist().replace("'", "''") + COMA_SEPARATOR;
        values += playlistDb.getDateActionPlaylist();
        values += "')";

        return values;
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
        if (entity instanceof AlbumDb) {
            return bindAlbumToUpdate(tableName, (AlbumDb) entity);
        } else if (entity instanceof AuteurDb) {
            return bindAuteurToUpdate(tableName, (AuteurDb) entity);
        } else if (entity instanceof MusiqueDb) {
            return bindMusiqueToUpdate(tableName, (MusiqueDb) entity);
        } else if (entity instanceof PlaylistDb) {
            return bindPlaylistToUpdate(tableName, (PlaylistDb) entity);
        }
        return "";
    }

    private static String bindAlbumToUpdate(String tableName, AlbumDb albumDb) {
        String values = "";

        values += "titreAlbum = '" + albumDb.getTitreAlbum().replace("'", "''") + "'";
        if (albumDb.getAnneeAlbum() != null) {
            values += ", ";
            values += "anneeAlbum = " + albumDb.getAnneeAlbum();
        }
        values += WHERE;
        values += getIdColumnName(tableName);
        values += " = ";
        values += albumDb.getNumeroAlbum();

        return values;
    }

    private static String bindAuteurToUpdate(String tableName, AuteurDb auteurDb) {
        String values = "";

        values += "nomAuteur = '" + auteurDb.getNomAuteur().replace("'", "''") + "'";
        if (auteurDb.getPrenomAuteur() != null) {
            values += ", ";
            values += "prenomAuteur = '" + auteurDb.getPrenomAuteur().replace("'", "''") + "'";
        }
        values += WHERE;
        values += getIdColumnName(tableName);
        values += " = ";
        values += auteurDb.getIdentifiantAuteur();

        return values;
    }

    private static String bindMusiqueToUpdate(String tableName, MusiqueDb musiqueDb) {
        String values = "";

        values += "titreMusique = '" + musiqueDb.getTitreMusique().replace("'", "''") + "', ";
        values += "dureeMusique = '" + musiqueDb.getDureeMusique() + "', ";
        values += "dateActionMusique = '" + musiqueDb.getDateActionMusique() + "', ";
        values += "nomFichierMusique = '" + musiqueDb.getNomFichierMusique().replace("'", "''") + "', ";
        values += "albumMusique = " + musiqueDb.getAlbumMusique().getNumeroAlbum();
        values += WHERE;
        values += getIdColumnName(tableName);
        values += " = ";
        values += musiqueDb.getCodeMusique();

        return values;
    }

    private static String bindPlaylistToUpdate(String tableName, PlaylistDb playlistDb) {
        String values = "intitulePlaylist = '" + playlistDb.getIntitulePlaylist().replace("'", "''") + "', ";
        values += "dateActionPlaylist = '" + playlistDb.getDateActionPlaylist() + "'";
        values += WHERE + getIdColumnName(tableName) + " = " + playlistDb.getIdPlaylist();

        return values;
    }

    /**
     * METHOD TO DELETE A RECORD IN ANY TABLE
     */

    public static String generateDeletingQuery(String tableName, Integer id) {
        String query = "DELETE FROM ";
        query += tableName;
        query += WHERE;
        query += getIdColumnName(tableName);
        query += " = ";
        query += id;

        return query;
    }

    /**
     * METHODS TO FIND A RECORD IN ANY TABLE
     */

    public static String generateFindingByIdQuery(String tableName, Integer id) {
        String query = SELECT_FROM;
        query += tableName;
        query += WHERE;
        query += getIdColumnName(tableName);
        query += " = ";
        query += id;

        return query;
    }

    public static String generateFindingAllQuery(String tableName) {
        return SELECT_FROM + tableName;
    }

    public static String findBySpecificColumn(String tableName, String columnName, String searchedValue) {
        String query = SELECT_FROM;
        query += tableName;
        query += WHERE;
        query += columnName;
        query += " = ";
        query += "'" + searchedValue.replace("'", "''") + "'";

        return query;
    }

    public static String findBySpecificColumn(String tableName, String columnName, Integer searchedValue) {
        String query = SELECT_FROM;
        query += tableName;
        query += WHERE;
        query += columnName;
        query += " = ";
        query += searchedValue;

        return query;
    }

    public static String findBySpecificsColumns(String tableName, Object entity) {
        String query = SELECT_FROM;
        query += tableName;
        query += WHERE;
        query += generateSearchingConditions(entity);

        return query;
    }

    private static String generateSearchingConditions(Object entity) {
        if (entity instanceof AlbumDb) {
            return bindAlbumToSearch((AlbumDb) entity);
        } else if (entity instanceof AuteurDb) {
            return bindAuteurToSearch((AuteurDb) entity);
        } else if (entity instanceof MusiqueDb) {
            return bindMusiqueToSearch((MusiqueDb) entity);
        } else if (entity instanceof PlaylistDb) {
            return bindPlaylistToSearch((PlaylistDb) entity);
        }
        return "";
    }

    private static String bindAlbumToSearch(AlbumDb albumDb) {
        String conditions = "";

        conditions += "titreAlbum = '" + albumDb.getTitreAlbum().replace("'", "''") + "'";
        conditions += AND;
        if (albumDb.getAnneeAlbum() != null) {
            conditions += "anneeAlbum = " + albumDb.getAnneeAlbum();
        } else {
            conditions += "anneeAlbum IS NULL";
        }

        return conditions;
    }

    private static String bindAuteurToSearch(AuteurDb auteurDb) {
        String conditions = "";

        conditions += "nomAuteur = '" + auteurDb.getNomAuteur().replace("'", "''") + "'";
        conditions += AND;

        if (auteurDb.getPrenomAuteur() != null) {
            conditions += "prenomAuteur = '" + auteurDb.getPrenomAuteur().replace("'", "''") + "'";
        } else {
            conditions += "prenomAuteur IS NULL";
        }

        return conditions;
    }

    private static String bindMusiqueToSearch(MusiqueDb musiqueDb) {
        String conditions = "";

        conditions += "titreMusique = '" + musiqueDb.getTitreMusique().replace("'", "''") + "'";
        conditions += AND + "dureeMusique = '" + musiqueDb.getDureeMusique() + "'";
        conditions += AND + "dateActionMusique = '" + musiqueDb.getDateActionMusique() + "'";
        conditions += AND + "nomFichierMusique = '" + musiqueDb.getNomFichierMusique().replace("'", "''") + "'";
        conditions += AND;
        if (musiqueDb.getAlbumMusique().getNumeroAlbum() != null) {
            conditions += "albumMusique = " + musiqueDb.getAlbumMusique().getNumeroAlbum();
        } else {
            conditions += "albumMusique IS NULL";
        }

        return conditions;
    }

    private static String bindPlaylistToSearch(PlaylistDb playlistDb) {
        String conditions = "intitulePlaylist = '" + playlistDb.getIntitulePlaylist().replace("'", "''") + "'";
        conditions += AND + "dateActionPlaylist = '" + playlistDb.getDateActionPlaylist() + "'";

        return conditions;
    }

    /**
     * METHOD TO KNOW THE LAST ID USED IN ANY TABLE
     */

    public static String getLastIdOfTable(String tableName) {
        String query = "SELECT MAX(";
        query += getIdColumnName(tableName);
        query += ") FROM ";
        query += tableName;

        return query;
    }

    /**
     * METHOD TO KNOW WHAT FIELD IS TABLE'S PRIMARY KEY
     */

    private static String getIdColumnName(String tableName) {
        String idColumnName = null;

        switch (tableName) {
            case "album":
                idColumnName = "numeroAlbum";
                break;
            case "auteur":
                idColumnName = "identifiantAuteur";
                break;
            case "musique":
                idColumnName = "codeMusique";
                break;
            case "playlist":
                idColumnName = "idPlaylist";
                break;
            default:
                break;
        }
        return idColumnName;
    }

}