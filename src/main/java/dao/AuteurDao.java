package dao;

import database.SQLiteConnection;
import db.AuteurDb;
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

public class AuteurDao implements AbstractDao<AuteurDb> {

    private static final Logger LOG = LogManager.getLogger(AuteurDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private static final String AUTEUR = "auteur";
    private static final String SQL_EXCEPTION = "SQLException : ";

    @Override
    public void insert(AuteurDb auteurDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery(AUTEUR, auteurDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void update(AuteurDb auteurDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery(AUTEUR, auteurDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void delete(AuteurDb auteurDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery(AUTEUR, auteurDb.getIdentifiantAuteur()))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public AuteurDb find(int id) {
        AuteurDb auteurDb = new AuteurDb();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                AUTEUR, id))) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                auteurDb.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
                auteurDb.setNomAuteur(result.getString("nomAuteur"));
                auteurDb.setPrenomAuteur(result.getString("prenomAuteur"));
            }
            return auteurDb;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<AuteurDb> findAll() {
        List<AuteurDb> auteursList = new ArrayList<>();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                AUTEUR))) {
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    AuteurDb auteurDb = new AuteurDb();

                    auteurDb.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
                    auteurDb.setNomAuteur(result.getString("nomAuteur"));
                    auteurDb.setPrenomAuteur(result.getString("prenomAuteur"));

                    auteursList.add(auteurDb);
                }
            }
            return auteursList;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
