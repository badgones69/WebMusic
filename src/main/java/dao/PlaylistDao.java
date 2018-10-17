package dao;

import database.SQLiteConnection;
import db.PlaylistDb;
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

public class PlaylistDao implements AbstractDao<PlaylistDb> {

    private static final Logger LOG = LogManager.getLogger(PlaylistDao.class);
    private static final Connection CONNECTION = SQLiteConnection.getInstance();
    private static final String PLAYLIST = "playlist";
    private static final String SQL_EXCEPTION = "SQLException : ";


    @Override
    public void insert(PlaylistDb playlistDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateInsertingQuery(PLAYLIST, playlistDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void update(PlaylistDb playlistDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateUpdatingQuery(PLAYLIST, playlistDb))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public void delete(PlaylistDb playlistDb) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(DaoQueryUtils.generateDeletingQuery(PLAYLIST, playlistDb.getIdPlaylist()))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
    }

    @Override
    public PlaylistDb find(int id) {
        PlaylistDb playlistDb = new PlaylistDb();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingByIdQuery(
                PLAYLIST, id))) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                playlistDb.setIdPlaylist(result.getInt("idPlaylist"));
                playlistDb.setIntitulePlaylist(result.getString("intitulePlaylist"));
            }
            return playlistDb;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<PlaylistDb> findAll() {
        List<PlaylistDb> playlistsList = new ArrayList<>();

        try (PreparedStatement statement = SQLiteConnection.getInstance().prepareStatement(DaoQueryUtils.generateFindingAllQuery(
                PLAYLIST))) {
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    PlaylistDb playlistDb = new PlaylistDb();

                    playlistDb.setIdPlaylist(result.getInt("idPlaylist"));
                    playlistDb.setIntitulePlaylist(result.getString("intitulePlaylist"));

                    playlistsList.add(playlistDb);
                }
            }
            return playlistsList;

        } catch (SQLException e) {
            LOG.error(SQL_EXCEPTION + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
