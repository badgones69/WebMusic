package dao;

import database.SQLiteConnection;
import db.AlbumDb;
import db.AuteurDb;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestMusiqueDao {

    private MusiqueDao musiqueDao;
    private MusiqueDb musiqueDb;
    private MusiqueDb musiqueDb2;
    private AlbumDao albumDao;
    private AlbumDb albumMusique;
    private AuteurDao auteurDao;
    private List<AuteurDb> artistes;
    private AuteurDb artiste1Musique;
    private AuteurDb artiste2Musique;

    private Integer idMusique1;
    private Integer idMusique2;

    @Before
    public void initialize() throws Exception {

        musiqueDao = new MusiqueDao();
        musiqueDb = new MusiqueDb();
        musiqueDb2 = new MusiqueDb();
        albumDao = new AlbumDao();
        albumMusique = new AlbumDb();
        auteurDao = new AuteurDao();
        artistes = new ArrayList<>();
        artiste1Musique = new AuteurDb();
        artiste2Musique = new AuteurDb();

        artiste1Musique.setNomAuteur("nom1Test");
        artiste1Musique.setPrenomAuteur("prenom1Test");
        artiste2Musique.setNomAuteur("nom2Test");
        artiste2Musique.setPrenomAuteur("prenom2Test");

        artistes.add(artiste1Musique);
        artistes.add(artiste2Musique);

        albumMusique.setTitreAlbum("albumMusique");

        musiqueDb.setTitreMusique("musiqueTest");
        musiqueDb.setDureeMusique("00:03:57");
        musiqueDb.setDateActionMusique("09/04/2018");
        musiqueDb.setNomFichierMusique("musiqueTest.mp3");
        musiqueDb.setAlbumMusique(albumMusique);
        musiqueDb.setListeAuteurs(artistes);

        musiqueDb2.setTitreMusique("musiqueTest2");
        musiqueDb2.setDureeMusique("00:04:21");
        musiqueDb2.setDateActionMusique("25/04/2018");
        musiqueDb2.setNomFichierMusique("musiqueTest2.mp3");
        musiqueDb2.setAlbumMusique(albumMusique);
        musiqueDb2.setListeAuteurs(artistes);
    }

    @Test
    public void insert() {
        auteurDao.insert(artiste1Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste1Musique);
        auteurDao.insert(artiste2Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste2Musique);
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        idMusique1 = musiqueDb.getCodeMusique();

        try {
            MusiqueDb resultDb = new MusiqueDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", musiqueDb.getCodeMusique()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setCodeMusique(result.getInt("codeMusique"));
            resultDb.setTitreMusique(result.getString("titreMusique"));
            resultDb.setDureeMusique(result.getString("dureeMusique"));
            resultDb.setDateActionMusique(result.getString("dateActionMusique"));
            resultDb.setNomFichierMusique(result.getString("nomFichierMusique"));
            resultDb.setAlbumMusique(albumDao.find(result.getInt("albumMusique")));

            assertTrue(musiqueDb.getCodeMusique().equals(resultDb.getCodeMusique()));
            assertTrue(musiqueDb.getTitreMusique().equals(resultDb.getTitreMusique()));
            assertTrue(musiqueDb.getDureeMusique().equals(resultDb.getDureeMusique()));
            assertTrue(musiqueDb.getDateActionMusique().equals(resultDb.getDateActionMusique()));
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
        auteurDao.insert(artiste1Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste1Musique);
        auteurDao.insert(artiste2Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste2Musique);
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        idMusique1 = musiqueDb.getCodeMusique();
        musiqueDb.setTitreMusique("musiqueTestUpdated");
        musiqueDb.setDureeMusique("00:07:07");
        musiqueDb.setDateActionMusique("26/04/2018");
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
            resultDb.setDateActionMusique(result.getString("dateActionMusique"));
            resultDb.setNomFichierMusique(result.getString("nomFichierMusique"));

            assertTrue("musiqueTestUpdated".equals(resultDb.getTitreMusique()));
            assertTrue("00:07:07".equals(resultDb.getDureeMusique()));
            assertTrue("26/04/2018".equals(resultDb.getDateActionMusique()));
            assertTrue("musiqueTestUpdated.mp3".equals(resultDb.getNomFichierMusique()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        auteurDao.insert(artiste1Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste1Musique);
        auteurDao.insert(artiste2Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste2Musique);
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        idMusique1 = musiqueDb.getCodeMusique();
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
        auteurDao.insert(artiste1Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste1Musique);
        auteurDao.insert(artiste2Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste2Musique);
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);
        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        idMusique1 = musiqueDb.getCodeMusique();

        try {

            MusiqueDb musique = musiqueDao.find(musiqueDb.getCodeMusique());

            assertTrue(musiqueDb.getCodeMusique().equals(musique.getCodeMusique()));
            assertTrue(musiqueDb.getTitreMusique().equals(musique.getTitreMusique()));
            assertTrue(musiqueDb.getDureeMusique().equals(musique.getDureeMusique()));
            assertTrue(musiqueDb.getDateActionMusique().equals(musique.getDateActionMusique()));
            assertTrue(musiqueDb.getNomFichierMusique().equals(musique.getNomFichierMusique()));
            assertTrue(musiqueDb.getAlbumMusique().getNumeroAlbum().equals(musique.getAlbumMusique().getNumeroAlbum()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune musique n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        auteurDao.insert(artiste1Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste1Musique);
        auteurDao.insert(artiste2Musique);
        DaoTestsUtils.setIdentifiantToAuteur(artiste2Musique);
        albumDao.insert(albumMusique);
        DaoTestsUtils.setNumeroToAlbum(albumMusique);

        musiqueDao.insert(musiqueDb);
        DaoTestsUtils.setCodeToMusique(musiqueDb);
        idMusique1 = musiqueDb.getCodeMusique();
        musiqueDao.insert(musiqueDb2);
        DaoTestsUtils.setCodeToMusique(musiqueDb2);
        idMusique2 = musiqueDb2.getCodeMusique();

        try {
            List<MusiqueDb> musiquesList = musiqueDao.findAll();

            assertTrue(musiquesList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucune musique n'est présent dans la base de données.");
        }
    }

    // DATABASE CLEARING (TEST DATA REMOVING)
    @After
    public void reset() throws Exception {

        Statement statement = SQLiteConnection.getInstance().createStatement();
        statement.executeUpdate("DELETE FROM album WHERE titreAlbum = 'albumMusique'");
        statement.executeUpdate("DELETE FROM posseder WHERE codeMusique IN (" +
                idMusique1 + ", " + idMusique2 + ")");
        statement.executeUpdate("DELETE FROM musique WHERE titreMusique IN ('musiqueTest', 'musiqueTest2', 'musiqueTestUpdated')");
        statement.executeUpdate("DELETE FROM auteur WHERE prenomAuteur IN ('prenom1Test', 'prenom2Test')");
        Integer nbAlbums = albumDao.findAll().size();
        Integer nbMusiques = musiqueDao.findAll().size();
        Integer nbAuteurs = auteurDao.findAll().size();

        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + nbAlbums + "' WHERE name = 'album'");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + nbMusiques + "' WHERE name = 'musique'");
        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + nbAuteurs + "' WHERE name = 'auteur'");
    }
}
