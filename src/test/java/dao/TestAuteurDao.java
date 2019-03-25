package dao;

import controllers.common.Home;
import database.SQLiteConnection;
import db.AuteurDb;
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

public class TestAuteurDao {

    private static final Logger LOG = LogManager.getLogger(TestAuteurDao.class);
    private static final String SQL_EXCEPTION = "SQLException : ";

    private AuteurDao auteurDao;
    private AuteurDb auteurDb;

    @Before
    public void initialize() {
        Home.initializeDB();

        auteurDao = new AuteurDao();
        auteurDb = new AuteurDb();
        auteurDb.setNomAuteur("nomTest");
        auteurDb.setPrenomAuteur("prenomTest");
    }

    @Test
    public void insert() {
        auteurDao.insert(auteurDb);
        DaoTestsUtils.setIdentifiantToAuteur(auteurDb);

        try {
            AuteurDb resultDb = new AuteurDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "auteur", auteurDb.getIdentifiantAuteur()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
            resultDb.setNomAuteur(result.getString("nomAuteur"));
            resultDb.setPrenomAuteur(result.getString("prenomAuteur"));

            assertTrue(auteurDb.getIdentifiantAuteur().equals(resultDb.getIdentifiantAuteur()));
            assertTrue(auteurDb.getNomAuteur().equals(resultDb.getNomAuteur()));
            assertTrue(auteurDb.getPrenomAuteur().equals(resultDb.getPrenomAuteur()));

            result.close();
            statement.close();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void update() {
        auteurDao.insert(auteurDb);
        DaoTestsUtils.setIdentifiantToAuteur(auteurDb);
        auteurDb.setNomAuteur("nomTestUpdated");
        auteurDb.setPrenomAuteur("prenomTestUpdated");
        auteurDao.update(auteurDb);

        try {
            AuteurDb resultDb = new AuteurDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "auteur", auteurDb.getIdentifiantAuteur()));
            ResultSet result = statement.executeQuery();
            result.next();
            resultDb.setNomAuteur(result.getString("nomAuteur"));
            resultDb.setPrenomAuteur(result.getString("prenomAuteur"));

            assertTrue("nomTestUpdated".equals(resultDb.getNomAuteur()));
            assertTrue("prenomTestUpdated".equals(resultDb.getPrenomAuteur()));

            result.close();
            statement.close();

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Test
    public void delete() {
        auteurDao.insert(auteurDb);
        DaoTestsUtils.setIdentifiantToAuteur(auteurDb);
        auteurDao.delete(auteurDb);

        try {
            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "auteur", auteurDb.getIdentifiantAuteur()));
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
        auteurDao.insert(auteurDb);
        DaoTestsUtils.setIdentifiantToAuteur(auteurDb);

        try {

            AuteurDb auteur = auteurDao.find(auteurDb.getIdentifiantAuteur());

            assertTrue(auteurDb.getIdentifiantAuteur().equals(auteur.getIdentifiantAuteur()));
            assertTrue(auteurDb.getNomAuteur().equals(auteur.getNomAuteur()));
            assertTrue(auteurDb.getPrenomAuteur().equals(auteur.getPrenomAuteur()));

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun auteur n'est présent dans la base de données.");
        }
    }

    @Test
    public void findAll() {
        AuteurDb auteurDb2 = new AuteurDb();
        auteurDb2.setNomAuteur("nomTest2");
        auteurDb2.setPrenomAuteur("prenomTest2");

        auteurDao.insert(auteurDb);
        auteurDao.insert(auteurDb2);

        try {
            List<AuteurDb> auteursList = auteurDao.findAll();

            assertTrue(auteursList.size() >= 2);

        } catch (NullPointerException npe) {
            throw new NullPointerException("Aucun auteur n'est présent dans la base de données.");
        }
    }

    // DATABASE CLEARING (TEST DATA REMOVING)
    @After
    public void reset() throws Exception {
        Statement statement = SQLiteConnection.getInstance().createStatement();
        statement.executeUpdate("DELETE FROM auteur WHERE nomAuteur IN ('nomTest', 'nomTest2', 'nomTestUpdated')");

        Integer lastIdAuteur = 0;

        try (PreparedStatement sequenceStatement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.getLastIdOfTable("auteur"))) {
            try (ResultSet sequenceResult = sequenceStatement.executeQuery()) {
                lastIdAuteur = sequenceResult.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }

        statement.executeUpdate("UPDATE sqlite_sequence SET seq = '" + lastIdAuteur + "' WHERE name = 'auteur'");
        statement.close();
    }
}
