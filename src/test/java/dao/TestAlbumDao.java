package dao;

import database.SQLiteConnection;
import db.AlbumDb;
import org.junit.After;
import org.junit.AfterClass;
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

    private AlbumDao albumDao;
    private AlbumDb albumDb;

    @Before
    public void initialise() throws Exception {

        albumDao = new AlbumDao();
        albumDb = new AlbumDb();
        albumDb.setTitreAlbum("test");
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

            assertTrue(albumDb.getNumeroAlbum().equals(resultDb.getNumeroAlbum()));
            assertTrue(albumDb.getTitreAlbum().equals(resultDb.getTitreAlbum()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() {
        albumDao.insert(albumDb);
        DaoTestsUtils.setNumeroToAlbum(albumDb);
        albumDb.setTitreAlbum("testUpdated");
        albumDao.update(albumDb);

        try {
            AlbumDb resultDb = new AlbumDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "album", albumDb.getNumeroAlbum()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setTitreAlbum(result.getString("titreAlbum"));

            assertTrue("testUpdated".equals(resultDb.getTitreAlbum()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun album n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        AlbumDb albumDb2 = new AlbumDb();
        albumDb2.setTitreAlbum("test2");

        albumDao.insert(albumDb);
        albumDao.insert(albumDb2);

        try {
            List<AlbumDb> albumsList = albumDao.findAll();

            assertTrue(albumsList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun album n'est présent dans la base de données.");
        }
    }

    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        Integer nbAlbums = albumDao.findAll().size();
        statement.executeUpdate("DELETE FROM album WHERE titreAlbum IN ('test', 'test2', 'testUpdated')");

        if(nbAlbums <= 2) {
            statement.executeUpdate("UPDATE sqlite_sequence SET seq = '0' WHERE name = 'album'");
        }
    }
}
