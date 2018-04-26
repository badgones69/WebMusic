package dao;

import database.SQLiteConnection;
import db.AlbumDb;
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

public class AlbumDao extends AbstractDao<AlbumDb> {

    private static final Logger LOG = LogManager.getLogger(AlbumDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();

    public AlbumDao() {
    }

    @Override
    public void insert(AlbumDb albumDb) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery("album", albumDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AlbumDb albumDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery("album", albumDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(AlbumDb albumDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery("album", albumDb.getNumeroAlbum()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AlbumDb find(int id) throws TransactionalException {

        try {
            AlbumDb albumDb = new AlbumDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "album", id));
            ResultSet result = statement.executeQuery();
            result.next();
            albumDb.setNumeroAlbum(result.getInt("numeroAlbum"));
            albumDb.setTitreAlbum(result.getString("titreAlbum"));

            result.close();
            statement.close();

            return albumDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AlbumDb> findAll() {

        try {
            List<AlbumDb> albumsList = new ArrayList<>();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                    "album"));
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                AlbumDb albumDb = new AlbumDb();

                albumDb.setNumeroAlbum(result.getInt("numeroAlbum"));
                albumDb.setTitreAlbum(result.getString("titreAlbum"));

                albumsList.add(albumDb);
            }

            result.close();
            statement.close();

            return albumsList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
