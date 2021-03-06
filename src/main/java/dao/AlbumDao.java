package dao;

import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
import db.MusiqueDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlbumDao implements AbstractDao<AlbumDb> {

    private static final Logger LOG = LogManager.getLogger(AlbumDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private static final String ALBUM = "album";
    private static final String SQL_EXCEPTION = "SQLException : ";

    @Override
    public void insert(AlbumDb albumDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery(ALBUM, albumDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void update(AlbumDb albumDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery(ALBUM, albumDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void delete(AlbumDb albumDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery(ALBUM, albumDb.getNumeroAlbum()))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public AlbumDb find(int id) {
        AlbumDb albumDb = new AlbumDb();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                ALBUM, id))) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                albumDb.setNumeroAlbum(result.getInt("numeroAlbum"));
                albumDb.setTitreAlbum(result.getString("titreAlbum"));
                albumDb.setAnneeAlbum(result.getInt("anneeAlbum"));
            }
            return albumDb;
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<AlbumDb> findAll() {
        List<AlbumDb> albumsList = new ArrayList<>();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                ALBUM))) {
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    AlbumDb albumDb = new AlbumDb();

                    albumDb.setNumeroAlbum(result.getInt("numeroAlbum"));
                    albumDb.setTitreAlbum(result.getString("titreAlbum"));
                    albumDb.setAnneeAlbum(result.getInt("anneeAlbum"));

                    albumsList.add(albumDb);
                }
            }
            return albumsList;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public List<MusiqueDb> getMusiques(AlbumDb albumDb) {
        List<MusiqueDb> musiquesList = new ArrayList<>();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                "musique", "albumMusique", albumDb.getNumeroAlbum()))) {
            try (ResultSet musiquesResult = statement.executeQuery()) {

                while (musiquesResult.next()) {
                    MusiqueDb musiqueDb = new MusiqueDb();
                    List<AuteurDb> artistes = new ArrayList<>();

                    musiqueDb.setCodeMusique(musiquesResult.getInt("codeMusique"));
                    musiqueDb.setTitreMusique(musiquesResult.getString("titreMusique"));
                    musiqueDb.setDureeMusique(musiquesResult.getString("dureeMusique"));
                    musiqueDb.setDateActionMusique(musiquesResult.getString("dateActionMusique"));
                    musiqueDb.setNomFichierMusique(musiquesResult.getString("nomFichierMusique"));

                    // MUSIC ARTIST(S) RETRIEVING
                    try (PreparedStatement artistesStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.findBySpecificColumn(
                            "posseder", "codeMusique", musiqueDb.getCodeMusique()))) {
                        try (ResultSet artistesResult = artistesStatement.executeQuery()) {
                            AuteurDao auteurDao = new AuteurDao();

                            while (artistesResult.next()) {
                                AuteurDb auteur = auteurDao.find(artistesResult.getInt("identifiantAuteur"));
                                artistes.add(auteur);
                            }
                        }
                    }

                    musiqueDb.setAlbumMusique(albumDb);
                    musiqueDb.setListeAuteurs(artistes);

                    musiquesList.add(musiqueDb);
                }
            }
            return musiquesList;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
