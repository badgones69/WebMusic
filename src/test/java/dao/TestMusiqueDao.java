package dao;

import database.SQLiteConnection;
import db.AlbumDb;
import db.MusiqueDb;
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

public class TestMusiqueDao {

    private MusiqueDao musiqueDao;
    private MusiqueDb musiqueDb;
    private AlbumDao albumDao;
    private AlbumDb albumMusique;

    @Before
    public void initialize() throws Exception {

        musiqueDao = new MusiqueDao();
        musiqueDb = new MusiqueDb();

        albumDao = new AlbumDao();
        albumMusique = new AlbumDb();
        albumMusique.setTitreAlbum("albumMusique");

        musiqueDb.setTitreMusique("musiqueTest");
        musiqueDb.setDureeMusique("00:03:57");
        musiqueDb.setDateInsertionMusique("09/04/2018");
        musiqueDb.setNomFichierMusique("musiqueTest.mp3");
        musiqueDb.setAlbumMusique(albumMusique);
    }

    @Test
    public void insert() {
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);

        try {
            MusiqueDb resultDb = new MusiqueDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", musiqueDb.getCodeMusique()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setCodeMusique(result.getInt("codeMusique"));
            resultDb.setTitreMusique(result.getString("titreMusique"));
            resultDb.setDureeMusique(result.getString("dureeMusique"));
            resultDb.setDateInsertionMusique(result.getString("dateInsertionMusique"));
            resultDb.setNomFichierMusique(result.getString("nomFichierMusique"));
            resultDb.setAlbumMusique(albumDao.find(result.getInt("albumMusique")));

            assertTrue(musiqueDb.getCodeMusique().equals(resultDb.getCodeMusique()));
            assertTrue(musiqueDb.getTitreMusique().equals(resultDb.getTitreMusique()));
            assertTrue(musiqueDb.getDureeMusique().equals(resultDb.getDureeMusique()));
            assertTrue(musiqueDb.getDateInsertionMusique().equals(resultDb.getDateInsertionMusique()));
            assertTrue(musiqueDb.getNomFichierMusique().equals(resultDb.getNomFichierMusique()));
            assertTrue(musiqueDb.getAlbumMusique().getNumeroAlbum().equals(resultDb.getAlbumMusique().getNumeroAlbum()));

            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() {
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        musiqueDb.setTitreMusique("musiqueTestUpdated");
        musiqueDb.setDureeMusique("00:07:07");
        musiqueDb.setDateInsertionMusique("26/04/2018");
        musiqueDb.setNomFichierMusique("musiqueTestUpdated.mp3");
        musiqueDao.update(musiqueDb);

        try {
            MusiqueDb resultDb = new MusiqueDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", musiqueDb.getCodeMusique()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setTitreMusique(result.getString("titreMusique"));
            resultDb.setDureeMusique(result.getString("dureeMusique"));
            resultDb.setDateInsertionMusique(result.getString("dateInsertionMusique"));
            resultDb.setNomFichierMusique(result.getString("nomFichierMusique"));

            assertTrue("musiqueTestUpdated".equals(resultDb.getTitreMusique()));
            assertTrue("00:07:07".equals(resultDb.getDureeMusique()));
            assertTrue("26/04/2018".equals(resultDb.getDateInsertionMusique()));
            assertTrue("musiqueTestUpdated.mp3".equals(resultDb.getNomFichierMusique()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        albumDao.insert(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        musiqueDao.delete(musiqueDb);

        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", musiqueDb.getCodeMusique()));
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
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);

        try {

            MusiqueDb musique = musiqueDao.find(musiqueDb.getCodeMusique());

            assertTrue(musiqueDb.getCodeMusique().equals(musique.getCodeMusique()));
            assertTrue(musiqueDb.getTitreMusique().equals(musique.getTitreMusique()));
            assertTrue(musiqueDb.getDureeMusique().equals(musique.getDureeMusique()));
            assertTrue(musiqueDb.getDateInsertionMusique().equals(musique.getDateInsertionMusique()));
            assertTrue(musiqueDb.getNomFichierMusique().equals(musique.getNomFichierMusique()));
            assertTrue(musiqueDb.getAlbumMusique().getNumeroAlbum().equals(musique.getAlbumMusique().getNumeroAlbum()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune musique n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);

        MusiqueDb musiqueDb2 = new MusiqueDb();

        musiqueDb2.setTitreMusique("musiqueTest2");
        musiqueDb2.setDureeMusique("00:04:21");
        musiqueDb2.setDateInsertionMusique("25/04/2018");
        musiqueDb2.setNomFichierMusique("musiqueTest2.mp3");
        musiqueDb2.setAlbumMusique(albumMusique);

        musiqueDao.insert(musiqueDb);
        musiqueDao.insert(musiqueDb2);

        try {
            List<MusiqueDb> musiquesList = musiqueDao.findAll();

            assertTrue(musiquesList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune musique n'est présent dans la base de données.");
        }
    }

    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        Integer nbAlbums = albumDao.findAll().size();
        Integer nbMusiques = musiqueDao.findAll().size();
        statement.executeUpdate("DELETE FROM album WHERE titreAlbum = 'albumMusique'");
        statement.executeUpdate("DELETE FROM musique WHERE titreMusique IN ('musiqueTest', 'musiqueTest2', 'musiqueTestUpdated')");

        if(nbAlbums <= 1) {
            statement.executeUpdate("UPDATE sqlite_sequence SET seq = '0' WHERE name = 'album'");
        }

        if(nbMusiques <= 2) {
            statement.executeUpdate("UPDATE sqlite_sequence SET seq = '0' WHERE name = 'musique'");
        }
    }
}
