package dao;

import database.SQLiteConnection;
import db.AuteurDb;
import db.MusiqueDb;
import utils.DaoQueryUtils;
import utils.LogUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusiqueDao implements AbstractDao<MusiqueDb> {

    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private static final String MUSIQUE = "musique";
    private static final String CODE_MUSIQUE = "codeMusique";
    private static final String ALBUM_MUSIQUE = "albumMusique";
    private static final String TITRE_MUSIQUE = "titreMusique";
    private AlbumDao albumDao = new AlbumDao();

    @Override
    public void insert(MusiqueDb musiqueDb) {
        try {
            // NEW MUSIC INSERTION
            try (PreparedStatement musiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery(MUSIQUE, musiqueDb))) {
                musiqueStatement.executeUpdate();
            }

            // INSERTED MUSIC CODE RETRIEVING
            Integer musiqueIdGenerated;
            try (PreparedStatement idMusiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.findBySpecificsColumns(
                    MUSIQUE, musiqueDb))) {
                try (ResultSet result = idMusiqueStatement.executeQuery()) {
                    result.next();
                    musiqueIdGenerated = result.getInt(CODE_MUSIQUE);
                }
            }

            // MUSIC/ARTIST(S) ASSIGNMENT
            try (PreparedStatement artistesMusiqueStatement = CONNECTION.prepareStatement("INSERT INTO posseder VALUES (?, ?)")) {

                for (AuteurDb artiste : musiqueDb.getListeAuteurs()) {
                    artistesMusiqueStatement.setInt(1, musiqueIdGenerated);
                    artistesMusiqueStatement.setInt(2, artiste.getIdentifiantAuteur());
                    artistesMusiqueStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(MusiqueDao.class, e);
        }
    }

    @Override
    public void update(MusiqueDb musiqueDb) {
        try {
            try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery(MUSIQUE, musiqueDb))) {
                statement.executeUpdate();
            }

            // MUSIC/ARTIST(S) ASSIGNMENT
            try (Statement deletingArtistesMusiqueStatement = SQLiteConnection.getInstance().createStatement()) {
                deletingArtistesMusiqueStatement.execute("DELETE FROM posseder WHERE codeMusique = " + musiqueDb.getCodeMusique());
            }

            try (PreparedStatement insertingArtistesMusiqueStatement = CONNECTION.prepareStatement("INSERT INTO posseder VALUES (?, ?)")) {

                for (AuteurDb artiste : musiqueDb.getListeAuteurs()) {
                    insertingArtistesMusiqueStatement.setInt(1, musiqueDb.getCodeMusique());
                    insertingArtistesMusiqueStatement.setInt(2, artiste.getIdentifiantAuteur());
                    insertingArtistesMusiqueStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(MusiqueDao.class, e);
        }
    }

    @Override
    public void delete(MusiqueDb musiqueDb) {
        try {
            MusiqueDao musiqueDao = new MusiqueDao();

            try (PreparedStatement artistesMusiqueStatement = CONNECTION.prepareStatement("DELETE FROM posseder WHERE codeMusique=" + musiqueDb.getCodeMusique())) {
                artistesMusiqueStatement.executeUpdate();
            }

            try (PreparedStatement musiqueStatement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery(MUSIQUE, musiqueDb.getCodeMusique()))) {
                musiqueStatement.executeUpdate();
            }

            Integer nbMusiques = musiqueDao.findAll().size();
            try (Statement statement = SQLiteConnection.getInstance().createStatement()) {
                statement.execute("UPDATE sqlite_sequence SET seq = '" + nbMusiques + "' WHERE name = 'musique'");
            }

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(MusiqueDao.class, e);
        }
    }

    @Override
    public MusiqueDb find(int id) {
        MusiqueDb musiqueDb = new MusiqueDb();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                MUSIQUE, id))) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                musiqueDb.setCodeMusique(result.getInt(CODE_MUSIQUE));
                musiqueDb.setTitreMusique(result.getString(TITRE_MUSIQUE));
                musiqueDb.setDureeMusique(result.getString("dureeMusique"));
                musiqueDb.setDateActionMusique(result.getString("dateActionMusique"));
                musiqueDb.setNomFichierMusique(result.getString("nomFichierMusique"));

                // MUSIC ARTIST(S) RETRIEVING
                List<AuteurDb> artistes;
                try (PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                        "posseder", CODE_MUSIQUE, musiqueDb.getCodeMusique()))) {
                    try (ResultSet artistesResult = artistesStatement.executeQuery()) {
                        AuteurDao auteurDao = new AuteurDao();
                        artistes = new ArrayList<>();

                        while (artistesResult.next()) {
                            AuteurDb auteur = auteurDao.find(artistesResult.getInt("identifiantAuteur"));
                            artistes.add(auteur);
                        }
                    }
                }

                musiqueDb.setListeAuteurs(artistes);

                if (result.getInt(ALBUM_MUSIQUE) != 0) {
                    musiqueDb.setAlbumMusique(albumDao.find(result.getInt(ALBUM_MUSIQUE)));
                }
            }
            return musiqueDb;

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(MusiqueDao.class, e);
        }
        return null;
    }

    @Override
    public List<MusiqueDb> findAll() {
        List<MusiqueDb> musiquesList = new ArrayList<>();

        try (PreparedStatement musiquesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                MUSIQUE))) {
            try (ResultSet musiquesResult = musiquesStatement.executeQuery()) {


                while (musiquesResult.next()) {
                    MusiqueDb musiqueDb = new MusiqueDb();
                    List<AuteurDb> artistes = new ArrayList<>();

                    musiqueDb.setCodeMusique(musiquesResult.getInt(CODE_MUSIQUE));
                    musiqueDb.setTitreMusique(musiquesResult.getString(TITRE_MUSIQUE));
                    musiqueDb.setDureeMusique(musiquesResult.getString("dureeMusique"));
                    musiqueDb.setDateActionMusique(musiquesResult.getString("dateActionMusique"));
                    musiqueDb.setNomFichierMusique(musiquesResult.getString("nomFichierMusique"));

                    // MUSIC ARTIST(S) RETRIEVING
                    try (PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                            "posseder", CODE_MUSIQUE, musiqueDb.getCodeMusique()))) {
                        try (ResultSet artistesResult = artistesStatement.executeQuery()) {
                            AuteurDao auteurDao = new AuteurDao();

                            while (artistesResult.next()) {
                                AuteurDb auteur = auteurDao.find(artistesResult.getInt("identifiantAuteur"));
                                artistes.add(auteur);
                            }
                        }
                    }

                    if (musiquesResult.getInt(ALBUM_MUSIQUE) != 0) {
                        musiqueDb.setAlbumMusique(albumDao.find(musiquesResult.getInt(ALBUM_MUSIQUE)));
                    }
                    musiqueDb.setListeAuteurs(artistes);
                    musiquesList.add(musiqueDb);
                }
            }
            return musiquesList;

        } catch (SQLException e) {
            LogUtils.generateSQLExceptionLog(MusiqueDao.class, e);
        }
        return Collections.emptyList();
    }
}
