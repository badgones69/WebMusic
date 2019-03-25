package dao;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AlbumDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DaoQueryUtils;
import utils.DaoTestsUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestAlbumDao {

    private static final Logger LOG = LogManager.getLogger(TestAlbumDao.class);
    private static final String SQL_EXCEPTION = "SQLException : ";

    private AlbumDao albumDao;
    private AlbumDb albumDb;

    @Before
    public void initialise() {
        Home.initializeDB();

        albumDao = new AlbumDao();
        albumDb = new AlbumDb();
        albumDb.setTitreAlbum("test");
        albumDb.setAnneeAlbum(2015);
    }

    @Test
    public void insert() {
        albumDao.insert(albumDb);
        DaoTestsUtils.setNumeroToAlbum(albumDb);

        try {
            AlbumDb resultDb = new AlbumDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "album", albumDb.getNumeroAlbum()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setNumeroAlbum(result.getInt("numeroAlbum"));
            resultDb.setTitreAlbum(result.getString("titreAlbum"));
            resultDb.setAnneeAlbum(result.getInt("anneeAlbum"));

            assertTrue(albumDb.getNumeroAlbum().equals(resultDb.getNumeroAlbum()));
            assertTrue(albumDb.getTitreAlbum().equals(resultDb.getTitreAlbum()));
            assertTrue(albumDb.getAnneeAlbum().equals(resultDb.getAnneeAlbum()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void update() {
        albumDao.insert(albumDb);
        DaoTestsUtils.setNumeroToAlbum(albumDb);
        albumDb.setTitreAlbum("testUpdated");
        albumDb.setAnneeAlbum(2018);
        albumDao.update(albumDb);

        try {
            AlbumDb resultDb = new AlbumDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "album", albumDb.getNumeroAlbum()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setTitreAlbum(result.getString("titreAlbum"));
            resultDb.setAnneeAlbum(result.getInt("anneeAlbum"));

            assertTrue("testUpdated".equals(resultDb.getTitreAlbum()));
            assertTrue(resultDb.getAnneeAlbum() == 2018);

            result.close();
            statement.close();

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void delete() {
        albumDao.insert(albumDb);
        DaoTestsUtils.setNumeroToAlbum(albumDb);
        albumDao.delete(albumDb);

        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "album", albumDb.getNumeroAlbum()));
            assertTrue(statement.execute());
            ResultSet result = statement.executeQuery();
            assertTrue(result.isClosed());

            result.close();
            statement.close();

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void find() {
        albumDao.insert(albumDb);
        DaoTestsUtils.setNumeroToAlbum(albumDb);

        try {

            AlbumDb album = albumDao.find(albumDb.getNumeroAlbum());

            assertTrue(albumDb.getNumeroAlbum().equals(album.getNumeroAlbum()));
            assertTrue(albumDb.getTitreAlbum().equals(album.getTitreAlbum()));
            assertTrue(albumDb.getAnneeAlbum().equals(album.getAnneeAlbum()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun album n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        AlbumDb albumDb2 = new AlbumDb();
        albumDb2.setTitreAlbum("test2");
        albumDb2.setAnneeAlbum(2017);

        albumDao.insert(albumDb);
        albumDao.insert(albumDb2);

        try {
            List<AlbumDb> albumsList = albumDao.findAll();

            assertTrue(albumsList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun album n'est présent dans la base de données.");
        }
    }

    // DATABASE CLEARING (TEST DATA REMOVING)
    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        statement.executeUpdate("DELETE FROM album WHERE titreAlbum IN ('test', 'test2', 'testUpdated')");

        Integer lastIdAlbum = 0;

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("album"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdAlbum = sequenceResult.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdAlbum + "' WHERE name = 'album'");
        statement.close();
    }
}
