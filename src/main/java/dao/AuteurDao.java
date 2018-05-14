package dao;

import database.SQLiteConnection;
import db.AuteurDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DaoQueryUtils;

import javax.transaction.TransactionalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuteurDao extends AbstractDao<AuteurDb> {

    private static final Logger LOG = LogManager.getLogger(AuteurDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();

    @Override
    public void insert(AuteurDb auteurDb) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery("auteur", auteurDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AuteurDb auteurDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery("auteur", auteurDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(AuteurDb auteurDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery("auteur", auteurDb.getIdentifiantAuteur()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AuteurDb find(int id) throws TransactionalException {

        try {
            AuteurDb auteurDb = new AuteurDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "auteur", id));
            ResultSet result = statement.executeQuery();
            result.next();
            auteurDb.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
            auteurDb.setNomAuteur(result.getString("nomAuteur"));
            auteurDb.setPrenomAuteur(result.getString("prenomAuteur"));

            result.close();
            statement.close();

            return auteurDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AuteurDb> findAll() {

        try {
            List<AuteurDb> auteursList = new ArrayList<>();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                    "auteur"));
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                AuteurDb auteurDb = new AuteurDb();

                auteurDb.setIdentifiantAuteur(result.getInt("identifiantAuteur"));
                auteurDb.setNomAuteur(result.getString("nomAuteur"));
                auteurDb.setPrenomAuteur(result.getString("prenomAuteur"));

                auteursList.add(auteurDb);
            }

            result.close();
            statement.close();

            return auteursList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
