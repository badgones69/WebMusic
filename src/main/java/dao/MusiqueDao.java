package dao;

import database.SQLiteConnection;
import db.MusiqueDb;
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

public class MusiqueDao extends AbstractDao<MusiqueDb> {

    private static final Logger LOG = LogManager.getLogger(MusiqueDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private AlbumDao albumDao = new AlbumDao();

    @Override
    public void insert(MusiqueDb musiqueDb) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery("musique", musiqueDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(MusiqueDb musiqueDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery("musique", musiqueDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MusiqueDb musiqueDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery("musique", musiqueDb.getCodeMusique()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MusiqueDb find(int id) throws TransactionalException {

        try {
            MusiqueDb musiqueDb = new MusiqueDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "musique", id));
            ResultSet result = statement.executeQuery();
            result.next();
            musiqueDb.setCodeMusique(result.getInt("codeMusique"));
            musiqueDb.setTitreMusique(result.getString("titreMusique"));
            musiqueDb.setDureeMusique(result.getString("dureeMusique"));
            musiqueDb.setDateInsertionMusique(result.getString("dateInsertionMusique"));
            musiqueDb.setNomFichierMusique(result.getString("nomFichierMusique"));
            musiqueDb.setAlbumMusique(albumDao.find(result.getInt("albumMusique")));

            result.close();
            statement.close();

            return musiqueDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MusiqueDb> findAll() {

        try {
            List<MusiqueDb> musiquesList = new ArrayList<>();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                    "musique"));
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                MusiqueDb musiqueDb = new MusiqueDb();

                musiqueDb.setCodeMusique(result.getInt("codeMusique"));
                musiqueDb.setTitreMusique(result.getString("titreMusique"));
                musiqueDb.setDureeMusique(result.getString("dureeMusique"));
                musiqueDb.setDateInsertionMusique(result.getString("dateInsertionMusique"));
                musiqueDb.setNomFichierMusique(result.getString("nomFichierMusique"));
                musiqueDb.setAlbumMusique(albumDao.find(result.getInt("albumMusique")));

                musiquesList.add(musiqueDb);
            }

            result.close();
            statement.close();

            return musiquesList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
