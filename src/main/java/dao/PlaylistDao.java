package dao;

import database.SQLiteConnection;
import db.PlaylistDb;
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

public class PlaylistDao extends AbstractDao<PlaylistDb> {

    private static final Logger LOG = LogManager.getLogger(PlaylistDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();


    @Override
    public void insert(PlaylistDb playlistDb) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery("playlist", playlistDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(PlaylistDb playlistDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery("playlist", playlistDb));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PlaylistDb playlistDb) throws TransactionalException {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery("playlist", playlistDb.getIdPlaylist()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlaylistDb find(int id) throws TransactionalException {
        try {
            PlaylistDb playlistDb = new PlaylistDb();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                    "playlist", id));
            ResultSet result = statement.executeQuery();
            result.next();
            playlistDb.setIdPlaylist(result.getInt("idPlaylist"));
            playlistDb.setIntitulePlaylist(result.getString("intitulePlaylist"));

            result.close();
            statement.close();

            return playlistDb;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PlaylistDb> findAll() {

        try {
            List<PlaylistDb> playlistsList = new ArrayList<>();

            PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                    "playlist"));
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                PlaylistDb playlistDb = new PlaylistDb();

                playlistDb.setIdPlaylist(result.getInt("idPlaylist"));
                playlistDb.setIntitulePlaylist(result.getString("intitulePlaylist"));

                playlistsList.add(playlistDb);
            }

            result.close();
            statement.close();

            return playlistsList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
